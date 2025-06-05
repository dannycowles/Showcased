import {Injectable} from '@angular/core';
import {ReviewData} from '../data/review-data';
import {UtilsService} from './utils.service';
import {ShowData} from '../data/show/show-data';
import {SeasonData} from '../data/show/season-data';
import {EpisodeData} from '../data/show/episode-data';
import {TrendingShowsData} from '../data/trending-shows-data';
import {ShowGenresData} from '../data/show-genres-data';
import {TopRatedShowsData} from '../data/top-rated-shows-data';
import {ResultPageData} from '../data/show/result-page-data';
import {RoleData} from '../data/role-data';

@Injectable({
  providedIn: 'root'
})
export class ShowService {
  baseUrl: string = "http://localhost:8080/shows";

  // If the user is unauthorized, we redirect user to the login page
  checkUnauthorizedUser(response: Response) {
    if (response.status === 401) {
      window.location.href = "/login";
    }
  }

  // If the show / season / episode is not found, we redirect user to the 404 page
  checkPageNotFound(response: Response) {
    if (response.status === 500) {
      window.location.href = "/not-found";
    }
  }

  /**
   * Fetches search results for a user query
    * @param searchString
   */
  async searchForShows(searchString: string): Promise<TopRatedShowsData> {
    try {
      const response = await fetch(`${this.baseUrl}?name=${encodeURIComponent(searchString)}`);

      const data = await response.json();
      return new TopRatedShowsData(data);
    } catch(error) {
      throw error;
    }
  }

  /**
   * Fetches show results by genre and page number
   * @param genreId
   * @param page
   */
  async searchByGenre(genreId: number, page: number = 1): Promise<ResultPageData> {
    try {
      const response = await fetch(`${this.baseUrl}?genre=${genreId}&page=${page}`);

      const data = await response.json();
      return new ResultPageData(data);
    } catch(error) {
      throw error;
    }
  }

  /**
   * Searches for characters by show ID and provided search query
   * @param showId
   * @param name
   */
  async searchCharacters(showId: number, name ?: string): Promise<RoleData[]> {
    const params = (name?.length > 0) ? `?name=${encodeURIComponent(name)}` : '';
    const url = `${this.baseUrl}/${showId}/characters${params}`;

    try {
      const response = await fetch(url);
      const data = await response.json();
      return data.map((role: {}) => {
        return new RoleData(role);
      });
    } catch (error) {
      throw error;
    }
  }

  /**
   * Fetches the show details by ID
   * @param showId
   */
  async fetchShowDetails(showId: number): Promise<ShowData> {
    try {
      const response = await fetch(`${this.baseUrl}/${showId}`, {
        credentials: 'include'
      });

      this.checkPageNotFound(response);
      const data = await response.json();
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
      const response = await fetch(`${this.baseUrl}/${showId}/num-seasons`);

      const data = await response.json();
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

      const data = await response.json();
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
  async addShowReview(showId:number, data: {}): Promise<Response> {
    try {
      const response = await fetch(`${this.baseUrl}/${showId}/reviews`, {
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
   * Likes the show review by its ID
   * @param reviewId
   */
  async likeShowReview(reviewId: number): Promise<Response> {
    try {
      const response = await fetch(`${this.baseUrl}/reviews/${reviewId}/likes`, {
        method: 'POST',
        credentials: 'include'
      });

      this.checkUnauthorizedUser(response);
      return response;
    } catch(error) {
      throw error;
    }
  }

  /**
   * Unlikes the show review by its ID
   * @param reviewId
   */
  async unlikeShowReview(reviewId: number): Promise<Response> {
    try {
      const response = await fetch(`${this.baseUrl}/reviews/${reviewId}/likes`, {
        method: 'DELETE',
        credentials: 'include'
      });

      this.checkUnauthorizedUser(response);
      return response;
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
      const response = await fetch(`${this.baseUrl}/${showId}/seasons/${seasonNumber}`, {
        credentials: 'include'
      });

      this.checkPageNotFound(response);
      const data = await response.json();
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
      const response = await fetch(`${this.baseUrl}/${showId}/seasons/${seasonNumber}/episodes/${episodeNumber}`);

      this.checkPageNotFound(response);
      const data = await response.json();
      return new EpisodeData(data);
    } catch (error) {
      throw error;
    }
  }

  /**
   * Fetches a page of the trending shows on TMDB
   * @param page
   */
  async fetchTrendingShows(page: number = 1): Promise<TrendingShowsData> {
    try {
      const response = await fetch(`${this.baseUrl}/trending?page=${page}`);

      const data = await response.json();
      return new TrendingShowsData(data);
    } catch (error) {
      throw error;
    }
  }

  /**
   * Fetches all show genres on TMDB
   */
  async fetchShowGenres(): Promise<ShowGenresData> {
    try {
      const response = await fetch(`${this.baseUrl}/genres`);

      const data = await response.json();
      return new ShowGenresData(data);
    } catch (error) {
      throw error;
    }
  }

  /**
   * Fetches a page of the top-rated shows on TMDB
   * @param page
   */
  async fetchTopRatedShows(page: number = 1): Promise<TopRatedShowsData> {
    try {
      const response = await fetch(`${this.baseUrl}/top?page=${page}`);

      const data = await response.json();
      return new TopRatedShowsData(data);
    } catch (error) {
      throw error;
    }
  }
}
