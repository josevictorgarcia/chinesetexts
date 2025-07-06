import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { User } from "../model/user";
import { UserWithPassword } from "../model/userWithPassword";
import { UserService } from "../sevices/userService";

@Component({
  selector: 'user-form',
  templateUrl: './user-form.component.html',
})
export class UserForm implements OnInit{

    user: UserWithPassword = {
      email: '',
      name: '',
      password: '',
      language: 'en',
      collections: [],
      roles: ['USER'] // valor por defecto
    };

    errorForm: boolean = false;
    errorMessage: string = '';
    successForm: boolean = false;

    constructor(private userService:UserService, private router: Router){}
    
    ngOnInit(){
      this.clearForm();
    }

    private clearForm(){
        this.user.email = '';
        this.user.name = '';
        this.user.password = '';
        this.user.language = 'en';
        this.user.collections = [];
        this.user.roles = ['USER'];
        this.errorForm = false;
        this.errorMessage = '';
        this.successForm = false;
    }

    private formularioUsuarioValido(): boolean {
        return this.user.email.trim() !== '' &&
               this.user.name.trim() !== '' &&
               this.user.password.trim().length >= 8;
    }

    onSubmit(){
      this.errorForm = false;
      this.errorMessage = '';
      this.successForm = false;
      if(!this.formularioUsuarioValido()){
        this.errorForm = true;
        this.errorMessage = "Error creating the user. Please make sure all fields are filled in and the password is at least 8 characters long."
      } else {
        this.userService.postUser(this.user).subscribe(
            () => {
                this.clearForm();
                this.successForm = true;
            },
            (error) => {
              this.errorForm = true;
              this.errorMessage = "User already exists or there was an error creating the user.";
            }
        );
      }
    }

    back(){this.router.navigate(['/'])}

}