import { Component, OnInit } from "@angular/core";
import { ActivatedRoute, Router } from '@angular/router';
import { TextService } from "../sevices/textService";
import { Text } from '../model/text'
import { Word } from "../model/word";

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

    constructor(private textService:TextService, private router: Router){}

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
        this.textService.saveWords(this.pendingWords).subscribe(
            () => {
                this.pendingWords = [];
                this.palabrasAVerificar = false;
            },
            (error) => console.error("Error saving pending words", error)
        )
    }

    onSubmit(){
        this.textService.getPendingWords(this.text.text).subscribe(
            (pendingWords) => {
                this.pendingWords = pendingWords;
                if(this.pendingWords.length == 0){
                    this.palabrasAVerificar = false;
                    this.textService.postText(this.text).subscribe(
                        (createdText) => {
                            if(createdText.id && this.selectedFile){
                                this.textService.createTextImage(createdText.id, this.selectedFile).subscribe(
                                    () => this.clearForm(),
                                    (error) => console.error("Error uploading the text image", error)
                                )
                            }
                        },
                        (error) => console.error("Error al crear el texto", error)
                    );
                } else {
                    this.palabrasAVerificar = true;
                }
            },
            (error) => console.error("Error getting pending words", error)
        )
    }

    back(){this.router.navigate(['/'])}

}