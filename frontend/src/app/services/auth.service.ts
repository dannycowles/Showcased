import {Injectable} from "@angular/core";
import {LoginDto} from '../data/dto/login-dto';
import {RegisterDto} from '../data/dto/register-dto';
import {ValidateOtpDto} from '../data/dto/validate-otp-dto';
import {ChangePasswordDto} from '../data/dto/change-password-dto';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  baseUrl: string = "http://localhost:8080/auth";

  /**
   * Attempts to log in to an account using given email & password
   * @param data
   */
  async loginUser(data: LoginDto) {
      const response = await fetch(`${this.baseUrl}/login`, {
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
      const response = await fetch(`${this.baseUrl}/login-status`, {
        credentials: 'include'
      });

      const data = await response.json();
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
  async registerUser(data: RegisterDto) {
    const response = await fetch(`${this.baseUrl}/register`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(data)
    });

    // If there is an email/username conflict return the error
    if (response.status === 409) {
      const text = await response.json();
      throw new Error(text["error"]);
    }
  }

  /**
   * Checks if the provided username is taken
   * @param username
   */
  async checkUsernameAvailability(username: string): Promise<boolean> {
    const response = await fetch(`${this.baseUrl}/check-username/${username}`);

    const text = await response.json();
    return text["taken"];
  }

  /**
   * Generates an OTP for password reset
   * @param email
   */
  async generateOTP(email: string) {
    try {
      await fetch(`${this.baseUrl}/request-otp`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: email
      });
    } catch(error) {
      throw error;
    }
  }

  /**
   * Validates the given OTP against the backend DB
   * @param data
   */
  async validateOTP(data: ValidateOtpDto): Promise<Response> {
    try {
      return await fetch(`${this.baseUrl}/validate-otp`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        credentials: 'include',
        body: JSON.stringify(data)
      });
    } catch(error) {
      throw error;
    }
  }

  /**
   * Used to change/reset a user's password
   * @param data
   */
  async changePassword(data: ChangePasswordDto) {
    try {
      const response = await fetch(`${this.baseUrl}/change-password`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        credentials: 'include',
        body: JSON.stringify(data)
      });

      // Upon successful update redirect the user to the login page
      if (response.ok) {
        window.location.href = "/login";
      }

    } catch(error) {
      throw error;
    }
  }

}

