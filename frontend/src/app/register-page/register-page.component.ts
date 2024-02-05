import { Component, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { Router, RouterLink } from '@angular/router';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { AuthServiceService } from '../services/auth-service.service';
import { NgIf } from '@angular/common';
import { passwordValidator } from '../util/password.validator';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-register-page',
  standalone: true,
  imports: [
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    RouterLink,
    ReactiveFormsModule,
    NgIf,
  ],
  templateUrl: './register-page.component.html',
  styleUrl: './register-page.component.css',
})
export class RegisterPageComponent implements OnInit {
  registerForm!: FormGroup;
  registerError = false;

  constructor(
    private authService: AuthServiceService,
    private formBuilder: FormBuilder,
    private router: Router,
  ) {}

  ngOnInit() {
    this.registerForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, passwordValidator()]],
      passwordConfirmation: [
        '',
        [Validators.required, Validators.minLength(8)],
      ],
    });
  }

  passwordHasError() {
    return !this.registerForm.get('password')?.valid;
  }

  getFirstPasswordError() {
    const error = this.registerForm.get('password')?.errors;
    if (!error) {
      return '';
    }
    if (Object.keys(error)[0] == 'required') {
      return 'Password is required';
    } else return error[Object.keys(error)[0]];
  }

  onRegisterClicked() {
    this.registerError = false;
    const email = this.registerForm.get('email')?.value;
    const password = this.registerForm.get('password')?.value;
    const passwordConfirmation = this.registerForm.get(
      'passwordConfirmation',
    )?.value;

    this.authService.register(email, password, passwordConfirmation).subscribe(
      (response) => {
        this.router.navigate(['/login']);
      },
      (error) => {
        const err = error as HttpErrorResponse;
        if (err.error.startsWith('User with email')) {
          this.registerError = true;
        }
      },
    );
  }
}
