import {Injectable} from "@angular/core";
import {ProfileData} from '../data/profile-data';
import {WatchlistData} from '../data/lists/watchlist-data';
import {WatchingData} from '../data/lists/watching-data';
import {ShowRankingData} from '../data/lists/show-ranking-data';
import {EpisodeRankingData} from '../data/lists/episode-ranking-data';
import {UserSearchData} from '../data/user-search-data';
import {SeasonRankingData} from '../data/lists/season-ranking-data';

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

  async uploadProfilePicture(formData: FormData): Promise<string> {
    try {
      let response = await fetch(`${this.baseUrl}/profile-picture`, {
        method: 'POST',
        credentials: 'include',
        body: formData
      });
      return await response.text();
    } catch(error) {
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
   * Retrieves the full watchlist for the profile
   */
  async getFullWatchlist(): Promise<WatchlistData[]> {
    try {
      let response = await fetch(`${this.baseUrl}/watchlist`, {
        credentials: 'include'
      });

      // If the user is unauthorized, we redirect them to the login page
      if (response.status === 401) {
        window.location.href = '/login';
      }

      let data = await response.json();

      return data.map((show: {}) => {
        return new WatchlistData(show);
      })
    } catch(error) {
      throw error;
    }
  }

  /**
   * Removes a show from profile watchlist by ID
   * @param id
   */
  async removeShowFromWatchlist(id: number) {
    try {
      let response = await fetch(`${this.baseUrl}/watchlist/${id}`, {
        method: 'DELETE',
        credentials: 'include'
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
   * Moves a show from watchlist to watching list by ID
   * @param id
   */
  async moveShowToWatchingList(id: number) {
    try {
      let response = await fetch(`${this.baseUrl}/watchlist/${id}`, {
        method: 'PUT',
        credentials: 'include'
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
   * Retrieves the full watching list for the profile
   */
  async getFullWatchingList(): Promise<WatchingData[]> {
    try {
      let response = await fetch(`${this.baseUrl}/watching`, {
        credentials: 'include'
      });

      let data = await response.json();

      // If the user is unauthorized, we redirect them to the login page
      if (response.status === 401) {
        window.location.href = '/login';
      }

      return data.map((show: {}) => {
        return new WatchingData(show);
      })
    } catch(error) {
      throw error;
    }
  }

  /**
   * Removes a show from watching list by ID
   * @param id
   */
  async removeShowFromWatchingList(id: number) {
    try {
      let response = await fetch(`${this.baseUrl}/watching/${id}`, {
        method: 'DELETE',
        credentials: 'include'
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
   * Moves a show from currently watching to ranking list by ID
   * @param id
   */
  async moveShowToRankingList(id: number) {
    try {
      let response = await fetch(`${this.baseUrl}/watching/${id}`, {
        method: 'PUT',
        credentials: 'include'
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
   * Retrieves the full show ranking list for the profile
   */
  async getFullShowRankingList(): Promise<ShowRankingData[]> {
    try {
      let response = await fetch(`${this.baseUrl}/show-ranking`, {
        credentials: 'include'
      });

      // If the user is unauthorized, we redirect them to the login page
      if (response.status === 401) {
        window.location.href = '/login';
      }

      let data = await response.json();

      return data.map((show: {}) => {
        return new ShowRankingData(show);
      });
    } catch(error) {
      throw error;
    }
  }

  /**
   * Removes a show from ranking list by ID
   * @param id
   */
  async removeShowFromRankingList(id: number) {
    try {
      let response = await fetch(`${this.baseUrl}/show-ranking/${id}`, {
        method: 'DELETE',
        credentials: 'include'
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
   * Updates the show ranking list, in particular it updates the rank numbers of each show
   * @param data
   */
  async updateShowRankingList(data: {}) {
    try {
      let response = await fetch(`${this.baseUrl}/show-ranking`, {
        method: 'PATCH',
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

  /**
   * Retrieves the full episode ranking list for the profile
   */
  async getFullEpisodeRankingList() {
    try {
      let response = await fetch(`${this.baseUrl}/episode-ranking`, {
        credentials: 'include'
      });

      // If the user is unauthorized, we redirect them to the login page
      if (response.status === 401) {
        window.location.href = '/login';
      }

      let data = await response.json();

      return data.map((show: {}) => {
        return new EpisodeRankingData(show);
      })
    } catch(error) {
      throw error;
    }
  }

  /**
   * Removes an episode from ranking list by ID, season #, and episode #
   * @param id
   * @param season
   * @param episode
   */
  async removeEpisodeFromRankingList(id: number, season: number, episode: number) {
    try {
      let response = await fetch(`${this.baseUrl}/episode-ranking/show/${id}/season/${season}/episode/${episode}`, {
        method: 'DELETE',
        credentials: 'include'
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
   * Updates the episode ranking list, in particular it updates the rank numbers of each episode
   * @param data
   */
  async updateEpisodeRankingList(data: {}) {
    try {
      let response = await fetch(`${this.baseUrl}/episode-ranking`, {
        method: 'PATCH',
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
    } catch(error) {
      throw error;
    }
  }

  /**
   * Adds a season to the logged-in user's ranking list
   * @param data
   */
  async addSeasonToRankingList(data: {}): Promise<Response> {
    try {
      let response = await fetch(`${this.baseUrl}/season-rankings`, {
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
   * Removes season from the logged-in user's ranking list by ID
   * @param id
   */
  async removeSeasonFromRankingList(id: number): Promise<Response> {
    try {
      let response = await fetch(`${this.baseUrl}/season-rankings/${id}`, {
        method: 'DELETE',
        credentials: 'include'
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
   * Retrieves the full season ranking list for the logged-in user
   */
  async getSeasonRankingList(): Promise<SeasonRankingData[]> {
    try {
      let response = await fetch(`${this.baseUrl}/season-rankings`, {
        credentials: 'include'
      });

      // If the user is unauthorized, we redirect them to the login page
      if (response.status === 401) {
        window.location.href = '/login';
      }
      return await response.json();
    } catch (error) {
      throw error;
    }
  }

  /**
   * Retrieves a list of all the users currently following the logged-in user
   */
  async getFollowersList(): Promise<UserSearchData[]> {
    try {
      let response = await fetch(`${this.baseUrl}/followers`, {
        credentials: 'include'
      });

      // If the user is unauthorized, we redirect them to the login page
      if (response.status === 401) {
        window.location.href = '/login';
      }

      let data = await response.json();
      return data.map((user: {}) => {
        return new UserSearchData(user);
      });
    }catch(error) {
      throw error;
    }
  }

  /**
   * Updates the season ranking list for the logged-in user, in particular the rank nums
   * @param data
   */
  async updateSeasonRankingList(data: {}) {
    try {
      let response = await fetch(`${this.baseUrl}/season-rankings`, {
        method: 'PATCH',
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
    } catch(error) {
      throw error;
    }
  }

  /**
   * Retrieves a list of all the users the logged-in user is following
   */
  async getFollowingList(): Promise<UserSearchData[]> {
    try {
      let response = await fetch(`${this.baseUrl}/following`, {
        credentials: 'include'
      });

      // If the user is unauthorized, we redirect them to the login page
      if (response.status === 401) {
        window.location.href = '/login';
      }

      let data = await response.json();
      return data.map((user: {}) => {
        return new UserSearchData(user);
      });
    } catch(error) {
      throw error;
    }
  }

}
