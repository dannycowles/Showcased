import {CanActivateFn, RedirectCommand, Router} from '@angular/router';
import {AuthenticationService} from '../services/auth.service';
import {inject} from '@angular/core';

export const authGuard: CanActivateFn = (route, state): RedirectCommand | boolean => {
  const router = inject(Router);
  const authService = inject(AuthenticationService);

  const username = localStorage.getItem("username");
  if (!username) {
    const loginPath = router.parseUrl("/login");

    // Store the attempted URL
    authService.returnUrl = state.url;
    console.log("set return url: ", state.url);
    return new RedirectCommand(loginPath);
  }

  return true;
};
