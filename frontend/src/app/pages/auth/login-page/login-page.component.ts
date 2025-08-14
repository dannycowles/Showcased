import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from '../../../services/auth.service';
import {LoginDto} from '../../../data/dto/login-dto';
import {FormControl, FormGroup} from '@angular/forms';
import {log} from '@angular-devkit/build-angular/src/builders/ssr-dev-server';

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.css',
  standalone: false
})
export class LoginPageComponent implements OnInit {
  loginForm = new FormGroup({
    email: new FormControl(''),
    password: new FormControl('')
  });

  constructor(private authService: AuthenticationService) { }

  ngOnInit() {
  }

  protected readonly log = log;
}
