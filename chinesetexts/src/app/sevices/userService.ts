import { HttpClient, HttpParams } from "@angular/common/http";
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

    getUser(email: string): Observable<UserWithPassword>{
        const params = new HttpParams().set('email', email);
        return this.httpClient.get<UserWithPassword>(`${BASE_URL}`, { params });
    }

    putUser(user: UserWithPassword): Observable<UserWithPassword>{
        const email = encodeURIComponent(user.email);
        return this.httpClient.put<UserWithPassword>(`${BASE_URL}${email}`, user);
    }
}