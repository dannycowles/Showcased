import { Component } from '@angular/core';
import {AuthenticationService} from './services/auth.service';
import {NgOptimizedImage} from '@angular/common';
import {RouterLink, RouterOutlet} from '@angular/router';
import {AngularToastifyModule} from 'angular-toastify';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  imports: [NgOptimizedImage, RouterLink, RouterOutlet, AngularToastifyModule],
  standalone: true,
})
export class AppComponent {
  readonly isLoggedIn: boolean;

  constructor(private authService: AuthenticationService) {
    this.isLoggedIn = this.authService.isLoggedIn();
  }

  logoutUser() {
    this.authService.logout();
  }

  protected readonly localStorage = localStorage;
}
