import { Component, OnInit, inject } from '@angular/core';
import { LayoutService } from 'src/app/layout/service/app.layout.service';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { AbstractControl, FormBuilder, FormGroup, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { catchError, map, Observable, of } from 'rxjs';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent implements OnInit {
  layoutService = inject(LayoutService);
  authService = inject(AuthService);
  router = inject(Router);
  formBuilder = inject(FormBuilder);
  registrationForm!: FormGroup;

  // Add displayTerms property to control the visibility of terms and conditions
  displayTerms = false;

  ngOnInit(): void {
    this.initFormNew();
  }

  register() {
    if (this.registrationForm.valid) {
      this.authService.register(this.registrationForm.value).subscribe({
        next: (data: any) => {
          this.router.navigate(['/auth/login']);
        },
        error: error => {
          console.error(error);
        },
      });
    } else {
      console.log(this.username?.invalid);
      console.log(this.username?.dirty);
      console.log(this.username?.touched);
      console.log('Form is not valid');
    }
  }

  private initFormNew() {
    this.registrationForm = this.formBuilder.group(
      {
        fullName: ['', [Validators.required, Validators.minLength(3), Validators.pattern('^[A-Za-z ]+$')]],
        username: ['', [Validators.required, Validators.minLength(3), Validators.pattern('^[a-z0-9_]+$')], this.usernameValidator.bind(this)],
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
        number: [
          '',
          [
            Validators.required,
            Validators.pattern('^[0-9]*$'),
            Validators.min(1000000000),
            Validators.max(9999999999)
          ]
        ],
        rememberme: [false],
      },
      {
        validators: this.matchPasswords('password', 'confirmPassword'),
      }
    );
  }

  // Method to format the username input
  transformUsername(event: Event) {
    const input = event.target as HTMLInputElement;
    const transformed = input.value.toLowerCase();
    this.registrationForm.get('username')?.setValue(transformed, { emitEvent: false });
  }

  // Method to show terms and conditions
  showTermsAndConditions() {
    this.displayTerms = true; // Show the terms and conditions when this method is called
  }

  closeDialog() {
    this.displayTerms = false;
  }

  private matchPasswords(passwordKey: string, confirmPasswordKey: string): ValidatorFn {
    return (form: AbstractControl): ValidationErrors | null => {
      const password = form.get(passwordKey);
      const confirmPassword = form.get(confirmPasswordKey);

      if (!password || !confirmPassword) {
        return null;
      }

      return password.value !== confirmPassword.value ? { passwordMismatch: true } : null;
    };
  }

  private usernameValidator(control: AbstractControl): Observable<ValidationErrors | null> {
    return this.checkUsername(control.value).pipe(
      map((isTaken) => (isTaken ? { usernameTaken: true } : null)),
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
