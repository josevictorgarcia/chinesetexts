import { Component, OnInit } from '@angular/core';
import { TextService } from '../sevices/textService';
import { WordService } from '../sevices/wordService';
import { Router } from '@angular/router';
import { FlashcardService } from '../sevices/flashcardService';
import { Collection } from '../model/collection';

@Component({
  selector: 'flashcards',
  templateUrl: './flashcards.component.html',
})
export class Flashcards implements OnInit{

    collections: Collection[] = [];
  
    constructor(private flashcardService:FlashcardService, private router: Router){}

    ngOnInit(): void {
        this.init();
    }

    private init() {
      this.flashcardService.getCollections().subscribe(
        (collections) => {this.collections = collections; console.log(this.collections)},
        (error) => console.error("Error getting collections", error)
      )
    }

    back(){this.router.navigate(['/'])}
}