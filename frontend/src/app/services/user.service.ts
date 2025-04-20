import {Injectable} from '@angular/core';
import {UserSearchData} from '../data/user-search-data';
import {ProfileData} from '../data/profile-data';
import {WatchlistData} from '../data/lists/watchlist-data';
import {WatchingData} from '../data/lists/watching-data';
import {ShowRankingData} from '../data/lists/show-ranking-data';
import {EpisodeRankingData} from '../data/lists/episode-ranking-data';

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
      let response: Response = await fetch(`${this.baseUrl}/${id}/details`, {
        credentials: 'include'
      });
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

  /**
   * Retrieves the full show ranking list for the user with the specified id
   * @param id
   */
  async getFullShowRankingList(id:number): Promise<ShowRankingData[]> {
    try {
      let response: Response = await fetch(`${this.baseUrl}/${id}/show-ranking`);
      let data: [] = await response.json();

      return data.map((show: {}) => {
        return new ShowRankingData(show);
      });
    } catch(error) {
      throw error;
    }
  }

  /**
   * Retrieves the full episode ranking list for the user with the specified id
   * @param id
   */
  async getFullEpisodeRankingList(id:number): Promise<EpisodeRankingData[]> {
    try {
      let response: Response = await fetch(`${this.baseUrl}/${id}/episode-ranking`);
      let data: [] = await response.json();

      return data.map((show: {}) => {
        return new EpisodeRankingData(show);
      });
    } catch(error) {
      throw error;
    }
  }

  /**
   * Unfollows the user with the specified id
   * @param id
   */
  async unfollowUser(id: number): Promise<Response> {
    try {
      const response: Response = await fetch(`${this.baseUrl}/${id}/unfollow`, {
        method: 'DELETE',
        credentials: 'include'
      });

      // Redirect to login page if unauthorized
      if (response.status == 401) {
        window.location.href = "/login";
      }
      return response;
    } catch (error) {
      throw error;
    }
  }

  /**
   * Follows the user with the specified id
   * @param id
   */
  async followUser(id: number): Promise<Response> {
    try {
      const response: Response = await fetch(`${this.baseUrl}/${id}/follow`, {
        method: 'POST',
        credentials: 'include'
      });

      // Redirect to login page if unauthorized
      if (response.status == 401) {
        window.location.href = "/login";
      }
      return response;
    } catch (error) {
      throw error;
    }
  }

  /**
   * Removes the specified id from the logged-in user's following list
   * @param id
   */
  async removeFollower(id: number) {
    try {
      let response: Response = await fetch(`${this.baseUrl}/followers/${id}`, {
        method: 'DELETE',
        credentials: 'include'
      });
    } catch (error) {
      throw error;
    }
  }

  /**
   * Retrieves the followers list for user with the specified id
   * @param id
   */
  async getFollowersList(id: number): Promise<UserSearchData[]> {
    try {
      let response: Response = await fetch(`${this.baseUrl}/${id}/followers`);
      let data: [] = await response.json();
      return data.map((user: {}) => {
        return new UserSearchData(user);
      });
    } catch(error) {
      throw error;
    }
  }

  /**
   * Retrieves the following list for user with the specified id
   * @param id
   */
  async getFollowingList(id: number): Promise<UserSearchData[]> {
    try {
      let response: Response = await fetch(`${this.baseUrl}/${id}/following`);
      let data: [] = await response.json();
      return data.map((user: {}) => {
        return new UserSearchData(user);
      });
    } catch(error) {
      throw error;
    }
  }

}
