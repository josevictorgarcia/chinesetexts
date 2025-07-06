import { Component, OnInit } from "@angular/core";
import { ActivatedRoute, Router } from '@angular/router';
import { TextService } from "../sevices/textService";
import { Text } from '../model/text'
import { Word } from "../model/word";
import { WordService } from "../sevices/wordService";

@Component({
  selector: 'text-form',
  templateUrl: './text-form.component.html',
})

export class TextForm implements OnInit{

    text: Text = {
      titleSpanish: '',
      titleEnglish: '',
      text: '',
      spanishTranslation: '',
      englishTranslation: '',
      level: '',
      englishDescription: '',
      spanishDescription: '',
      creationDate: '',
      translation: ''
    }

    selectedFile: File | null = null;

    palabrasAVerificar: boolean = false;
    pendingWords: Word[] = [];

    errorText: boolean = false;
    messageError: string = '';
    successText: boolean = false;

    constructor(private textService:TextService, private wordService:WordService, private router: Router){}

    ngOnInit(){
        console.log("Page loaded successfully")
    }

    private clearForm(){
        this.text.titleSpanish = '';
        this.text.titleEnglish = '';
        this.text.text = '';
        this.text.spanishTranslation = '';
        this.text.englishTranslation = '';
        this.text.level = '';
        this.text.englishDescription = '';
        this.text.spanishDescription = '';
        this.text.creationDate = '';
        this.text.translation = '';
        this.errorText = false;
        this.messageError = '';
        this.selectedFile = null;
    }

    onFileSelected(event: any) {
        this.selectedFile = event.target.files[0];
    }

    formularioValido(): boolean {
        return this.pendingWords.every(word =>
            word.pinyin && word.pinyin.trim() !== '' &&
            word.english && word.english.trim() !== '' &&
            word.spanish && word.spanish.trim() !== ''
        );
    }

    saveWords(){
        this.wordService.saveWords(this.pendingWords).subscribe(
            () => {
                this.pendingWords = [];
                this.palabrasAVerificar = false;
            },
            (error) => console.error("Error saving pending words", error)
        )
    }

    private formularioTextoValido(): boolean {
        return this.text.titleSpanish.trim() !== '' &&
               this.text.titleEnglish.trim() !== '' &&
               this.text.text.trim() !== '' &&
               this.text.spanishTranslation.trim() !== '' &&
               this.text.englishTranslation.trim() !== '' &&
               this.text.level.trim() !== '' &&
               this.text.englishDescription.trim() !== '' &&
               this.text.spanishDescription.trim() !== '' &&
               this.selectedFile !== null && this.selectedFile !== undefined;
    }

    onSubmit(){
        this.successText = false;
        this.errorText = false;
        this.messageError = '';
        if(!this.formularioTextoValido()){
            this.errorText = true;
            this.messageError = 'Please fill in all fields.';
            return;
        } else {
            this.errorText = false;
            this.messageError = '';
            this.wordService.getPendingWords(this.text.text).subscribe(
                (pendingWords) => {
                    this.pendingWords = pendingWords;
                    if(this.pendingWords.length == 0){
                        this.palabrasAVerificar = false;
                        this.textService.postText(this.text).subscribe(
                            (createdText) => {
                                if(createdText.id && this.selectedFile){
                                    this.textService.createTextImage(createdText.id, this.selectedFile).subscribe(
                                        () => {this.clearForm(); this.successText = true;},
                                        (error) => console.error("Error uploading the text image", error)
                                    )
                                }
                            },
                            (error) => {
                                this.errorText = true;
                                this.messageError = "Error creating the text. Please check that none of the titles in English or Spanish already exist."
                            }
                        );
                    } else {
                        this.palabrasAVerificar = true;
                    }
                },
                (error) => console.error("Error getting pending words", error)
            )
        }
    }

    back(){this.router.navigate(['/'])}

}