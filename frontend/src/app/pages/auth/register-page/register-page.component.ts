import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from '../../../services/auth.service';
import {RegisterDto} from '../../../data/dto/register-dto';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {UtilsService} from '../../../services/utils.service';

@Component({
  selector: 'app-register-page',
  templateUrl: './register-page.component.html',
  styleUrl: './register-page.component.css',
  standalone: false
})
export class RegisterPageComponent implements OnInit {
  usernameMessage: string = '';
  usernameMessageColor: string = '';
  debouncedCheckUsername: () => void;

  registerForm = new FormGroup({
    email: new FormControl('', Validators.required),
    username: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required),
    recaptcha: new FormControl(null, Validators.required)
  });

  constructor(private authService: AuthenticationService,
              public utilsService: UtilsService) {
    this.debouncedCheckUsername = this.utilsService.debounce(() => this.checkUsernameAvailability());
  };

  ngOnInit() {
    // (() => {
    //   'use strict'
    //
    //   const registerForm = document.getElementById('register-form') as HTMLFormElement;
    //
    //   registerForm.addEventListener('submit', async event => {
    //     if (!registerForm.checkValidity()) {
    //       event.preventDefault()
    //       event.stopPropagation()
    //     } else {
    //       await this.registerValidationSuccess();
    //     }
    //     registerForm.classList.add('was-validated')
    //   }, false)
    // })();
  }

  resolved(captchaResponse: string) {
    console.log(`Resolved captcha with response: ${captchaResponse}`);
  }

  async onSubmit() {
    console.log(this.registerForm.value);
    try {
      const data: RegisterDto = this.registerForm.value as RegisterDto;
      const response = await this.authService.registerUser(data);
    } catch (error) {
      console.error()
    }
  }

  async checkUsernameAvailability() {
    if (this.registerForm.value.username) {

      try {
        const response = await this.authService.checkUsernameAvailability(this.registerForm.value.username);
        if (response) {
          this.usernameMessage = "Taken";
          this.usernameMessageColor = "red";
        } else {
          this.usernameMessage = "Available";
          this.usernameMessageColor = "green";
        }
      } catch (error) {
        console.error(error);
      }
    }
  }
}
