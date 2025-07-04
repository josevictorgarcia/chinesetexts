import { Component, OnInit } from '@angular/core';
import { TextService } from '../sevices/textService';
import { Text } from '../model/text';
import { Router } from '@angular/router';
import { LoginService } from '../sevices/loginService';

@Component({
  selector: 'show-text',
  templateUrl: './show-text.component.html',
})
export class AppShowText implements OnInit{
  texts: Text[] = [];

  constructor(private textService:TextService, private router: Router, private loginService: LoginService){}

  ngOnInit(): void {
      this.loginService.reqIsLogged().subscribe(() => {
        this.init(); // o lo que necesites hacer una vez recuperado el usuario
      });
  }

  private init(){
    this.loginService.reqIsLogged();
    this.loadTexts();
  }

  private loadTexts(){
    this.textService.getTexts().subscribe(
      (texts) => this.texts = texts,
      (error) => console.error("Error al cargar los textos", error)
    )
  }

  /*text: Text = {
    title: "",
    text: "",
    translation: ""
  }
  texts: Text[] = [];
  

  getText(id:number | undefined){
    //console.log(this.title);
    //console.log(this.text);
    //console.log(this.translation);

    //this.title = "";
    //this.text = "";
    //this.translation = "";
    if(id != undefined){
      this.textService.getText(id).subscribe(
        (text) => {
          this.text.title = text.title;
          this.text.text = text.text;
          this.text.translation = text.translation;
          console.log(text);
        },
        (error) => console.error("Error al obtener el texto", error)
      );
    }
  }

    postText(){
      this.textService.postText(this.text).subscribe(
        () => {
          this.clearForm();
          this.loadTexts();
        },
        (error) => console.error("Error al crear el texto", error)
      );
    }*/
}
