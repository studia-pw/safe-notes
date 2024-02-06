import { Routes } from '@angular/router';
import { LoginPageComponent } from './login-page/login-page.component';
import { RegisterPageComponent } from './register-page/register-page.component';
import { NotesPageComponent } from './notes-page/notes-page.component';
import { RegisterQrPageComponent } from './register-qr-page/register-qr-page.component';

export const routes: Routes = [
  { path: 'home', component: NotesPageComponent },
  { path: 'login', component: LoginPageComponent },
  { path: 'register', component: RegisterPageComponent },
  { path: '2fa', component: RegisterQrPageComponent },
  { path: '', redirectTo: '/home', pathMatch: 'full' },
];
