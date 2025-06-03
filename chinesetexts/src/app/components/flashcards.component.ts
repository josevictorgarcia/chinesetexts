import { Component } from '@angular/core';
import { TextService } from '../sevices/textService';
import { WordService } from '../sevices/wordService';
import { Router } from '@angular/router';

@Component({
  selector: 'flashcards',
  templateUrl: './flashcards.component.html',
})
export class Flashcards {
  
    constructor(private router: Router){}

    back(){this.router.navigate(['/'])}
}