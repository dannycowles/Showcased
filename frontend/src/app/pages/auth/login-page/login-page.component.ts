import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from '../../../services/auth.service';
import $ from 'jquery';
import 'jquery-serializejson';
import {LoginDto} from '../../../data/dto/login-dto';

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.css',
  standalone: false
})
export class LoginPageComponent implements OnInit {

  constructor(private authService: AuthenticationService) { }

  ngOnInit() {
    (() => {
      'use strict'

      const loginForm = document.getElementById('login-form') as HTMLFormElement;

      loginForm.addEventListener('submit', async event => {
        if (!loginForm.checkValidity()) {
          event.preventDefault()
          event.stopPropagation()
        } else {
          await this.loginValidationSuccess();
        }
        loginForm.classList.add('was-validated')
      }, false)
    })();
  }

  async loginValidationSuccess() {
    try {
      // @ts-ignore
      const data: LoginDto = $('#login-form').serializeJSON();
      await this.authService.loginUser(data);

      // If the login attempt was successful route the user back to the page they were previously on
      window.history.back();
    } catch (error) {
      // If there was a login error, we show the error message
      // @ts-ignore
      document.getElementById("login-error-message").removeAttribute("hidden");
    }
  }
}
