import {Component} from '@angular/core';
import {AuthenticationService} from '../../../services/auth.service';
import {RegisterDto} from '../../../data/dto/register-dto';
import {
  AbstractControl,
  AsyncValidatorFn,
  FormControl,
  FormGroup, ReactiveFormsModule,
  ValidationErrors,
  ValidatorFn,
  Validators
} from '@angular/forms';
import {UtilsService} from '../../../services/utils.service';
import {map, Observable, of, switchMap, timer} from 'rxjs';
import {NgClass} from '@angular/common';

@Component({
  selector: 'app-register-page',
  templateUrl: './register-page.component.html',
  styleUrl: './register-page.component.css',
  imports: [ReactiveFormsModule, NgClass],
  standalone: true,
})
export class RegisterPageComponent {
  usernameMessage: string = '';
  usernameMessageColor: string = '';
  registerMessage: string = '';
  registerMessageColor: string = '';
  readonly usernameMinLength: number = 3;
  readonly passwordMinLength: number = 8;

  showPassword: boolean = false;
  showConfirmPassword: boolean = false;

  registerForm = new FormGroup(
    {
      email: new FormControl('', [Validators.required, Validators.email]),
      username: new FormControl(
        '',
        [Validators.required, Validators.minLength(this.usernameMinLength)],
        [this.usernameAvailableValidator()],
      ),
      password: new FormControl('', [
        Validators.required,
        Validators.minLength(this.passwordMinLength),
      ]),
      confirmPassword: new FormControl('', Validators.required),
      recaptcha: new FormControl(null, Validators.required),
    },
    { validators: this.passwordsMatchingValidator() },
  );

  constructor(
    private authService: AuthenticationService,
    public utilsService: UtilsService,
  ) {}

  get email(): AbstractControl<string, string> {
    return this.registerForm.get('email');
  }

  get username(): AbstractControl<string, string> {
    return this.registerForm.get('username');
  }

  get password(): AbstractControl<string, string> {
    return this.registerForm.get('password');
  }

  get confirmPassword(): AbstractControl<string, string> {
    return this.registerForm.get('confirmPassword');
  }

  passwordsMatchingValidator(): ValidatorFn {
    return (
      control: AbstractControl<string, string>,
    ): ValidationErrors | null => {
      const password: string = control.get('password').value;
      const confirmPassword: string = control.get('confirmPassword').value;

      return password !== confirmPassword ? { passwordsDontMatch: true } : null;
    };
  }

  usernameAvailableValidator(): AsyncValidatorFn {
    return (
      control: AbstractControl<string, string>,
    ): Observable<ValidationErrors | null> => {
      const username = control.value.trim();

      if (username.length < this.usernameMinLength) {
        return of(null);
      }

      return timer(300).pipe(
        switchMap(() => this.authService.checkUsernameAvailability(username)),
        map((taken) => {
          if (taken) {
            this.usernameMessage = 'Username is taken';
            this.usernameMessageColor = 'red';
            return { usernameTaken: true };
          } else {
            this.usernameMessage = 'Username is available';
            this.usernameMessageColor = 'green';
            return null;
          }
        }),
      );
    };
  }

  async onSubmit() {
    try {
      const data: RegisterDto = this.registerForm.value as RegisterDto;
      const response = await this.authService.registerUser(data);

      if (response.ok) {
        window.location.href = this.authService.returnUrl || '/profile';
      }
      if (!response.ok) {
        this.registerMessage = (await response.json()).error;
        this.registerMessageColor = 'red';
      }
    } catch (error) {
      console.error();
    } finally {
      setTimeout(() => (this.registerMessage = ''), 5000);
    }
  }

  toggleShowPassword() {
    this.showPassword = !this.showPassword;
  }

  toggleShowConfirmPassword() {
    this.showConfirmPassword = !this.showConfirmPassword;
  }
}
