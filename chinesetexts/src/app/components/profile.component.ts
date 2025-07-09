import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { LoginService } from "../sevices/loginService";
import { UserService } from "../sevices/userService";
import { UserWithPassword } from "../model/userWithPassword";
import { Word } from "../model/word";
import { WordService } from "../sevices/wordService";


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
    errorMessage: string = '';
    textError: boolean = false;
    textSuccess: boolean = false;

    editedWord: Word = {chinese: '', pinyin: '', english: '', spanish: ''};
    errorEditWord: boolean = false;
    successEditWord: boolean = false;
    deleteEmail: string = '';
    successDeleteEmail: boolean = false;
    errorDeleteEmail: boolean = false;

    constructor(private router: Router, public loginService: LoginService, private userService: UserService, private wordService: WordService){}

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
            this.editedWord.chinese = '';
            this.editedWord.pinyin = '';
            this.editedWord.english = '';
            this.editedWord.spanish = '';
            this.errorEditWord = false;
            this.deleteEmail = '';
            this.errorDeleteEmail = false;
          },
          (error) => console.error("Error getting the user", error)
        );
      }
    }

    onSubmit(){
      this.cancelEdit();
      console.log(this.user.id);
      this.userService.putUser(this.user).subscribe(
        (userDto) => {
          console.log(userDto)
          if(userDto.email == "error"){
            this.errorMessage = "The information could not be updated. Please make sure the current password is correct and that the new password is not empty.";
            this.textError = true;
            return;
          }
          this.init();
          this.textSuccess = true;
        },
        () => {
          this.errorMessage = "An error occurred while updating the user";
          this.textError = true;
        }
      )
    }

    cancelEdit(){
      this.editMode = false;
      this.textSuccess = false;
      this.textError = false;
    }

    editWord(){
      console.log('Editing word', this.editedWord);

      this.editedWord.chinese = this.editedWord.chinese.trim();
      this.editedWord.pinyin = this.editedWord.pinyin.trim();
      this.editedWord.english = this.editedWord.english.trim();
      this.editedWord.spanish = this.editedWord.spanish.trim();

      if(this.editedWord.chinese !== ''){
        this.wordService.putWord(this.editedWord).subscribe(
          (response) => {
            console.log('Word edited successfully', response);
            this.successEditWord = true;
            this.errorEditWord = false;
            this.editedWord.chinese = '';
            this.editedWord.pinyin = '';
            this.editedWord.english = '';
            this.editedWord.spanish = '';
          },
          (error) => {  //En teoría no va a entrar por aqui, porque si la palabra no existe, entonces la crea.
            console.error('Error editing word', error);
            this.errorEditWord = true;
            this.successEditWord = false;
            this.editedWord.chinese = '';
            this.editedWord.pinyin = '';
            this.editedWord.english = '';
            this.editedWord.spanish = '';
          }
        );
      } else {
        this.errorEditWord = true;
        this.successEditWord = false;
        this.editedWord.chinese = '';
        this.editedWord.pinyin = '';
        this.editedWord.english = '';
        this.editedWord.spanish = '';
      }
    }

    deleteUser(){
      this.userService.deleteUser(this.deleteEmail).subscribe(
        () => {
          console.log("User deleted successfully");
          this.successDeleteEmail = true;
          this.errorDeleteEmail = false;
          this.deleteEmail = '';
        },
        (error) => {
          console.error("Error deleting user", error);
          this.errorDeleteEmail = true;
          this.successDeleteEmail = false;
          this.deleteEmail = '';
        }
      );
    }

    back(){this.router.navigate(['/'])}
}
