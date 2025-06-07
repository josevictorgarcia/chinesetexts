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
    }

    onSubmit(){
        this.userService.postUser(this.user).subscribe(
            () => {
                this.clearForm();
                this.router.navigate(['/'])
            },
            (error) => console.error("Error creating the user", error)
        );
    }

    back(){this.router.navigate(['/'])}

}