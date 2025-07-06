import { Component, TemplateRef, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from '../sevices/loginService';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'header',
  templateUrl: './header.component.html',
  styleUrls:['../app.component.css']
})
export class AppHeader {
    showLoginForm = false;
    loginEmail = '';
    loginPassword = '';
    messageError = '';

    @ViewChild('loginErrorModal', { static: true })
    public loginErrorModal: TemplateRef<void> | undefined;
    
    //Con configurar el traductor en solo un componente es suficiente. Pero hay que tener en cuenta que ese componente tiene que estar todo el rato en pantalla (para que el traductor esté "presente" en todo momento). Por eso lo ponemos en el header, porque el header siempre se va a encontrar en todas las páginas de la aplicación web.
    constructor(
      public loginService: LoginService, // Cambiado a público para acceder desde la plantilla
      private router: Router,
      private translate: TranslateService
    ) {
      this.translate.setDefaultLang('en'); // Establecer el idioma por defecto
      this.translate.use('en');
    }

    public changeLanguage(language: string) {
      this.translate.use(language); // Cambiar el idioma
    }

    public login() {
      if (this.loginEmail && this.loginPassword) {
        this.loginService.login(this.loginEmail, this.loginPassword).subscribe(
        () => {
          this.loginService.reqIsLogged().subscribe(
            () => this.toggleLoginForm() // Cerrar el formulario de inicio de sesión
          )
        },
        () => {
          //this.modalService.open(this.loginErrorModal, { centered: true });
          this.loginEmail = '';
          this.loginPassword = '';
          this.messageError = '"Incorrect credentials. Please try again."'
        });
      } else {
        this.messageError = 'Please fill in all fields.';
      }
    }

    public logout(): void {
      this.loginService.logout().subscribe({
        next: () => {
          // Aquí el logout ya se completó
          this.router.navigate(['/']);
          console.log(this.loginService.isLogged()); // Debe mostrar false
        },
        error: (error) => {
          console.error("Error en logout:", error);
        }
      });
    }

    public toggleLoginForm() {
      this.showLoginForm = !this.showLoginForm;
      // Resetear campos si se cierra el formulario
      if (!this.showLoginForm) {
        this.loginEmail = '';
        this.loginPassword = '';
        this.messageError = '';
      }
    }

    public cancelLogin() {
      this.showLoginForm = false;
      this.loginEmail = '';
      this.loginPassword = '';
      this.messageError = '';
    }

}
