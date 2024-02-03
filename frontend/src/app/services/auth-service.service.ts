import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';

export type User = {
  id: number;
  email: string;
};
export type LoggedInUser = User | null;

@Injectable({
  providedIn: 'root',
})
export class AuthServiceService {
  baseUrl = 'http://localhost:8080/api/auth';
  private user: BehaviorSubject<LoggedInUser> =
    new BehaviorSubject<LoggedInUser>(null);

  constructor(private http: HttpClient) {}

  public getUserAsObservable(): Observable<LoggedInUser> {
    return this.user.asObservable();
  }

  public getUser(): LoggedInUser {
    return this.user.getValue();
  }

  public setUser(user: LoggedInUser) {
    this.user.next(user);
  }

  login(email: string, password: string) {
    const formData = new FormData();
    formData.append('email', email);
    formData.append(`password`, password);

    let options = {
      withCredentials: true,
    };
    return this.http.post(`${this.baseUrl}/login`, formData, options);
  }

  register(email: string, password: string, passwordConfirmation: string) {
    return this.http.post(`${this.baseUrl}/register`, {
      email,
      password,
      passwordConfirmation,
    });
  }

  logout() {
    this.user.next(null);
    return this.http.post(
      `${this.baseUrl}/logout`,
      {},
      { withCredentials: true },
    );
  }
}
