import { Component, OnInit, inject } from '@angular/core';
import { LayoutService } from 'src/app/layout/service/app.layout.service';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

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
      console.log(this.username.invalid);
      console.log(this.username.dirty);
      console.log(this.username.touched);

      console.log('Form is not valid');
    }
  }

  private initFormNew() {
    this.registrationForm = this.formBuilder.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      rememberme: [false],
    });
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
