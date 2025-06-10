import { Component, OnInit } from '@angular/core';
import { TextService } from '../sevices/textService';
import { WordService } from '../sevices/wordService';
import { Router } from '@angular/router';
import { FlashcardService } from '../sevices/flashcardService';
import { Collection } from '../model/collection';
import { LoginService } from '../sevices/loginService';

@Component({
  selector: 'flashcards',
  templateUrl: './flashcards.component.html',
})
export class Flashcards implements OnInit{

    collections: Collection[] = [];
    displayFormCreateCollection: boolean = false;
    name: string = '';
  
    constructor(private flashcardService:FlashcardService, public loginService: LoginService, private router: Router){}

    ngOnInit(): void {
      this.loginService.reqIsLogged().subscribe(() => {
        this.init(); // o lo que necesites hacer una vez recuperado el usuario
      });
    }

    private init() {
      this.loginService.reqIsLogged();
      const email = this.loginService.currentUser()?.email;
      if(email){
        this.flashcardService.getCollections(email).subscribe(
          (collections) => {this.collections = collections; console.log(this.collections)}
        );
      }
      console.log(this.loginService.currentUser());
      this.name = '';
      this.displayFormCreateCollection = false;
    }

    formCreateCollection(){
      this.displayFormCreateCollection = !this.displayFormCreateCollection;
    }

    onSubmit(){
      //console.log(this.loginService.currentUser()?.email, this.loginService.currentUser()?.id)
      const email = this.loginService.currentUser()?.email;
      if(email)
      this.flashcardService.postCollection(this.name, email).subscribe(
        () => { this.init() },
        (error) => console.error("An error occurred while creating the collection")
      )
    }

    back(){this.router.navigate(['/'])}
}