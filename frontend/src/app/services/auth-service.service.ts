import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class AuthServiceService {
  baseUrl = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) {}

  login(email: string, password: string) {
    let body = new URLSearchParams();
    body.set('email', email);
    body.set('password', password);
    let header = new HttpHeaders({
      'Content-Type': 'application/x-www-form-urlencoded',
    });

    let options = {
      headers: header,
      withCredentials: true,
    };
    return this.http.post(`${this.baseUrl}/login`, body, options);
  }

  register(email: string, password: string, passwordConfirmation: string) {
    return this.http.post(`${this.baseUrl}/register`, {
      email,
      password,
      passwordConfirmation,
    });
  }
}
