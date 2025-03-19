import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from '../../services/auth.service';
import $ from 'jquery';
import 'jquery-serializejson';

@Component({
  selector: 'app-register-page',
  templateUrl: './register-page.component.html',
  styleUrl: './register-page.component.css',
  standalone: false
})
export class RegisterPageComponent implements OnInit {

  constructor(private authService: AuthenticationService) {};

  ngOnInit() {
    (() => {
      'use strict'

      const registerForm = document.getElementById('register-form') as HTMLFormElement;

      registerForm.addEventListener('submit', async event => {
        if (!registerForm.checkValidity()) {
          event.preventDefault()
          event.stopPropagation()
        } else {
          await this.registerValidationSuccess();
        }
        registerForm.classList.add('was-validated')
      }, false)
    })();
  }

  async registerValidationSuccess() {
    try {
      // @ts-ignore
      let data = $("#register-form").serializeJSON();
      await this.authService.registerUser(data);


    } catch(error) {
      // Update the error message with the provided message
      // @ts-ignore
      document.getElementById('register-error-message').innerText = error.message;
      // @ts-ignore
      document.getElementById('register-error-message').removeAttribute("hidden");
    }
  }

}
