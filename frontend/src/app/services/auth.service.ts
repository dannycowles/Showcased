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
        credentials: 'include',
        body: JSON.stringify(data)
      });

      // If the credentials were invalid we throw an error
      if (response.status === 401) {
        throw new Error("Invalid credentials");
      }
  }

  /**
   * Returns true/false if the user is currently logged in
   */
  async loginStatus(): Promise<boolean> {
    try {
      let response = await fetch(`${this.baseUrl}/login-status`, {
        credentials: 'include'
      });

      let data = await response.json();
      return data["loggedIn"];
    } catch (error) {
      throw error;
    }
  }

  /**
   * Attempts to register a user using given email, username, and password
   * will provide feedback if username or email is taken
   * @param data
   */
  async registerUser(data: {}) {
    let response = await fetch(`${this.baseUrl}/register`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(data)
    });

    // If there is an email/username conflict return the error
    if (response.status === 409) {
      let text = await response.json();
      throw new Error(text["error"]);
    }
  }

  /**
   * Checks if the provided username is taken
   * @param username
   */
  async checkUsernameAvailability(username: string): Promise<boolean> {
    let response = await fetch(`${this.baseUrl}/check-username/${username}`);

    let text = await response.json();
    return text["taken"];
  }

}

