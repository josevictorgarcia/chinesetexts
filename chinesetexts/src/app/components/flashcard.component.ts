import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FlashcardService } from '../sevices/flashcardService';
import { Flashcard } from '../model/flashcard';

@Component({
  selector: 'flashcard',
  templateUrl: './flashcard.component.html',
})
export class FlashcardPage implements OnInit{

    flashcards : Flashcard[] = [];

    constructor(private router: Router, private flashcardService:FlashcardService, private activatedRoute: ActivatedRoute){}

    ngOnInit(): void {
      this.init();
    }

    private init(){
      const collectionId = this.activatedRoute.snapshot.params['id'];
      this.getFlashcards(collectionId);
    }

    private getFlashcards(id: number){
      this.flashcardService.getFlashcards(id).subscribe(
        (flashcards) => {this.flashcards = flashcards; console.log(this.flashcards)},
        (error) => console.error("Error getting flashcards", error)
      )
    }

    back(){this.router.navigate(['/flashcards'])}

}