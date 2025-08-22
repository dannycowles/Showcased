import { Component } from '@angular/core';
import {AuthenticationService} from './services/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  standalone: false
})
export class AppComponent {
  readonly username: string;
  readonly profilePicture: string;

  constructor(private authService: AuthenticationService) {
    this.username = localStorage.getItem("username");
    this.profilePicture = localStorage.getItem("profilePicture");
  }

  logoutUser() {
    this.authService.logout();
  }
}
