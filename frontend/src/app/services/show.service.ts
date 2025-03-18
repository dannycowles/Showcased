import {ShowData} from '../data/show-data';
import {Injectable} from '@angular/core';
import {ReviewData} from '../data/review-data';
import {SearchResultData} from '../data/search-result-data';
import {SeasonData} from '../data/season-data';

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
   * Fetches the number of seasons by show ID
   * @param showId
   */
  async fetchNumberOfSeasons(showId: number): Promise<number> {
    try {
      let response = await fetch(`http://localhost:8080/show/${showId}/num-seasons`);

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
   * Fetches the season details for a show given its ID and season number
   * @param showId
   * @param seasonNumber
   */
  async fetchSeasonDetails(showId: number, seasonNumber: number): Promise<SeasonData> {
    try {
      let response = await fetch(`http://localhost:8080/show/${showId}/season/${seasonNumber}`);

      let data = await response.json();
      return new SeasonData(data);
    } catch (error) {
      throw error;
    }
  }

}
