import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Collection } from "../model/collection";
import { Flashcard } from "../model/flashcard";

const BASE_URL="/api/flashcards/"
@Injectable({
    providedIn:'root'
})

export class FlashcardService{
    constructor(private httpClient:HttpClient){}

    getCollections(email: string) : Observable<Collection[]>{
        const params = new HttpParams().set('email', email);
        return this.httpClient.get<Collection[]>(`${BASE_URL}`, { params });
    }

    getFlashcards(id:number) : Observable<Flashcard[]>{
        return this.httpClient.get<Flashcard[]>(`${BASE_URL}${id}`);
    }
    
    putWordToCollection(collectionId: number, word: string, textId: number): Observable<String>{
        const params = new HttpParams().set('chinese', word).set('textId', textId.toString()); 
        return this.httpClient.put<String>(`${BASE_URL}${collectionId}`, {}, { params, responseType: 'text' as 'json' });
    }

    postCollection(title: string, email: string): Observable<Collection>{
        return this.httpClient.post<Collection>(`${BASE_URL}`, { title: title, email: email });
    }

    deleteFlashcard(id: number): Observable<void> {
        return this.httpClient.delete<void>(`${BASE_URL}flashcard/${id}`);
    }

}