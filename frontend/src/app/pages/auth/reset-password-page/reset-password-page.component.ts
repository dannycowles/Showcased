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

      if (response.ok) {
        console.log("matched!")
      } else {
        console.log(response.text());
      }

    } catch(error) {
      console.error(error);
    }
  }
}
