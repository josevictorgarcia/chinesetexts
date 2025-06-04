import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'flashcard',
  templateUrl: './flashcard.component.html',
})
export class Flashcard {

    constructor(private router: Router){}

    back(){this.router.navigate(['/flashcards'])}

}