import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Collection } from "../model/Collection";

const BASE_URL="/api/flashcards/"
@Injectable({
    providedIn:'root'
})

export class FlashcardService{
    constructor(private httpClient:HttpClient){}

    getCollections() : Observable<Collection[]>{
        return this.httpClient.get<Collection[]>(`${BASE_URL}`);
    }
    
}