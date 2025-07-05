import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Word } from "../model/word";

const BASE_URL="/api/words/"
@Injectable({
    providedIn:'root'
})

export class WordService{
    constructor(private httpClient:HttpClient){}

    getPendingWords(text: string): Observable<any> {
      const params = new HttpParams().set('text', text);
      return this.httpClient.get<any>(`${BASE_URL}/pendingWords`, { params });
    }

    saveWords(words: Word[]): Observable<any>{
      return this.httpClient.post<any>(`${BASE_URL}/createdWords`, words);
    }

    getTextWords(originalText: string[]): Observable<Word[]> {
      // Convertimos el array originalText a una cadena separada por comas
      const params = new HttpParams().set('text', originalText.join(','));
      return this.httpClient.get<Word[]>(`${BASE_URL}textWords`, { params });
    }

}