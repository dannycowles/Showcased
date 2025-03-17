import {ShowData} from '../data/show-data';
import {Injectable} from '@angular/core';
import {ReviewData} from '../data/review-data';
import {SearchResultData} from '../data/search-result-data';

@Injectable({
  providedIn: 'root'
})
export class ShowService {

  /**
   * Fetches search results for a user query
    * @param searchString
   */
  async searchForShows(searchString: string): Promise<SearchResultData[]> {
    try {
      let response = await fetch(`http://localhost:8080/show/search?query=${encodeURIComponent(searchString)}`);

      let data = await response.json();
      return data.map((result: {}) =>  {
        return new SearchResultData(result);
      });
    } catch(error) {
      throw error;
    }
  }

  /**
   * Fetches the show details by ID
   * @param showId
   */
  async fetchShowDetails(showId: number): Promise<ShowData> {
    try {
      let response = await fetch(`http://localhost:8080/show/${showId}`);

      let data = await response.json();
      return new ShowData(data);
    } catch (error) {
      throw error;
    }
  }

  /**
   * Fetches the reviews for a show by its ID
   * @param showId
   */
  async fetchShowReviews(showId: number): Promise<ReviewData[]> {
    try {
      let response = await fetch(`http://localhost:8080/show/${showId}/reviews`);

      let data = await response.json();
      return data.map((review: {}) => {
        return new ReviewData(review);
      });
    } catch (error) {
      throw error;
    }
  }

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
