import {Injectable} from "@angular/core";
import {ProfileData} from '../data/profile-data';
import {ShowRankingData} from '../data/lists/show-ranking-data';
import {EpisodeRankingData} from '../data/lists/episode-ranking-data';
import {UserSearchData} from '../data/user-search-data';
import {SeasonRankingData} from '../data/lists/season-ranking-data';
import {CharacterRankingsData} from '../data/character-rankings-data';
import {CollectionData} from '../data/collection-data';
import {SingleCollectionData} from '../data/single-collection-data';
import {ShowListData} from '../data/lists/show-list-data';

@Injectable({
  providedIn: 'root'
})
export class ProfileService {
  readonly baseUrl: string = "http://localhost:8080/profile";

  // If the user is unauthorized, we redirect them to the login page
  checkUnauthorizedUser(response: Response): void {
    if (response.status === 401) {
      window.location.href = '/login';
    }
  }

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
      const response = await fetch(`${this.baseUrl}/details`, {
        credentials: 'include'
      });

      this.checkUnauthorizedUser(response);
      const data = await response.json();
      return new ProfileData(data);
    } catch (error) {
      throw error;
    }
  }

  /**
   * Updates the profile information, currently it's just for bio updates
   * @param data
   */
  async updateProfileDetails(data: {}): Promise<Response> {
    try {
      const response = await fetch(`${this.baseUrl}/details`, {
        method: 'PATCH',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
      });

      this.checkUnauthorizedUser(response);
      return response;
    } catch (error) {
      throw error;
    }
  }

  /**
   * Adds a new social account, if user already has a social account for
   * the given social page it will overwrite it
   * @param data
   */
  async addSocialAccount(data: {}): Promise<Response> {
    try {
      const response = await fetch(`${this.baseUrl}/socials`, {
        method: 'POST',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
      });

      this.checkUnauthorizedUser(response);
      return response;
    } catch (error) {
      throw error;
    }
  }

  /**
   * Removes a social account by the id type of the social page
   * @param socialId
   */
  async removeSocialAccount(socialId: number): Promise<Response> {
    try {
      const response = await fetch(`${this.baseUrl}/socials/${socialId}`, {
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
   * Uploads a profile picture for the logged-in user
   * @param formData
   */
  async uploadProfilePicture(formData: FormData): Promise<string> {
    try {
      const response = await fetch(`${this.baseUrl}/profile-picture`, {
        method: 'POST',
        credentials: 'include',
        body: formData
      });

      this.checkUnauthorizedUser(response);
      return await response.text();
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
      const response = await fetch(`${this.baseUrl}/watchlist`, {
        method: 'POST',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
      });

      this.checkUnauthorizedUser(response);
      return response;
    } catch (error) {
      throw error;
    }
  }

  /**
   * Retrieves the full watchlist for the profile
   */
  async getFullWatchlist(): Promise<ShowListData[]> {
    try {
      const response = await fetch(`${this.baseUrl}/watchlist`, {
        credentials: 'include'
      });

      this.checkUnauthorizedUser(response);
      const data = await response.json();
      return data.map((show: {}) => {
        return new ShowListData(show);
      });
    } catch (error) {
      throw error;
    }
  }

  /**
   * Removes a show from profile watchlist by ID
   * @param id
   */
  async removeShowFromWatchlist(id: number): Promise<Response> {
    try {
      const response = await fetch(`${this.baseUrl}/watchlist/${id}`, {
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
   * Moves a show from watchlist to watching list by ID
   * @param id
   */
  async moveShowToWatchingList(id: number): Promise<Response> {
    try {
      const response = await fetch(`${this.baseUrl}/watchlist/${id}`, {
        method: 'PUT',
        credentials: 'include'
      });

      this.checkUnauthorizedUser(response);
      return response;
    } catch (error) {
      throw error;
    }
  }

  /**
   * Adds a show to the logged-in user's currently watching list
   * @param data
   */
  async addShowToWatchingList(data: {}): Promise<Response> {
    try {
      const response = await fetch(`${this.baseUrl}/currently-watching`, {
        method: 'POST',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
      });

      this.checkUnauthorizedUser(response);
      return response;
    } catch (error) {
      throw error;
    }
  }

  /**
   * Retrieves the full watching list for the profile
   */
  async getFullWatchingList(): Promise<ShowListData[]> {
    try {
      const response = await fetch(`${this.baseUrl}/currently-watching`, {
        credentials: 'include'
      });

      this.checkUnauthorizedUser(response);
      const data = await response.json();
      return data.map((show: {}) => {
        return new ShowListData(show);
      });
    } catch (error) {
      throw error;
    }
  }

  /**
   * Removes a show from watching list by ID
   * @param id
   */
  async removeShowFromWatchingList(id: number): Promise<Response> {
    try {
      const response = await fetch(`${this.baseUrl}/currently-watching/${id}`, {
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
   * Moves a show from currently watching to ranking list by ID
   * @param id
   */
  async moveShowToRankingList(id: number): Promise<Response> {
    try {
      const response = await fetch(`${this.baseUrl}/currently-watching/${id}`, {
        method: 'PUT',
        credentials: 'include'
      });

      this.checkUnauthorizedUser(response);
      return response;
    } catch (error) {
      throw error;
    }
  }

  /**
   * Adds a show to the logged-in user's ranking list
   * @param data
   */
  async addShowToRankingList(data: {}): Promise<Response> {
    try {
      const response = await fetch(`${this.baseUrl}/show-rankings`, {
        method: 'POST',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
      });

      this.checkUnauthorizedUser(response);
      return response;
    } catch (error) {
      throw error;
    }
  }

  /**
   * Retrieves the full show ranking list for the profile
   */
  async getFullShowRankingList(): Promise<ShowRankingData[]> {
    try {
      const response = await fetch(`${this.baseUrl}/show-rankings`, {
        credentials: 'include'
      });

      this.checkUnauthorizedUser(response);
      const data = await response.json();
      return data.map((show: {}) => {
        return new ShowRankingData(show);
      });
    } catch (error) {
      throw error;
    }
  }

  /**
   * Removes a show from ranking list by ID
   * @param id
   */
  async removeShowFromRankingList(id: number): Promise<Response> {
    try {
      const response = await fetch(`${this.baseUrl}/show-rankings/${id}`, {
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
   * Updates the show ranking list, in particular it updates the rank numbers of each show
   * @param data
   */
  async updateShowRankingList(data: {}): Promise<Response> {
    try {
      const response = await fetch(`${this.baseUrl}/show-rankings`, {
        method: 'PATCH',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
      });

      this.checkUnauthorizedUser(response);
      return response;
    } catch (error) {
      throw error;
    }
  }

  /**
   * Adds an episode to the logged-in user's ranking list
   * @param data
   */
  async addEpisodeToRankingList(data: {}): Promise<Response> {
    try {
      const response = await fetch(`${this.baseUrl}/episode-rankings`, {
        method: 'POST',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
      });

      this.checkUnauthorizedUser(response);
      return response;
    } catch (error) {
      throw error;
    }
  }

  /**
   * Retrieves the full episode ranking list for the profile
   */
  async getFullEpisodeRankingList(): Promise<EpisodeRankingData[]> {
    try {
      const response = await fetch(`${this.baseUrl}/episode-rankings`, {
        credentials: 'include'
      });

      this.checkUnauthorizedUser(response);
      const data = await response.json();
      return data.map((episode: {}) => {
        return new EpisodeRankingData(episode);
      });
    } catch (error) {
      throw error;
    }
  }

  /**
   * Removes an episode from ranking list by ID
   * @param id
   */
  async removeEpisodeFromRankingList(id: number): Promise<Response> {
    try {
      const response = await fetch(`${this.baseUrl}/episode-rankings/${id}`, {
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
   * Updates the episode ranking list, in particular it updates the rank numbers of each episode
   * @param data
   */
  async updateEpisodeRankingList(data: {}): Promise<Response> {
    try {
      const response = await fetch(`${this.baseUrl}/episode-rankings`, {
        method: 'PATCH',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
      });

      this.checkUnauthorizedUser(response);
      return response;
    } catch (error) {
      throw error;
    }
  }

  /**
   * Adds a season to the logged-in user's ranking list
   * @param data
   */
  async addSeasonToRankingList(data: {}): Promise<Response> {
    try {
      const response = await fetch(`${this.baseUrl}/season-rankings`, {
        method: 'POST',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
      });

      this.checkUnauthorizedUser(response);
      return response;
    } catch (error) {
      throw error;
    }
  }

  /**
   * Removes season from the logged-in user's ranking list by ID
   * @param id
   */
  async removeSeasonFromRankingList(id: number): Promise<Response> {
    try {
      const response = await fetch(`${this.baseUrl}/season-rankings/${id}`, {
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
   * Retrieves the full season ranking list for the logged-in user
   */
  async getSeasonRankingList(): Promise<SeasonRankingData[]> {
    try {
      const response = await fetch(`${this.baseUrl}/season-rankings`, {
        credentials: 'include'
      });

      this.checkUnauthorizedUser(response);
      const data = await response.json();
      return data.map((season: {}) => {
        return new SeasonRankingData(season);
      });
    } catch (error) {
      throw error;
    }
  }

  /**
   * Retrieves a list of all the users currently following the logged-in user
   * @param name
   */
  async getFollowersList(name?: string): Promise<UserSearchData[]> {
    const params = (name?.length > 0) ? `?name=${encodeURIComponent(name)}` : '';
    const url = `${this.baseUrl}/followers${params}`;

    try {
      const response = await fetch(url, {
        credentials: 'include'
      });

      this.checkUnauthorizedUser(response);
      const data = await response.json();
      return data.map((user: {}) => {
        return new UserSearchData(user);
      });
    } catch (error) {
      throw error;
    }
  }

  /**
   * Updates the season ranking list for the logged-in user, in particular the rank nums
   * @param data
   */
  async updateSeasonRankingList(data: {}): Promise<Response> {
    try {
      const response = await fetch(`${this.baseUrl}/season-rankings`, {
        method: 'PATCH',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
      });

      this.checkUnauthorizedUser(response);
      return response;
    } catch (error) {
      throw error;
    }
  }

  /**
   * Retrieves a list of all the users the logged-in user is following
   * @param name
   */
  async getFollowingList(name?: string): Promise<UserSearchData[]> {
    const params = (name?.length > 0) ? `?name=${encodeURIComponent(name)}` : '';
    const url = `${this.baseUrl}/following${params}`;

    try {
      const response = await fetch(url, {
        credentials: 'include'
      });

      this.checkUnauthorizedUser(response);
      const data = await response.json();
      return data.map((user: {}) => {
        return new UserSearchData(user);
      });
    } catch (error) {
      throw error;
    }
  }

  /**
   * Retrieves all the character ranking lists for the logged-in user
   */
  async getCharacterRankingLists() {
    try {
      const response = await fetch(`${this.baseUrl}/character-rankings`, {
        credentials: 'include'
      });

      this.checkUnauthorizedUser(response);
      const data = await response.json();
      return new CharacterRankingsData(data);
    } catch (error) {
      throw error;
    }
  }

  /**
   * Adds a character to a character ranking list for the logged-in user
   * @param data
   */
  async addCharacterToRankingList(data: {}): Promise<Response> {
    try {
      const response = await fetch(`${this.baseUrl}/character-rankings`, {
        method: 'POST',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
      });

      this.checkUnauthorizedUser(response);
      return response;
    } catch(error) {
      throw error;
    }
  }

  /**
   * Remove a character by name from a specific character type list for the logged-in user
   */
  async removeCharacterFromRankingList(type: string, name: string): Promise<Response> {
    try {
      const response = await fetch(`${this.baseUrl}/character-rankings/${type}/${name}`, {
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
   * Updates a character ranking list for the logged-in user
   */
  async updateCharacterRankingList(data: {}): Promise<Response> {
    try {
      const response = await fetch(`${this.baseUrl}/character-rankings`, {
        method: 'PATCH',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
      });

      this.checkUnauthorizedUser(response);
      return response;
    } catch (error) {
      throw error;
    }
  }

  /**
   * Removes the specified id from the logged-in user's following list
   * @param id
   */
  async removeFollower(id: number): Promise<Response> {
    try {
      const response: Response = await fetch(`${this.baseUrl}/followers/${id}`, {
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
   * Retrieves all collections for the logged-in user
   */
  async getCollections(name?: string): Promise<CollectionData[]> {
    const params = (name?.length > 0) ? `?name=${encodeURIComponent(name)}` : '';
    const url = `${this.baseUrl}/collections${params}`;

    try {
      const response = await fetch(url, {
        credentials: 'include'
      });

      this.checkUnauthorizedUser(response);
      const data = await response.json();
      return data.map((collection: {}) => {
        return new CollectionData(collection);
      });
    } catch (error) {
      throw error;
    }
  }

  /**
   * Creates a new collection for the logged-in user
   * @param data
   */
  async createCollection(data: {}): Promise<Response> {
    try {
      const response = await fetch(`${this.baseUrl}/collections`, {
        method: 'POST',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
      });

      this.checkUnauthorizedUser(response);
      return response;
    } catch (error) {
      throw error;
    }
  }

  /**
   * Deletes a collection for the logged-in user by ID
   * @param id
   */
  async deleteCollection(id: number): Promise<Response> {
    try {
      const response = await fetch(`${this.baseUrl}/collections/${id}`, {
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
   * Updates a collection for the logged-in user by ID
   * @param id
   * @param data
   */
  async updateCollection(id: number, data: {}): Promise<Response> {
    try {
      const response = await fetch(`${this.baseUrl}/collections/${id}`, {
        method: 'PATCH',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
      });

      this.checkUnauthorizedUser(response);
      return response;
    } catch (error) {
      throw error;
    }
  }

  /**
   * Adds a show to a collection for the logged-in user by ID
   * @param id
   * @param data
   */
  async addShowToCollection(id: number, data: {}): Promise<Response> {
    try {
      const response = await fetch(`${this.baseUrl}/collections/${id}/shows`, {
        method: 'POST',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
      });

      this.checkUnauthorizedUser(response);
      return response;
    } catch (error) {
      throw error;
    }
  }

  /**
   * Retrieves shows in a collection for the logged-in user by ID
   * @param id
   */
  async getCollectionDetails(id: number): Promise<SingleCollectionData> {
    try {
      const response = await fetch(`${this.baseUrl}/collections/${id}`, {
        credentials: 'include'
      });

      this.checkUnauthorizedUser(response);
      const data = await response.json();
      return new SingleCollectionData(data);
    } catch (error) {
      throw error;
    }
  }

  /**
   * Removes a show from a collection for the logged-in user by ID
   * @param collectionId
   * @param showId
   */
  async removeShowFromCollection(collectionId: number, showId: number): Promise<Response> {
    try {
      const response = await fetch(`${this.baseUrl}/collections/${collectionId}/shows/${showId}`, {
        method: 'DELETE',
        credentials: 'include'
      });

      this.checkUnauthorizedUser(response);
      return response;
    } catch (error) {
      throw error;
    }
  }
}
