import {Injectable} from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  baseUrl: string = "http://localhost:8080/auth";

  /**
   * Attempts to log in to an account using given email & password
   * @param data
   */
  async loginUser(data: {}) {
      let response = await fetch(`${this.baseUrl}/login`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
      });

      // If the credentials were invalid we throw an error
      if (response.status === 401) {
        throw new Error("Invalid credentials");
      }
    }
  }

