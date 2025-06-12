import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FlashcardService } from '../sevices/flashcardService';
import { Flashcard } from '../model/flashcard';

interface Question {
  prompt: string;       // enunciado (chinese)
  options: string[];    // opciones (pinyin)
  correctAnswer: string;
}

@Component({
  selector: 'flashcard',
  templateUrl: './flashcard.component.html',
})
export class FlashcardPage implements OnInit{

    flashcards : Flashcard[] = [];

    displayExam: boolean = false;
    enoughWords: boolean = false;

    questions: Question[] = [];
    selectedAnswers: string[] = [];
    score: number = 0;
    examFinished: boolean = false;

    constructor(private router: Router, private flashcardService:FlashcardService, private activatedRoute: ActivatedRoute){}

    ngOnInit(): void {
      this.init();
    }

    private init(){
      const collectionId = this.activatedRoute.snapshot.params['id'];
      this.getFlashcards(collectionId);
      this.displayExam = false;
    }

    private getFlashcards(id: number){
      this.flashcardService.getFlashcards(id).subscribe(
        (flashcards) => {
          this.flashcards = flashcards; 
          console.log(this.flashcards);
          this.enoughWords = (this.flashcards.length >= 10);
        },
        (error) => console.error("Error getting flashcards", error)
      )
    }

    startExam(){
      console.log("Starting exam");
      this.displayExam = !this.displayExam;
      if(this.enoughWords){
        this.questions = [];
        this.examFinished = false;
        this.score = 0;

        const flashcardsCopy = [...this.flashcards];
        flashcardsCopy.sort(() => Math.random() - 0.5); // Mezclar flashcards

        for (let i = 0; i < 10; i++) {
          const flashcard = flashcardsCopy[i];
          const prompt = flashcard.word.chinese;

          // Aleatoriamente decidimos si será una pregunta de pinyin o de español
          const answerType = Math.random() < 0.5 ? 'pinyin' : 'spanish';

          const correctAnswer = flashcard.word[answerType];

          // Generar opciones incorrectas
          let distractors = this.flashcards
            .filter(fc => fc.word[answerType] !== correctAnswer)
            .map(fc => fc.word[answerType]);

          // Mezclar y tomar 3 incorrectas
          distractors = this.shuffleArray(distractors).slice(0, 3);

          // Mezclar opciones con la correcta
          const options = this.shuffleArray([...distractors, correctAnswer]);

          this.questions.push({
            prompt,
            options,
            correctAnswer,
          });
        }

        this.selectedAnswers = new Array(this.questions.length).fill(null);
        console.log("Generated questions:", this.questions);
      }
    }

    private shuffleArray(array: any[]): any[] {
      return array
        .map(value => ({ value, sort: Math.random() }))
        .sort((a, b) => a.sort - b.sort)
        .map(({ value }) => value);
    }

    finishExam() {
      this.score = this.questions.reduce((acc, question, index) => {
        return acc + (this.selectedAnswers[index] === question.correctAnswer ? 1 : 0);
      }, 0);
      this.examFinished = true;
    }

    back(){this.router.navigate(['/flashcards'])}

}