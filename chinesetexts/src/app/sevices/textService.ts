import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Text } from "../model/text"
import { Word } from "../model/word";

const BASE_URL="/api/texts/"
@Injectable({
    providedIn:'root'
})

export class TextService{
    constructor(private httpClient:HttpClient){}

    getTexts() : Observable<Text[]>{
      return this.httpClient.get<Text[]>(`${BASE_URL}`);
    }

    getText(id: number): Observable<Text> {
      return this.httpClient.get<Text>(`${BASE_URL}${id}`);
    }

    postText(text: Text): Observable<Text> {
      return this.httpClient.post<Text>(`${BASE_URL}`, text);
    }

    createTextImage(id: number, file: File): Observable<any> {
      const formData = new FormData();
      formData.append('imageFile', file);
      return this.httpClient.post(`${BASE_URL}/${id}/image`, formData); //MANTENIMIENTO
    }

    getPendingWords(text: string): Observable<any> {
      const params = new HttpParams().set('text', text);
      return this.httpClient.get<any>(`${BASE_URL}/pendingWords`, { params });
    }
    
    getSpanishText(id: number): Observable<any>{
      return this.httpClient.get<any>(`${BASE_URL}${id}/SpanishText`);
    }

    saveWords(words: Word[]): Observable<any>{
      return this.httpClient.post<any>(`${BASE_URL}/createdWords`, words);
    }

    /*getEnglishText(id: number): Observable<any>{
      return this.httpClient.get<any>(`${BASE_URL}${id}/EnglishText`);
    }
      
    getTranslation(id: number): Observable<any>{
      return this.httpClient.get<any>(`${BASE_URL}${id}/translation`);
    }

    postWord(word: Word): Observable<Word>{
      return this.httpClient.post<Word>(`${BASE_URL}/words`, word);
    }*/
}