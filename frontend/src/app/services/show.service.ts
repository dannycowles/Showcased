import {ShowData} from '../data/show-data';
import {Injectable} from '@angular/core';
import {ReviewData} from '../data/review-data';
import {SearchResultData} from '../data/search-result-data';
import {SeasonData} from '../data/season-data';
import {EpisodeData} from '../data/episode-data';
import {UtilsService} from './utils.service';

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
      let response = await fetch(`${this.baseUrl}/${showId}`);

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
      let response = await fetch(`${this.baseUrl}/${showId}/reviews`);

      let data = await response.json();
      return data.map((review: {}) => {
        return new ReviewData(review, new UtilsService());
      });
    } catch (error) {
      throw error;
    }
  }

  /**
   * Likes the show review by its ID
   * @param reviewId
   */
  async likeShowReview(reviewId: number) {
    try {
      let response = await fetch(`${this.baseUrl}/${reviewId}/like`);

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
      let response = await fetch(`${this.baseUrl}/${showId}/season/${seasonNumber}`);

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

      let data = await response.json();
      return new EpisodeData(data);
    } catch (error) {
      throw error;
    }
  }

}
