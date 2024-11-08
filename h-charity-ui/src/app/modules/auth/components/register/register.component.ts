import { Component, OnInit, inject } from '@angular/core';
import { LayoutService } from 'src/app/layout/service/app.layout.service';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  ValidationErrors,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { catchError, map, Observable, of } from 'rxjs';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent implements OnInit {
  layoutService = inject(LayoutService);
  authService = inject(AuthService);
  router = inject(Router);
  messageService = inject(MessageService);

  formBuilder = inject(FormBuilder);
  registrationForm!: FormGroup;

  ngOnInit(): void {
    this.initFormNew();
  }

  register() {
    if (this.registrationForm.valid) {
      this.authService.register(this.registrationForm.value).subscribe({
        next: (data: any) => {
          this.messageService.add({
            severity: 'success',
            summary: 'User Registered',
            detail: 'User Registered successfully',
          });
          this.router.navigate(['/auth/login']);
        },
        error: error => {
          this.messageService.add({
            severity: 'error',
            summary: 'User Registeration Error',
            detail: error,
          });
        },
      });
    } else {
      console.log('Form is not valid');
    }
  }

  private initFormNew() {
    this.registrationForm = this.formBuilder.group(
      {
        fullname: [
          '',
          [
            Validators.required,
            Validators.minLength(3),
            Validators.pattern('^[A-Za-z ]+$'),
          ],
        ],
        username: [
          '',
          [
            Validators.required,
            Validators.minLength(3),
            Validators.pattern('^[A-Za-z]+$'),
          ],
          this.usernameValidator.bind(this),
        ],
        email: ['', [Validators.required, Validators.email]],
        password: [
          '',
          [
            Validators.required,
            Validators.minLength(6),
            Validators.pattern('^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*]).+$'),
          ],
        ],
        confirmPassword: ['', Validators.required],
        mobile: ['', [Validators.required, Validators.pattern('^[0-9]{10}$')]],
        rememberme: [false],
      },
      {
        validators: this.matchPasswords('password', 'confirmPassword'),
      }
    );
  }

  private matchPasswords(
    passwordKey: string,
    confirmPasswordKey: string
  ): ValidatorFn {
    return (form: AbstractControl): ValidationErrors | null => {
      const password = form.get(passwordKey);
      const confirmPassword = form.get(confirmPasswordKey);

      if (!password || !confirmPassword) {
        return null;
      }

      return password.value !== confirmPassword.value
        ? { passwordMismatch: true }
        : null;
    };
  }

  private usernameValidator(
    control: AbstractControl
  ): Observable<ValidationErrors | null> {
    return this.checkUsername(control.value).pipe(
      map(isTaken => (isTaken ? { usernameTaken: true } : null)),
      catchError(() => of(null))
    );
  }

  private checkUsername(username: string): Observable<boolean> {
    const existingUsernames = ['user1', 'user2', 'admin'];
    return of(existingUsernames.includes(username));
  }

  get username() {
    return this.registrationForm.get('username');
  }
  get email() {
    return this.registrationForm.get('email');
  }
  get password() {
    return this.registrationForm.get('password');
  }
}
