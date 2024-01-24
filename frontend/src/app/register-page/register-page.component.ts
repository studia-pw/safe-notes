import { Component, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { RouterLink } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { AuthServiceService } from '../services/auth-service.service';

@Component({
  selector: 'app-register-page',
  standalone: true,
  imports: [
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    RouterLink,
    ReactiveFormsModule,
  ],
  templateUrl: './register-page.component.html',
  styleUrl: './register-page.component.css',
})
export class RegisterPageComponent implements OnInit {
  registerForm!: FormGroup;

  constructor(
    private authService: AuthServiceService,
    private formBuilder: FormBuilder,
  ) {}

  ngOnInit() {
    this.registerForm = this.formBuilder.group({
      email: [''],
      password: [''],
      passwordConfirmation: [''],
    });
  }

  onRegisterClicked() {
    const email = this.registerForm.get('email')?.value;
    const password = this.registerForm.get('password')?.value;
    const passwordConfirmation = this.registerForm.get(
      'passwordConfirmation',
    )?.value;

    console.log({ email, password, passwordConfirmation });

    this.authService
      .register(email, password, passwordConfirmation)
      .subscribe((r) => console.log(r));
  }
}
