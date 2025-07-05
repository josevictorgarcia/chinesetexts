import { AfterViewChecked, Component, OnInit } from '@angular/core';
import { TextService } from '../sevices/textService';
import { ActivatedRoute, Router } from '@angular/router';
import { Text } from '../model/text'
import { Word } from '../model/word';
import { LoginService } from '../sevices/loginService';
import { Collection } from '../model/collection';
import { FlashcardService } from '../sevices/flashcardService';
import { WordService } from '../sevices/wordService';
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
    wordsArray: Word[] = [];
    translatedSpanishText: string[] = [];
    originalTextSeparatedBySentences: string[] = [];
    translatedSpanishTextSeparatedBySentences: string[] = [];

    showPinyin: boolean = false;
    showTextSplitIntoWords = true;
    showOriginalText = true;

    showErrorModal: boolean = false;
    showSuccessModal: boolean = false;
    errorMessage: string = '';
    successMessage: string = '';
    collections: Collection[] = [];
    wordToAdd: string = '';
    showAddWordSection: boolean = false;

  constructor(private textService:TextService, private wordService:WordService, private router: Router, private activatedRoute: ActivatedRoute, private loginService:LoginService, private flashcardService:FlashcardService){}

  /* ngOnInit(): void {
    let id = this.activatedRoute.snapshot.params['id']
    this.textService.getText(id).subscribe(
        (text) => this.text = text,
        (error) => console.error("Error al obtener la pagina del texto", error)
    )
  } */

    ngOnInit(): void {
      this.loginService.reqIsLogged().subscribe(() => {
        this.init(); // o lo que necesites hacer una vez recuperado el usuario
      });
    }
    
    private init(): void {
      const id = this.activatedRoute.snapshot.params['id'];
      this.getText(id);
      this.getSpanishText(id);
      this.initAddWordSection();
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
        (data) => {
          this.getTexts(data); 
          this.getWords(data[0]);
        },
        (error) => console.error("Error loading the text and its translation to Spanish", error)
      )
    }

    private getTexts(data: string[][]){
      this.originalText = data[0]; 
      this.translatedSpanishText = data[1];
      this.originalTextSeparatedBySentences = this.getSentences(data[0]);
      this.translatedSpanishTextSeparatedBySentences = this.getSentencesString(this.text.spanishTranslation);
    }

    private getWords(chineseText: string[]) {
      this.wordService.getTextWords(chineseText).subscribe(
        (wordsArray) => {
          this.wordsArray = wordsArray; 
          console.log(this.wordsArray.length, this.originalText.length, this.wordsArray)
          setTimeout(() => this.initPopovers(), 0); 
        },
        (error) => console.error("Error loading pinyin for the text", error)
      )
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
          <strong>Pinyin:</strong> ${this.wordsArray[i]?.pinyin || 'Sin pinyin'}
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
      this.initAddWordSection();
      if(this.loginService.isLogged()){
        const currentUser = this.loginService.currentUser();
        if (currentUser?.collections && Array.isArray(currentUser.collections) && currentUser.collections.length > 0) {
          this.wordToAdd = word;
          this.collections = currentUser.collections;
          this.showAddWordSection = true;
        } else {
          this.showError("Debes crear una colección desde el apartado de 'flashcards' para comenzar a guardar palabras. Si el error persiste, recarga la página e inténtalo de nuevo.")
        }
      } else {
        this.showError("Debes iniciar sesión para guardar palabras.")
      }
    }

    saveWordToCollection(collectionId: number | undefined, word: string){
      if(collectionId){
        const id = this.activatedRoute.snapshot.params['id'];
        this.flashcardService.putWordToCollection(collectionId, word, id).subscribe(
          (message) => {
            console.log(message);
            this.initAddWordSection();
            this.showSuccess('Palabra guardada con éxito');
          },
          (error) => this.showError("Word could not be added to collection")
        )
      } else {
        this.showError("Error inesperado al añadir la palabra")
      }
    }

    public initAddWordSection(){
      this.showErrorModal = false;
      this.showSuccessModal = false;
      this.errorMessage = '';
      this.successMessage = '';
      this.collections = [];
      this.wordToAdd = '';
      this.showAddWordSection = false;
    }

    showError(message: string) {
      this.errorMessage = message;
      this.showErrorModal = true;
    }

    showSuccess(message: string){
      this.successMessage = message;
      this.showSuccessModal = true;
    }

    closeErrorModal() {
      this.showErrorModal = false;
    }

    closeSuccessModal(){
      this.showSuccessModal = false;
    }

    changeSplit(){
      if(this.showPinyin){
        this.showPinyin = false;
      }
      this.showTextSplitIntoWords = !this.showTextSplitIntoWords;
      this.init();
    }

    changePinyin(){
      this.showTextSplitIntoWords = true; // Aseguramos que el texto se muestre desglosado por palabras
      this.showOriginalText = true; // Aseguramos que el texto original se muestre
      this.showPinyin = !this.showPinyin;
      this.init();
    }

    changeTranslation(){
      if(this.showPinyin){
        this.showPinyin = false;
      }
      this.showOriginalText = !this.showOriginalText;
      this.init();
    }

    back(){this.router.navigate(['/'])}
  
}