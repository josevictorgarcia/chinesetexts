import { Component, OnInit } from '@angular/core';
import { TextService } from '../sevices/textService';
import { Text } from '../model/text';
import { Router } from '@angular/router';
import { LoginService } from '../sevices/loginService';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'show-text',
  templateUrl: './show-text.component.html',
})
export class AppShowText implements OnInit{
  texts: Text[] = [];

  constructor(private textService:TextService, private router: Router, public loginService: LoginService, public translate: TranslateService){}

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

  deleteText(id: number | undefined, event: MouseEvent): void {
    event.stopPropagation(); // Evita que se dispare el routerLink
    console.log('Eliminar texto con id:', id);
    if (id !== undefined) {
      this.textService.deleteText(id).subscribe(
        () => {
          console.log('Texto eliminado con éxito');
          this.loadTexts(); // Recargar los textos después de eliminar
        },
        (error) => console.error('Error al eliminar el texto', error)
      );
    } else {
      console.error('ID del texto no definido');
    }
  }

}
