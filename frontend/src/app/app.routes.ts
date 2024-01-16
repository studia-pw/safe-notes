import { Routes } from '@angular/router';
import { LoginPageComponent } from './login-page/login-page.component';
import { RegisterPageComponent } from './register-page/register-page.component';
import { NotesPageComponent } from './notes-page/notes-page.component';

export const routes: Routes = [
  { path: 'home', component: NotesPageComponent },
  { path: 'login', component: LoginPageComponent },
  { path: 'register', component: RegisterPageComponent },
  { path: '', redirectTo: '/home', pathMatch: 'full' },
];
