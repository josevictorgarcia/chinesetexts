import { AfterViewChecked, Component, OnInit } from '@angular/core';
import { TextService } from '../sevices/textService';
import { ActivatedRoute, Router } from '@angular/router';
import { Text } from '../model/text'
import { Word } from '../model/word';
import { LoginService } from '../sevices/loginService';
declare var bootstrap: any;

@Component({
  selector: 'text-page',
  templateUrl: './text-page.component.html',
})
export class TextPage implements OnInit{
  /* text: Text = {
    title: "",
    text: "",
    translation: ""
  } */

  /*id: number = 0;
  originalText: [] = [];
  translatedText: string[] = [];
  translatedSpanishText: string[] = [];
  translation: { [key: string]: string } = {};
  word: Word = {
    chinese: "",
    english: "",
    spanish: ""
  }*/

    text: Text = {
      id: 0,
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

    originalText: string[] = [];
    translatedSpanishText: string[] = [];
    originalTextSeparatedBySentences: string[] = [];
    translatedSpanishTextSeparatedBySentences: string[] = [];

    showTextSplitIntoWords = true;
    showOriginalText = true;

    showErrorModal: boolean = false;
    errorMessage: string = '';

  constructor(private textService:TextService, private router: Router, private activatedRoute: ActivatedRoute, private loginService:LoginService){}

  /* ngOnInit(): void {
    let id = this.activatedRoute.snapshot.params['id']
    this.textService.getText(id).subscribe(
        (text) => this.text = text,
        (error) => console.error("Error al obtener la pagina del texto", error)
    )
  } */

    ngOnInit(): void {
      this.init();
    }
    
    private init(): void {
      const id = this.activatedRoute.snapshot.params['id'];
      this.getText(id);
      this.getSpanishText(id);
    }

    private initPopovers() {
      const popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
      const popoverList = popoverTriggerList.map(function (popoverTriggerEl: HTMLElement) {
        return new bootstrap.Popover(popoverTriggerEl, {
          html: true,
          content: popoverTriggerEl.getAttribute('data-bs-content') || '',
          trigger: 'manual' // usamos 'manual' para tener control total
        });
      });

      // Mostrar el popover al hacer clic
      popoverTriggerList.forEach((triggerEl: HTMLElement, index: number) => {
        triggerEl.addEventListener('click', (e) => {
          e.stopPropagation(); // evitar que se cierre inmediatamente
          const popover = bootstrap.Popover.getInstance(triggerEl);

          // Cerrar todos los popovers antes de abrir este
          popoverList.forEach((p, i) => {
            if (i !== index) {
              const el = popoverTriggerList[i];
              const inst = bootstrap.Popover.getInstance(el);
              inst?.hide();
            }
          });

          if (popover) {
            popover.toggle();
          }

          // Agregar el listener para el botón dentro del popover
          setTimeout(() => {
            const popoverButton = document.getElementById(`popover-button-${index}`);
            if (popoverButton) {
              popoverButton.addEventListener('click', (clickEvent) => {
                // Llamar a la función addWord cuando se hace clic en el contenido del popover
                this.addWord(this.originalText[index]);
                clickEvent.stopPropagation(); // Evitar que el clic se propague al popover
              });
            }
          }, 50);
        });
      });

      // Cerrar popovers al hacer clic fuera
      document.addEventListener('click', () => {
        popoverTriggerList.forEach((el: HTMLElement) => {
          const popover = bootstrap.Popover.getInstance(el);
          popover?.hide();
        });
      });
    }

    private getText(id: number): void{
      this.textService.getText(id).subscribe(
        (text) => this.text = text,
        (error) => console.error("Error loading the text", error)
      );
    }

    private getSpanishText(id: number){
      this.textService.getSpanishText(id).subscribe(
        (data) => {this.getTexts(data); setTimeout(() => this.initPopovers(), 0);},
        (error) => console.error("Error loading the text and its translation to Spanish", error)
      )
    }

    private getTexts(data: string[][]){
      this.originalText = data[0]; 
      this.translatedSpanishText = data[1];
      this.originalTextSeparatedBySentences = this.getSentences(data[0]);
      this.translatedSpanishTextSeparatedBySentences = this.getSentencesString(this.text.spanishTranslation);
    }

    private getSentences(text: string[]): string[] {
      const sentences: string[] = [];
      let currentSentence: string[] = [];

      // Iteramos sobre las palabras del texto
      for (const word of text) {
        // Agregamos la palabra a la frase actual
        currentSentence.push(word);

        // Comprobamos si la palabra termina con un punto o un punto chino "。"
        if (word.endsWith('.') || word.endsWith('。')) {
          // Unimos las palabras en una frase completa y la agregamos al array de oraciones
          sentences.push(currentSentence.join(' '));
          // Reiniciamos la frase actual
          currentSentence = [];
        }
      }

      // Si hay alguna frase que no se ha agregado (en caso de que no termine con un delimitador)
      if (currentSentence.length > 0) {
        sentences.push(currentSentence.join(' '));
      }

      return sentences;
    }

    private getSentencesString(text: string): string[] {
      // Usamos una expresión regular para dividir el texto en frases
      const sentences = text.split(/(?<=\.|。)(?=\s|$)/).filter(sentence => sentence.trim() !== '');
      return sentences;
    }


    getPopoverHtml(i: number): string {
      // Aquí defines el HTML que quieres mostrar en el tooltip
      // Por ejemplo:
      return `
        <div>
          <strong>Traducción:</strong> ${this.translatedSpanishText[i] || 'Sin traducción'}
          <br>
          <div class="btn btn-link" id="popover-button-${i}">Guardar</div>
        </div>
      `;
    }

    getTooltipHtml(i: number): string {
      // Aquí defines el HTML que quieres mostrar en el tooltip
      // Por ejemplo:
      return `
        <div>
          <strong>Traducción:</strong> ${this.originalTextSeparatedBySentences[i] || 'Sin traducción'}
          <br>
        </div>
      `;
    }

    getTooltipHtmlOriginal(i: number): string {
      // Aquí defines el HTML que quieres mostrar en el tooltip
      // Por ejemplo:
      return `
        <div>
          <strong>Traducción:</strong> ${this.translatedSpanishTextSeparatedBySentences[i] || 'Sin traducción'}
          <br>
        </div>
      `;
    }

    addWord(word: string){
      console.log(word);
      if(this.loginService.isLogged()){
        const currentUser = this.loginService.currentUser();
        if (currentUser?.collections && Array.isArray(currentUser.collections) && currentUser.collections.length > 0) {
          console.log(this.loginService.currentUser()?.collections);
        } else {
          this.showError("Debes crear una colección desde el apartado de 'flashcards' para comenzar a guardar palabras")
        }
      } else {
        this.showError("Debes iniciar sesión para guardar palabras.")
      }
    }

    showError(message: string) {
      this.errorMessage = message;
      this.showErrorModal = true;
    }

    closeErrorModal() {
      this.showErrorModal = false;
    }

    changeSplit(){
      this.showTextSplitIntoWords = !this.showTextSplitIntoWords;
      this.init();
    }

    changeTranslation(){
      this.showOriginalText = !this.showOriginalText;
      this.init();
    }

    back(){this.router.navigate(['/'])}

    /*private init(): void {
      this.id = this.activatedRoute.snapshot.params['id']
      this.getEnglishText();
      this.getSpanishText();
      this.getTranslation();
    }

    private getEnglishText(){
      this.textService.getEnglishText(this.id).subscribe(
        (data) => {this.originalText = data[0]; this.translatedText = data[1]},
        (error) => console.error("Error loading the text and its translation to English", error)
      )
    }

    private getSpanishText(){
      this.textService.getSpanishText(this.id).subscribe(
        (data) => {this.originalText = data[0]; this.translatedSpanishText = data[1]},
        (error) => console.error("Error loading the text and its translation to Spanish", error)
      )
    }

    private getTranslation(){
      this.textService.getTranslation(this.id).subscribe(
        (translation) => this.translation = translation,
        (error) => console.error("Error getting the text translation", error)
      )
    }

    private clearForm(){
      this.word.chinese = "";
      this.word.english = "";
      this.word.spanish = "";
    }

    addWord(){
      this.textService.postWord(this.word).subscribe(
        () => {
          this.clearForm(); 
          this.init()},
        (error) => console.error("Error creating a new word", error)
      )
    }


    objectKeys(obj: any): string[] {
      return Object.keys(obj);
    }*/
  
}