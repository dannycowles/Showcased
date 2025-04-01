import {Injectable} from "@angular/core";
import {ProfileData} from '../data/profile-data';

@Injectable({
  providedIn: 'root'
})
export class ProfileService {
  baseUrl: string = "http://localhost:8080/profile";

  /**
   * Retrieves all profile information including:
   * - username
   * - profile picture
   * - watchlist top
   * - watching top
   * - show ranking top
   * - episode ranking top
   */
  async getProfileDetails(): Promise<ProfileData> {
    try {
      let response = await fetch(`${this.baseUrl}/details`, {
        credentials: 'include'
      });

      // If the user is unauthorized, we redirect them to the login page
      if (response.status === 401) {
        window.location.href = '/login';
      }

      let data = await response.json();
      return new ProfileData(data);
    } catch (error) {
      throw error;
    }
  }

  /**
   * Adds a show to the logged-in user's watchlist
   * @param data
   */
  async addShowToWatchlist(data: {}): Promise<Response> {
    try {
      let response = await fetch(`${this.baseUrl}/watchlist`, {
        method: 'POST',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
      });

      // If the user is unauthorized, we redirect them to the login page
      if (response.status === 401) {
        window.location.href = '/login';
      }
      return response;
    } catch(error) {
      throw error;
    }
  }

  /**
   * Adds a show to the logged-in user's currently watching list
   * @param data
   */
  async addShowToWatchingList(data: {}): Promise<Response> {
    try {
      let response = await fetch(`${this.baseUrl}/watching`, {
        method: 'POST',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
      });

      // If the user is unauthorized, we redirect them to the login page
      if (response.status === 401) {
        window.location.href = '/login';
      }
      return response;
    } catch(error) {
      throw error;
    }
  }

  /**
   * Adds a show to the logged-in user's ranking list
   * @param data
   */
  async addShowToRankingList(data: {}): Promise<Response> {
    try {
      let response = await fetch(`${this.baseUrl}/show-ranking`, {
        method: 'POST',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
      });

      // If the user is unauthorized, we redirect them to the login page
      if (response.status === 401) {
        window.location.href = '/login';
      }
      return response;
    } catch(error) {
      throw error;
    }
  }

  /**
   * Adds an episode to the logged-in user's ranking list
   * @param data
   */
  async addEpisodeToRankingList(data: {}): Promise<Response> {
    try {
      let response = await fetch(`${this.baseUrl}/episode-ranking`, {
        method: 'POST',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
      });

      // If the user is unauthorized, we redirect them to the login page
      if (response.status === 401) {
        window.location.href = '/login';
      }
      return response;
    } catch(error) {
      throw error;
    }
  }
}
