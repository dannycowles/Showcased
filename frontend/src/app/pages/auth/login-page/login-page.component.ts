import {Component} from '@angular/core';
import {AuthenticationService} from '../../../services/auth.service';
import {LoginDto} from '../../../data/dto/login-dto';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {Router, RouterLink} from '@angular/router';
import {NgClass} from '@angular/common';

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.css',
  imports: [ReactiveFormsModule, NgClass, RouterLink],
  standalone: true,
})
export class LoginPageComponent {
  showPassword: boolean = false;
  errorMessage: string = '';
  loginForm = new FormGroup({
    email: new FormControl('', [Validators.required]),
    password: new FormControl('', [Validators.required]),
  });

  constructor(
    private authService: AuthenticationService,
    public router: Router,
  ) {}

  async onSubmit() {
    try {
      const data = this.loginForm.value as LoginDto;
      const response = await this.authService.loginUser(data);

      if (response.ok) {
        window.location.href = this.authService.returnUrl || '/profile';
      } else if (response.status === 401) {
        this.errorMessage =
          'You have entered an incorrect username or password';
      }
    } catch (error) {
      console.error(error);
    } finally {
      setTimeout(() => (this.errorMessage = ''), 5000);
    }
  }

  toggleShowPassword() {
    this.showPassword = !this.showPassword;
  }
}
