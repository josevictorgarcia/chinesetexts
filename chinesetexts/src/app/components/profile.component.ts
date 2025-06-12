import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { LoginService } from "../sevices/loginService";
import { UserService } from "../sevices/userService";
import { UserWithPassword } from "../model/userWithPassword";


@Component({
  selector: 'profile',
  templateUrl: './profile.component.html',
})
export class Profile implements OnInit{

    user: UserWithPassword = {
      email: '',
      name: '',
      password: '',
      newPassword: '',
      language: '',
      collections: [],
      roles: []
    }

    editMode: boolean = false;
    textError: boolean = false;
    textSuccess: boolean = false;

    constructor(private router: Router, public loginService: LoginService, private userService: UserService){}

    ngOnInit(): void {
      this.init();
    }

    private init(){
      const email = this.loginService.currentUser()?.email
      if(email){
        this.userService.getUser(email).subscribe(
          (user) => {
            this.user = user;
            this.user.password = '';
            this.user.newPassword = '';
          },
          (error) => console.error("Error getting the user", error)
        );
      }
    }

    onSubmit(){
      this.cancelEdit();
      console.log(this.user.id);
      this.userService.putUser(this.user).subscribe(
        () => {
          this.init();
          this.textSuccess = true;
        },
        () => this.textError = true
      )
    }

    cancelEdit(){
      this.editMode = false;
      this.textSuccess = false;
      this.textError = false;
    }

    back(){this.router.navigate(['/'])}
}
