import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from '../../../services/auth.service';

@Component({
  selector: 'app-reset-password-page',
  templateUrl: './reset-password-page.component.html',
  styleUrl: './reset-password-page.component.css',
  standalone: false
})
export class ResetPasswordPageComponent implements OnInit {
  formNumber: number = 1;
  email: string;
  otp: string = "";

  constructor(private authService: AuthenticationService) {};

  ngOnInit() {};

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
