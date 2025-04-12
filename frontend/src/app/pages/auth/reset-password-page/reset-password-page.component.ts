import {Component} from '@angular/core';
import {AuthenticationService} from '../../../services/auth.service';
import {FormControl, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-reset-password-page',
  templateUrl: './reset-password-page.component.html',
  styleUrl: './reset-password-page.component.css',
  standalone: false
})
export class ResetPasswordPageComponent{
  formNumber: number = 1;
  email: string;
  otp: string = "";
  passwordForm: FormGroup = new FormGroup({
    password: new FormControl('', Validators.required)
  });

  get password() {
    return this.passwordForm.get('password');
  }

  constructor(private authService: AuthenticationService) {};

  async onSubmitPasswordForm() {
    // Check to see if new password was valid aka not blank
    if (this.passwordForm.invalid) {
      this.passwordForm.markAllAsTouched();
      return;
    }

    let data: {} = {
      email: this.email,
      newPassword: this.password.value
    };
    console.log("data", data);
    await this.authService.changePassword(data);
  }

  async submitEmail() {
    if (this.email) {
      // Call backend to generate OTP and send email
      try {
        await this.authService.generateOTP(this.email);
      } catch(error) {
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
      let data = {
        email: this.email,
        otp: this.otp
      };
      let response: Response = await this.authService.validateOTP(data);

      // If successful, update page to new password section, else display error message
      if (response.ok) {
        this.formNumber = 3;
      } else {
        let text = await response.json();
        document.getElementById("otp-error-message").innerText = text["error"];
      }
    } catch(error) {
      console.error(error);
    }
  }
}
