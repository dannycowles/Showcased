import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from './services/auth.service';
import {NgOptimizedImage} from '@angular/common';
import {Router, RouterLink, RouterOutlet} from '@angular/router';
import {AngularToastifyModule} from 'angular-toastify';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  imports: [
    NgOptimizedImage,
    RouterLink,
    RouterOutlet,
    AngularToastifyModule,
    FormsModule,
  ],
  standalone: true,
})
export class AppComponent implements OnInit {
  readonly isLoggedIn: boolean;
  searchQuery: string = '';
  profilePicture: string | null = null;
  username: string | null = null;

  constructor(private authService: AuthenticationService, private router: Router) {
    this.isLoggedIn = this.authService.isLoggedIn();
  }

  ngOnInit() {
    this.profilePicture = localStorage.getItem('profilePicture');
    this.username = localStorage.getItem('username');
  }

  logoutUser() {
    this.authService.logout();
  }

  navigateToSearch() {
    // Navigate as long as query is not empty and contains at least one character
    if (this.searchQuery.trim()) {
      this.router.navigate(['/search'], {
        queryParams: { query: this.searchQuery }
      });
      this.searchQuery = '';

      // Remove focus from navbar input
      const navInput = document.getElementById('search-input') as HTMLInputElement;
      if (navInput) {
        navInput.blur();
      }
    }
  }

  closeMenu(): void {
    const navbarCollapse = document.querySelector('.navbar-collapse');
    navbarCollapse?.classList.remove('show');
  }
}
