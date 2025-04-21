import {Injectable} from '@angular/core';
import {ReviewData} from '../data/review-data';
import {SearchResultData} from '../data/search-result-data';
import {UtilsService} from './utils.service';
import {ShowData} from '../data/show/show-data';
import {SeasonData} from '../data/show/season-data';
import {EpisodeData} from '../data/show/episode-data';

@Injectable({
  providedIn: 'root'
})
export class ShowService {
  baseUrl: string = "http://localhost:8080/show";

  /**
   * Fetches search results for a user query
    * @param searchString
   */
  async searchForShows(searchString: string): Promise<SearchResultData[]> {
    try {
      let response = await fetch(`${this.baseUrl}/search?query=${encodeURIComponent(searchString)}`);

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
      let response = await fetch(`${this.baseUrl}/${showId}`, {
        credentials: 'include'
      });

      // If the show was not found then redirect the user to the 404 page
      if (response.status === 500) {
        window.location.href = "/not-found";
      }

      let data = await response.json();
      return new ShowData(data);
    } catch (error) {
      throw error;
    }
  }

  /**
   * Fetches the number of seasons by show ID
   * @param showId
   */
  async fetchNumberOfSeasons(showId: number): Promise<number> {
    try {
      let response = await fetch(`${this.baseUrl}/${showId}/num-seasons`);

      let data = await response.json();
      return data['number_of_seasons'];
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
      let response = await fetch(`${this.baseUrl}/${showId}/reviews`, {
        credentials: 'include'
      });

      let data = await response.json();
      return data.map((review: {}) => {
        return new ReviewData(review, new UtilsService());
      });
    } catch (error) {
      throw error;
    }
  }

  /**
   * Adds a review to a show
   * @param showId
   * @param data
   */
  async addShowReview(showId:number, data: {}) {
    try {
      let response = await fetch(`${this.baseUrl}/${showId}/reviews`, {
        method: 'POST',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
      });

      // If the user is unauthorized, we redirect them to the login page
      if (response.status === 401) {
        window.location.href = "/login";
      }
    } catch(error) {
      throw error;
    }
  }

  /**
   * Likes the show review by its ID
   * @param reviewId
   */
  async likeShowReview(reviewId: number) {
    try {
      let response = await fetch(`${this.baseUrl}/${reviewId}/like`, {
        method: 'PATCH',
        credentials: 'include'
      });

      // If the user is unauthorized, we redirect them to the login page
      if (response.status === 401) {
        window.location.href = "/login";
      }
    } catch(error) {
      throw error;
    }
  }

  /**
   * Unlikes the show review by its ID
   * @param reviewId
   */
  async unlikeShowReview(reviewId: number) {
    try {
      let response = await fetch(`${this.baseUrl}/${reviewId}/unlike`, {
        method: 'PATCH',
        credentials: 'include'
      });

      // If the user is unauthorized, we redirect them to the login page
      if (response.status === 401) {
        window.location.href = "/login";
      }
    } catch(error) {
      throw error;
    }
  }

  /**
   * Fetches the season details for a show given its ID and season number
   * @param showId
   * @param seasonNumber
   */
  async fetchSeasonDetails(showId: number, seasonNumber: number): Promise<SeasonData> {
    try {
      let response = await fetch(`${this.baseUrl}/${showId}/season/${seasonNumber}`, {
        credentials: 'include'
      });

      // If the season was not found then redirect the user to the 404 page
      if (response.status === 500) {
        window.location.href = "/not-found";
      }

      let data = await response.json();
      return new SeasonData(data);
    } catch (error) {
      throw error;
    }
  }

  /**
   * Fetches the details for an episode given the show's ID, season, and episode number
   * @param showId
   * @param seasonNumber
   * @param episodeNumber
   */
  async fetchEpisodeDetails(showId: number, seasonNumber: number, episodeNumber: number): Promise<EpisodeData> {
    try {
      let response = await fetch(`${this.baseUrl}/${showId}/season/${seasonNumber}/episode/${episodeNumber}`);

      // If the episode was not found then redirect the user to the 404 page
      if (response.status === 500) {
        window.location.href = "/not-found";
      }

      let data = await response.json();
      return new EpisodeData(data);
    } catch (error) {
      throw error;
    }
  }

}
