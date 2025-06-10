import { Component, TemplateRef, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from '../sevices/loginService';

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
    
    constructor(
      public loginService: LoginService, // Cambiado a público para acceder desde la plantilla
      private router: Router
    ) {}

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
          this.messageError = 'Credenciales incorrectas. Por favor, intenta de nuevo.'
        });
      } else {
        this.messageError = 'Por favor, completa todos los campos.';
      }
    }

    public logout() {
      this.loginService.logout()
      this.router.navigate(['/']);
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
