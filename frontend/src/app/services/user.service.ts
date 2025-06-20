import {Injectable} from '@angular/core';
import {UserSearchData} from '../data/user-search-data';
import {ProfileData} from '../data/profile-data';
import {ShowRankingData} from '../data/lists/show-ranking-data';
import {EpisodeRankingData} from '../data/lists/episode-ranking-data';
import {SeasonRankingData} from '../data/lists/season-ranking-data';
import {CharacterRankingsData} from '../data/character-rankings-data';
import {CollectionData} from '../data/collection-data';
import {SingleCollectionData} from '../data/single-collection-data';
import {ShowListData} from '../data/lists/show-list-data';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  readonly baseUrl: string = "http://localhost:8080/users";

  // If the user is unauthorized, we redirect them to the login page
  checkUnauthorizedUser(response: Response): void {
    if (response.status === 401) {
      window.location.href = '/login';
    }
  }

  /**
   * Searches for users given a query
   * @param query
   */
  async searchUsers(query: string): Promise<UserSearchData[]> {
    try {
      const response = await fetch(`${this.baseUrl}/search?query=${encodeURIComponent(query)}`);

      const data = await response.json();
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
      const response = await fetch(`${this.baseUrl}/${id}/details`, {
        credentials: 'include'
      });

      const data= await response.json();
      return new ProfileData(data);
    } catch(error) {
      throw error;
    }
  }

  /**
   * Retrieves the full watchlist for the user with the specified id
   * @param id
   */
  async getFullWatchlist(id: number): Promise<ShowListData[]> {
    try {
      const response = await fetch(`${this.baseUrl}/${id}/watchlist`);

      const data = await response.json();
      return data.map((show: {}) => {
        return new ShowListData(show);
      });
    } catch(error) {
      throw error;
    }
  }

  /**
   * Retrieves the full watching list for the user with the specified id
   * @param id
   */
  async getFullWatchingList(id:number): Promise<ShowListData[]> {
    try {
      const response = await fetch(`${this.baseUrl}/${id}/currently-watching`);

      const data = await response.json();
      return data.map((show: {}) => {
        return new ShowListData(show);
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
      const response = await fetch(`${this.baseUrl}/${id}/show-rankings`);

      const data = await response.json();
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
      const response = await fetch(`${this.baseUrl}/${id}/episode-rankings`);

      const data = await response.json();
      return data.map((show: {}) => {
        return new EpisodeRankingData(show);
      });
    } catch(error) {
      throw error;
    }
  }

  /**
   * Retrieves the full season ranking list for the user with the specified id
   * @param id
   */
  async getFullSeasonRankingList(id:number): Promise<SeasonRankingData[]> {
    try {
      const response = await fetch(`${this.baseUrl}/${id}/season-rankings`);

      const data = await response.json();
      return data.map((season: {}) => {
        return new SeasonRankingData(season);
      });
    } catch(error) {
      throw error;
    }
  }

  /**
   * Retrieves all the character ranking lists for the user with the specified id
   * @param id
   */
  async getAllCharacterRankingLists(id: number): Promise<CharacterRankingsData> {
    try {
      const response = await fetch(`${this.baseUrl}/${id}/character-rankings`);

      const data = await response.json();
      return new CharacterRankingsData(data);
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
      const response = await fetch(`${this.baseUrl}/${id}/followers`, {
        method: 'DELETE',
        credentials: 'include'
      });

      this.checkUnauthorizedUser(response);
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
      const response = await fetch(`${this.baseUrl}/${id}/followers`, {
        method: 'POST',
        credentials: 'include'
      });

      this.checkUnauthorizedUser(response);
      return response;
    } catch (error) {
      throw error;
    }
  }

  /**
   * Retrieves the followers list for user with the specified id
   * @param id
   * @param name
   */
  async getFollowersList(id: number, name ?: string): Promise<UserSearchData[]> {
    const params = (name?.length > 0) ? `?name=${encodeURIComponent(name)}` : '';
    const url = `${this.baseUrl}/${id}/followers${params}`;

    try {
      const response = await fetch(url, {
        credentials: 'include'
      });

      const data = await response.json();
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
   * @param name
   */
  async getFollowingList(id: number, name ?: string): Promise<UserSearchData[]> {
    const params = (name?.length > 0) ? `?name=${encodeURIComponent(name)}` : '';
    const url = `${this.baseUrl}/${id}/following${params}`;

    try {
      const response = await fetch(url, {
        credentials: 'include'
      });

      const data = await response.json();
      return data.map((user: {}) => {
        return new UserSearchData(user);
      });
    } catch(error) {
      throw error;
    }
  }

  /**
   * Retrieves the public collections for user with the specified id
   * @param name
   * @param id
   */
  async getPublicCollections(id: number, name ?: string): Promise<CollectionData[]> {
    const params = (name?.length > 0) ? `?name=${encodeURIComponent(name)}` : '';
    const url = `${this.baseUrl}/${id}/collections${params}`;

    try {
      const response = await fetch(url);

      const data = await response.json();
      return data.map((collection: {}) => {
        return new CollectionData(collection);
      });
    } catch (error) {
      throw error;
    }
  }

  /**
   * Retrieves the details for a collection with the specified id
   * @param collectionId
   */
  async getCollectionDetails(collectionId: number): Promise<SingleCollectionData> {
    try {
      const response = await fetch(`${this.baseUrl}/collections/${collectionId}`, {
        credentials: 'include'
      });

      const data = await response.json();
      return new SingleCollectionData(data);
    } catch (error) {
      throw error;
    }
  }

  /**
   * Likes a user's collection by the specified id
   * @param collectionId
   */
  async likeCollection(collectionId: number): Promise<Response> {
    try {
      return await fetch(`${this.baseUrl}/collections/${collectionId}/likes`, {
        method: 'POST',
        credentials: 'include'
      });
    } catch (error) {
      throw error;
    }
  }

  /**
   * Unlikes a user's collection by the specified id
   * @param collectionId
   */
  async unlikeCollection(collectionId: number): Promise<Response> {
    try {
      return await fetch(`${this.baseUrl}/collections/${collectionId}/likes`, {
        method: 'DELETE',
        credentials: 'include'
      });
    } catch (error) {
      throw error;
    }
  }
}
