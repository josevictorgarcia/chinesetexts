import { Component } from '@angular/core';

@Component({
  selector: 'header',
  templateUrl: './header.component.html',
  styleUrls:['../app.component.css']
})
export class AppHeader {
    showLoginForm = false;
    loginEmail = '';
    loginPassword = '';


    public login() {
      if (this.loginEmail && this.loginPassword) {
        // Aquí iría la lógica para autenticar al usuario
        console.log('Iniciando sesión con:', this.loginEmail, this.loginPassword);
        alert('Iniciando sesión... (Aquí iría la lógica de autenticación)');
      } else {
        alert('Por favor, completa todos los campos.');
      }
    }

    public toggleLoginForm() {
      this.showLoginForm = !this.showLoginForm;
      // Resetear campos si se cierra el formulario
      if (!this.showLoginForm) {
        this.loginEmail = '';
        this.loginPassword = '';
      }
    }

    public cancelLogin() {
      this.showLoginForm = false;
      this.loginEmail = '';
      this.loginPassword = '';
    }

}
