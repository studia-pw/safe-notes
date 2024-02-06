import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { QrCode } from '../dto/qr-code';

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
  private user: BehaviorSubject<LoggedInUser>;

  constructor(private http: HttpClient) {
    const user = localStorage.getItem('user');
    let loggedInUser: LoggedInUser = null;

    if (user) {
      loggedInUser = JSON.parse(user);
    }

    this.user = new BehaviorSubject<LoggedInUser>(loggedInUser);
  }

  public getUserAsObservable(): Observable<LoggedInUser> {
    return this.user.asObservable();
  }

  public getUser(): LoggedInUser {
    return this.user.getValue();
  }

  public setUser(user: LoggedInUser) {
    this.user.next(user);
    localStorage.setItem('user', JSON.stringify(user));
  }

  login(email: string, password: string, code: string) {
    const formData = new FormData();
    formData.append('email', email);
    formData.append('password', password);
    formData.append('code', code);

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

  get2faQRCode(email: string) {
    return this.http.get<QrCode>(`${this.baseUrl}/2fa/${email}`);
  }

  logout() {
    this.user.next(null);
    localStorage.removeItem('user');
    return this.http.post(
      `${this.baseUrl}/logout`,
      {},
      { withCredentials: true },
    );
  }
}
