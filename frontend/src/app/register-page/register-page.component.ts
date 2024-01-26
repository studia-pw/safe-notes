import { Component, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { Router, RouterLink } from '@angular/router';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  ValidationErrors,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { AuthServiceService } from '../services/auth-service.service';
import { NgIf } from '@angular/common';

export function passwordValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const password: string = control.value;

    if (!password) {
      return null;
    }

    const validationErrors: ValidationErrors = {};

    const hasEightCharacters =
      password.length >= 8 ||
      (validationErrors['hasEightCharacters'] = { value: control.value });
    const hasUpperCase =
      /[A-Z]/.test(password) ||
      (validationErrors['hasUpperCase'] = { value: control.value });
    const hasLowerCase =
      /[a-z]/.test(password) ||
      (validationErrors['hasLowerCase'] = { value: control.value });
    const hasDigit =
      /\d/.test(password) ||
      (validationErrors['hasDigit'] = { value: control.value });
    const hasSpecialChar = /[!@#$%^&*(),.?":{}|<>]/.test(password);

    console.log(validationErrors);
    const isValid =
      hasEightCharacters &&
      hasUpperCase &&
      hasLowerCase &&
      hasDigit &&
      hasSpecialChar;

    return !isValid ? validationErrors : null;
  };
}

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

  constructor(
    private authService: AuthServiceService,
    private formBuilder: FormBuilder,
    private router: Router,
  ) {}

  ngOnInit() {
    this.registerForm = this.formBuilder.group({
      email: ['', Validators.email],
      password: [
        '',
        [Validators.required, Validators.minLength(8), passwordValidator()],
      ],
      passwordConfirmation: [
        '',
        [Validators.required, Validators.minLength(8)],
      ],
    });
  }

  onRegisterClicked() {
    const email = this.registerForm.get('email')?.value;
    const password = this.registerForm.get('password')?.value;
    const passwordConfirmation = this.registerForm.get(
      'passwordConfirmation',
    )?.value;

    console.log({ email, password, passwordConfirmation });
    console.log(this.registerForm);

    this.authService.register(email, password, passwordConfirmation).subscribe(
      (response) => {
        this.router.navigate(['/login']);
      },
      (error) => {
        console.log(error);
      },
    );
  }
}
