import {Injectable} from '@angular/core';
import {UserSearchData} from '../data/user-search-data';
import {ProfileData} from '../data/profile-data';

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
}
