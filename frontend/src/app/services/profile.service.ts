import {Injectable} from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class ProfileService {

  /**
   * Adds a show to the logged-in user's watchlist
   * @param data
   */
  async addShowToWatchlist(data: {}) {
    try {
      let response = await fetch(`http://localhost:8080/profile/watchlist`, {
        method: 'POST',
        body: JSON.stringify(data)
      });

      // If the user is unauthorized, we redirect them to the login page
      if (response.status === 401) {
        window.location.href = '/login';
      }
    } catch(error) {
      throw error;
    }
  }

  /**
   * Adds a show to the logged-in user's currently watching list
   * @param data
   */
  async addShowToWatchingList(data: {}) {
    try {
      let response = await fetch(`http://localhost:8080/profile/watching`, {
        method: 'POST',
        body: JSON.stringify(data)
      });

      // If the user is unauthorized, we redirect them to the login page
      if (response.status === 401) {
        window.location.href = '/login';
      }
    } catch(error) {
      throw error;
    }
  }

  /**
   * Adds a show to the logged-in user's ranking list
   * @param data
   */
  async addShowToRankingList(data: {}) {
    try {
      let response = await fetch(`http://localhost:8080/profile/show-ranking`, {
        method: 'POST',
        body: JSON.stringify(data)
      });

      // If the user is unauthorized, we redirect them to the login page
      if (response.status === 401) {
        window.location.href = '/login';
      }
    } catch(error) {
      throw error;
    }
  }
}
