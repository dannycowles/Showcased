import {Injectable} from '@angular/core';
import {ShowData} from '../data/show/show-data';
import {SeasonData} from '../data/show/season-data';
import {EpisodeData} from '../data/show/episode-data';
import {ShowGenresData} from '../data/show-genres-data';
import {ResultPageData} from '../data/show/result-page-data';
import {RoleData} from '../data/role-data';
import {EpisodeReviewData, ShowReviewData} from '../data/reviews-data';
import {SeasonEpisodes} from '../data/show/season-episode';
import {Router} from '@angular/router';
import {ReviewCommentData} from '../data/review-comment-data';
import {AddCommentDto} from '../data/dto/add-comment-dto';
import {AddEpisodeReviewDto, AddShowReviewDto} from '../data/dto/add-review-dto';

@Injectable({
  providedIn: 'root'
})
export class ShowService {
  baseUrl: string = "http://localhost:8080/shows";

  constructor(public router: Router) {};

  // If the user is unauthorized, we redirect user to the login page
  checkUnauthorizedUser(response: Response) {
    if (response.status === 401) {
      this.router.navigate(['/login']);
      throw new Error("Unauthorized");
    }
  }

  // If the show / season / episode is not found, we redirect user to the 404 page
  checkPageNotFound(response: Response) {
    if (response.status === 500) {
      this.router.navigate(['/not-found']);
    }
  }

  /**
   * Fetches search results for a user query
    * @param searchString
   */
  async searchForShows(searchString: string): Promise<ResultPageData> {
    const response = await fetch(`${this.baseUrl}?name=${encodeURIComponent(searchString)}`);
    return await response.json();
  }

  /**
   * Fetches show results by genre and page number
   * @param genreId
   * @param page
   */
  async searchByGenre(genreId: number, page: number = 1): Promise<ResultPageData> {
    const response = await fetch(`${this.baseUrl}?genre=${genreId}&page=${page}`);
    return await response.json();
  }

  /**
   * Searches for characters by show ID and provided search query
   * @param showId
   * @param name
   */
  async searchCharacters(showId: number, name ?: string): Promise<RoleData[]> {
    const params = (name?.length > 0) ? `?name=${encodeURIComponent(name)}` : '';
    const url = `${this.baseUrl}/${showId}/characters${params}`;

    const response = await fetch(url);
    return await response.json();
  }

  /**
   * Fetches the show details by ID
   * @param showId
   */
  async fetchShowDetails(showId: number): Promise<ShowData> {
    const response = await fetch(`${this.baseUrl}/${showId}`, {
      credentials: 'include'
    });

    this.checkPageNotFound(response);
    return await response.json();
  }

  /**
   * Fetches the number of seasons by show ID
   * @param showId
   */
  async fetchNumberOfSeasons(showId: number): Promise<number> {
    const response = await fetch(`${this.baseUrl}/${showId}/num-seasons`);
    const data = await response.json();
    return data['numSeasons'];
  }

  /**
   * Fetches the reviews for a show by its ID
   * @param showId
   */
  async fetchShowReviews(showId: number): Promise<ShowReviewData[]> {
    const response = await fetch(`${this.baseUrl}/${showId}/reviews`, {
      credentials: 'include'
    });

    return await response.json();

  }

  /**
   * Adds a review to a show
   * @param showId
   * @param data
   */
  async addShowReview(showId:number, data: AddShowReviewDto): Promise<Response> {
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
  }

  /**
   * Likes the show review by its ID
   * @param reviewId
   */
  async likeShowReview(reviewId: number): Promise<Response> {
    const response = await fetch(`${this.baseUrl}/reviews/${reviewId}/likes`, {
      method: 'POST',
      credentials: 'include'
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Unlikes the show review by its ID
   * @param reviewId
   */
  async unlikeShowReview(reviewId: number): Promise<Response> {
    const response = await fetch(`${this.baseUrl}/reviews/${reviewId}/likes`, {
      method: 'DELETE',
      credentials: 'include'
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Adds a comment to a show review by its ID
   * @param reviewId
   * @param data
   */
  async addCommentToShowReview(reviewId: number, data: AddCommentDto): Promise<ReviewCommentData> {
    const response = await fetch(`${this.baseUrl}/reviews/${reviewId}/comments`, {
      method: 'POST',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(data)
    });

    this.checkUnauthorizedUser(response);
    return response.json();
  }

  /**
   * Retrieves all comments for a show review by its ID
   * @param reviewId
   */
  async getShowReviewComments(reviewId: number): Promise<ReviewCommentData[]> {
    const response = await fetch(`${this.baseUrl}/reviews/${reviewId}/comments`, {
      credentials: 'include'
    });
    return response.json();
  }

  /**
   * Likes a show review comment by its ID
   * @param commentId
   */
  async likeShowReviewComment(commentId: number): Promise<Response> {
    const response = await fetch(`${this.baseUrl}/reviews/comments/${commentId}/likes`, {
      method: 'POST',
      credentials: 'include',
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Unlikes a show review comment by its ID
   * @param commentId
   */
  async unlikeShowReviewComment(commentId: number): Promise<Response> {
    const response = await fetch(`${this.baseUrl}/reviews/comments/${commentId}/likes`, {
      method: 'DELETE',
      credentials: 'include',
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Adds a review to an episode by ID
   * @param episodeId
   * @param data
   */
  async addEpisodeReview(episodeId: number, data: AddEpisodeReviewDto): Promise<Response> {
    const response = await fetch(`${this.baseUrl}/episodes/${episodeId}/reviews`, {
      method: 'POST',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(data)
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Retrieves the reviews for an episode by its ID
   * @param episodeId
   */
  async fetchEpisodeReviews(episodeId: number): Promise<EpisodeReviewData[]> {
    const response = await fetch(`${this.baseUrl}/episodes/${episodeId}/reviews`, {
      credentials: 'include'
    });
    return await response.json();
  }

  /**
   * Likes an episode review by its ID
   * @param reviewId
   */
  async likeEpisodeReview(reviewId: number): Promise<Response> {
    const response = await fetch(`${this.baseUrl}/episode-reviews/${reviewId}/likes`, {
      method: 'POST',
      credentials: 'include'
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Unlikes an episode review by its ID
   * @param reviewId
   */
  async unlikeEpisodeReview(reviewId: number): Promise<Response> {
    const response = await fetch(`${this.baseUrl}/episode-reviews/${reviewId}/likes`, {
      method: 'DELETE',
      credentials: 'include'
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Adds a comment to an episode review by its ID
   * @param reviewId
   * @param data
   */
  async addCommentToEpisodeReview(reviewId: number, data: AddCommentDto): Promise<ReviewCommentData> {
    const response = await fetch(`${this.baseUrl}/episode-reviews/${reviewId}/comments`, {
      method: 'POST',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(data)
    });

    this.checkUnauthorizedUser(response);
    return response.json();
  }

  /**
   * Retrieves all comments for an episode review by its ID
   * @param reviewId
   */
  async getEpisodeReviewComments(reviewId: number): Promise<ReviewCommentData[]> {
    const response = await fetch(`${this.baseUrl}/episode-reviews/${reviewId}/comments`, {
      credentials: 'include'
    });
    return response.json();
  }

  /**
   * Likes an episode review comment by its ID
   * @param commentId
   */
  async likeEpisodeReviewComment(commentId: number): Promise<Response> {
    const response = await fetch(`${this.baseUrl}/episode-reviews/comments/${commentId}/likes`, {
      method: 'POST',
      credentials: 'include',
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Unlikes an episode review comment by its ID
   * @param commentId
   */
  async unlikeEpisodeReviewComment(commentId: number): Promise<Response> {
    const response = await fetch(`${this.baseUrl}/episode-reviews/comments/${commentId}/likes`, {
      method: 'DELETE',
      credentials: 'include',
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Fetches the season details for a show given its ID and season number
   * @param showId
   * @param seasonNumber
   */
  async fetchSeasonDetails(showId: number, seasonNumber: number): Promise<SeasonData> {
    const response = await fetch(`${this.baseUrl}/${showId}/seasons/${seasonNumber}`, {
      credentials: 'include'
    });

    this.checkPageNotFound(response);
    return await response.json();
  }

  /**
   * Fetches all episodes for a season given the show's ID and season number
   * @param showId
   * @param seasonNumber
   */
  async fetchSeasonEpisodes(showId: number, seasonNumber: number): Promise<SeasonEpisodes> {
    const response = await fetch(`${this.baseUrl}/${showId}/seasons/${seasonNumber}/episodes`);

    this.checkPageNotFound(response);
    return await response.json();
  }

  /**
   * Fetches the details for an episode given the show's ID, season, and episode number
   * @param showId
   * @param seasonNumber
   * @param episodeNumber
   */
  async fetchEpisodeDetails(showId: number, seasonNumber: number, episodeNumber: number): Promise<EpisodeData> {
    const response = await fetch(`${this.baseUrl}/${showId}/seasons/${seasonNumber}/episodes/${episodeNumber}`, {
      credentials: 'include'
    });

    this.checkPageNotFound(response);
    return await response.json();
  }

  /**
   * Fetches a page of the trending shows on TMDB
   * @param page
   */
  async fetchTrendingShows(page: number = 1): Promise<ResultPageData> {
    const response = await fetch(`${this.baseUrl}/trending?page=${page}`);
    return await response.json();
  }

  /**
   * Fetches all show genres on TMDB
   */
  async fetchShowGenres(): Promise<ShowGenresData> {
    const response = await fetch(`${this.baseUrl}/genres`);
    return await response.json();
  }

  /**
   * Fetches a page of the top-rated shows on TMDB
   * @param page
   */
  async fetchTopRatedShows(page: number = 1): Promise<ResultPageData> {
    const response = await fetch(`${this.baseUrl}/top?page=${page}`);
    return await response.json();
  }
}
