import {Component} from '@angular/core';
import {AuthenticationService} from '../../../services/auth.service';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  FormsModule, ReactiveFormsModule,
  ValidationErrors,
  ValidatorFn,
  Validators
} from '@angular/forms';
import {ValidateOtpDto} from '../../../data/dto/validate-otp-dto';
import {ResetPasswordDto} from '../../../data/dto/reset-password-dto';
import {NgOtpInputComponent} from 'ng-otp-input';
import {NgClass, NgOptimizedImage} from '@angular/common';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-reset-password-page',
  templateUrl: './reset-password-page.component.html',
  styleUrl: './reset-password-page.component.css',
  imports: [
    FormsModule,
    NgOtpInputComponent,
    ReactiveFormsModule,
    NgClass,
    NgOptimizedImage,
    RouterLink,
  ],
  standalone: true,
})
export class ResetPasswordPageComponent {
  readonly passwordMinLength: number = 8;
  formNumber: number = 1;
  invalidOtpMessage: string = '';
  otp: string = '';

  emailForm: FormControl = new FormControl('', [
    Validators.required,
    Validators.email,
  ]);

  passwordForm: FormGroup = new FormGroup(
    {
      password: new FormControl('', [
        Validators.required,
        Validators.minLength(this.passwordMinLength),
      ]),
      confirmPassword: new FormControl('', Validators.required),
    },
    { validators: this.passwordsMatchingValidator() },
  );

  showPassword: boolean = false;

  constructor(private authService: AuthenticationService) {}

  passwordsMatchingValidator(): ValidatorFn {
    return (
      control: AbstractControl<string, string>,
    ): ValidationErrors | null => {
      const password: string = control.get('password').value;
      const confirmPassword: string = control.get('confirmPassword').value;

      return password !== confirmPassword ? { passwordsDontMatch: true } : null;
    };
  }

  get password(): AbstractControl<string, string> {
    return this.passwordForm.get('password');
  }

  get confirmPassword(): AbstractControl<string, string> {
    return this.passwordForm.get('confirmPassword');
  }

  async onSubmitPasswordForm() {
    // Check to see if new password was valid aka not blank
    if (this.passwordForm.invalid) {
      this.passwordForm.markAllAsTouched();
      return;
    }

    const data: ResetPasswordDto = {
      email: this.emailForm.value,
      newPassword: this.password.value,
    };
    const response = await this.authService.changePassword(data);

    if (response.ok) {
      this.formNumber = 4;
    }
  }

  async submitEmail() {
    if (this.emailForm.value) {
      // Call backend to generate OTP and send email
      try {
        await this.authService.generateOTP(this.emailForm.value);
      } catch (error) {
        console.error(error);
      }
      this.formNumber = 2;
    }
  }

  onOtpChange(otp: string) {
    this.otp = otp;
  }

  async verifyCode() {
    try {
      const data: ValidateOtpDto = {
        email: this.emailForm.value,
        otp: this.otp,
      };
      await this.authService.validateOTP(data);
      this.formNumber = 3;
    } catch (error: any) {
      this.invalidOtpMessage = error.detail;
    } finally {
      setTimeout(() => (this.invalidOtpMessage = ''), 5000);
    }
  }

  toggleShowPassword() {
    this.showPassword = !this.showPassword;
  }
}
