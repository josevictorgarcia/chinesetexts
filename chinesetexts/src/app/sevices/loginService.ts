import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BehaviorSubject } from "rxjs";
import { User } from "../model/user";

const BASE_URL="/api/auth/"
@Injectable({
    providedIn:'root'
})

export class LoginService{

    private loggedInSubject = new BehaviorSubject<boolean>(false);
    public loggedIn$ = this.loggedInSubject.asObservable();

    public user?: User;

    constructor(private http: HttpClient) {
      this.reqIsLogged();
    }

    public login(user: string, pass: string) {
      return this.http.post(BASE_URL + "/login",{ username: user, password: pass },{ withCredentials: true }).pipe(); // sigue siendo un observable, el componente debe suscribirse
    }

    public logout() {
      return this.http.post(BASE_URL + "/logout", { withCredentials: true }).subscribe(
        () => {
          console.log("LOGOUT: Successfully");
          this.loggedInSubject.next(false);
          this.user = undefined;
        }
      );
    }

    public reqIsLogged() {
    this.http.get("/api/users/me", { withCredentials: true }).subscribe(
      (response) => {
        this.user = response as User;
        this.loggedInSubject.next(true);
      },
      (error) => {
        if (error.status !== 401) {
          console.error("Error when asking if logged: " + JSON.stringify(error));
        }
        this.loggedInSubject.next(false);
        }
      );
    }

    public currentUser() {
      return this.user;
    }

    public isLogged() {
      return this.loggedInSubject.getValue();
    }

    public isRoleUser(): boolean {
      return this.currentUser()?.roles.includes("USER") ?? false;
    }

    public isRoleAdmin(): boolean {
      return this.currentUser()?.roles.includes("ADMIN") ?? false;
    }

}