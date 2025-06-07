import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { User } from "../model/user";
import { Observable } from "rxjs";
import { UserWithPassword } from "../model/userWithPassword";

const BASE_URL="/api/users/"
@Injectable({
    providedIn:'root'
})

export class UserService{

    constructor(private httpClient:HttpClient){}
    
    postUser(user: UserWithPassword): Observable<User> {
        return this.httpClient.post<User>(`${BASE_URL}`, user);
    }
}