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
  
    constructor(private flashcardService:FlashcardService, public loginService: LoginService, private router: Router){}

    ngOnInit(): void {
        this.init();
    }

    private init() {
      this.collections = this.loginService.currentUser()?.collections ?? [];
    }

    back(){this.router.navigate(['/'])}
}