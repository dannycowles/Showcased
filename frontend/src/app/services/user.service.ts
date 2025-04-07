import {Injectable} from '@angular/core';
import {UserSearchData} from '../data/user-search-data';
import {ProfileData} from '../data/profile-data';
import {WatchlistData} from '../data/watchlist-data';
import {WatchingData} from '../data/watching-data';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  baseUrl: string = "http://localhost:8080/user";

  /**
   * Searches for users given a query
   * @param query
   */
  async searchUsers(query: string): Promise<UserSearchData[]> {
    try {
      let response: Response = await fetch(`${this.baseUrl}/search?query=${encodeURIComponent(query)}`);
      let data: [] = await response.json();

      return data.map((result: {}) => {
        return new UserSearchData(result);
      });
    } catch(error) {
      throw error;
    }
  }

  /**
   * Retrieves the user details for the given user id
   * @param id
   */
  async getUserDetails(id: number): Promise<ProfileData> {
    try {
      let response: Response = await fetch(`${this.baseUrl}/${id}/details`);
      let data: {} = await response.json();
      return new ProfileData(data);
    } catch(error) {
      throw error;
    }
  }

  /**
   * Retrieves the full watchlist for the user with the specified id
   * @param id
   */
  async getFullWatchlist(id: number): Promise<WatchlistData[]> {
    try {
      let response: Response = await fetch(`${this.baseUrl}/${id}/watchlist`);
      let data: [] = await response.json();

      return data.map((show: {}) => {
        return new WatchlistData(show);
      });
    } catch(error) {
      throw error;
    }
  }

  /**
   * Retrieves the full watching list for the user with the specified id
   * @param id
   */
  async getFullWatchingList(id:number): Promise<WatchingData[]> {
    try {
      let response: Response = await fetch(`${this.baseUrl}/${id}/watching`);
      let data: [] = await response.json();

      return data.map((show: {}) => {
        return new WatchingData(show);
      });
    } catch(error) {
      throw error;
    }
  }
}
