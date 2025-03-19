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
  usernameTimer: ReturnType<typeof setTimeout>;

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
      document.getElementById('register-error-message').removeAttribute("hidden");
    }
  }

  checkUsernameAvailability() {
    clearTimeout(this.usernameTimer);
    this.usernameTimer = setTimeout(async () => {
      // @ts-ignore
      let username = $("#register-form").serializeJSON()["username"];
      if (username) {
        let response = await this.authService.checkUsernameAvailability(username);

        let usernameMessage = document.getElementById('username-taken-message');
        usernameMessage.removeAttribute("hidden");
        if (response) {
          usernameMessage.innerText = "Taken";
          usernameMessage.style.color = "red";
        } else {
          usernameMessage.innerText = "Available";
          usernameMessage.style.color = "green";
        }
      }
    }, 500);
  }

}
