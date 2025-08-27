import {Injectable} from '@angular/core';
import {ShowData} from '../data/show/show-data';
import {SeasonData} from '../data/show/season-data';
import {EpisodeData} from '../data/show/episode-data';
import {ShowGenresData} from '../data/show-genres-data';
import {GenreResultPageData, ResultPageData} from '../data/show/result-page-data';
import {RoleData} from '../data/role-data';
import {EpisodeReviewData, ShowReviewData} from '../data/reviews-data';
import {SeasonEpisodes} from '../data/show/season-episode';
import {Router} from '@angular/router';
import {ReviewCommentData} from '../data/review-comment-data';
import {AddCommentDto} from '../data/dto/add-comment-dto';
import {AddEpisodeReviewDto, AddShowReviewDto} from '../data/dto/add-review-dto';
import {PageData} from '../data/page-data';
import {UpdateReviewDto} from '../data/dto/update-review-dto';
import {AuthenticationService} from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class ShowService {
  baseUrl: string = "http://localhost:8080/shows";
  private accessToken: string | null = localStorage.getItem("accessToken");

  constructor(public router: Router,
              private authService: AuthenticationService) {};

  // If the user is unauthorized, we redirect user to the login page
  checkUnauthorizedUser(response: Response) {
    if (response.status === 403) {
      this.authService.logout();
      throw new Error("Unauthorized");
    }
  }

  getHeaders(containsPayload: boolean = false): Headers {
    const headers = new Headers();
    if (this.accessToken) {
      headers.append("Authorization", `Bearer ${this.accessToken}`);
    }
    if (containsPayload) {
      headers.append("Content-Type", "application/json");
    }
    return headers;
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
  async searchByGenre(genreId: number, page: number = 1): Promise<GenreResultPageData> {
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
      headers: this.getHeaders()
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
   * @param page
   * @param sortOption
   */
  async fetchShowReviews(showId: number, page ?: number, sortOption ?: string): Promise<PageData<ShowReviewData>> {
    const url = new URL(`${this.baseUrl}/${showId}/reviews`);
    if (page != null) url.searchParams.set('page', String(page));
    if (sortOption != null) url.searchParams.set('sort', sortOption);

    const response = await fetch(url, {
      headers: this.getHeaders()
    });
    return await response.json();
  }

  /**
   * Fetches a show review by its ID, main usage is for profile notification clicks
   * @param reviewId
   */
  async fetchShowReview(reviewId: number): Promise<ShowReviewData> {
    const response = await fetch(`${this.baseUrl}/reviews/${reviewId}`, {
      headers: this.getHeaders()
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
      headers: this.getHeaders(true),
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
      headers: this.getHeaders()
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
      headers: this.getHeaders()
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Deletes the show review by its ID
   * @param reviewId
   */
  async deleteShowReview(reviewId: number): Promise<Response> {
    const response = await fetch(`${this.baseUrl}/reviews/${reviewId}`, {
      method: 'DELETE',
      headers: this.getHeaders()
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Updates the show review by its ID
   * @param reviewId
   * @param updates
   */
  async updateShowReview(reviewId: number, updates: UpdateReviewDto): Promise<Response> {
    const response = await fetch(`${this.baseUrl}/reviews/${reviewId}`, {
      method: 'PATCH',
      headers: this.getHeaders(true),
      body: JSON.stringify(updates)
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
      headers: this.getHeaders(true),
      body: JSON.stringify(data)
    });

    this.checkUnauthorizedUser(response);
    return response.json();
  }

  /**
   * Retrieves all comments for a show review by its ID
   * @param reviewId
   * @param page
   */
  async getShowReviewComments(reviewId: number, page ?: number): Promise<PageData<ReviewCommentData>> {
    const params = page != null ? `?page=${page}` : '';
    const response = await fetch(`${this.baseUrl}/reviews/${reviewId}/comments${params}`, {
      headers: this.getHeaders()
    });
    return response.json();
  }

  /**
   * Retrieves a comment for a show review by its ID, main usage is for profile notification click
   * @param commentId
   */
  async getShowReviewComment(commentId: number): Promise<ReviewCommentData> {
    const response = await fetch(`${this.baseUrl}/reviews/comments/${commentId}`, {
      headers: this.getHeaders()
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
      headers: this.getHeaders()
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
      headers: this.getHeaders()
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Deletes a show review comment by its ID
   * @param commentId
   */
  async deleteShowReviewComment(commentId: number): Promise<Response> {
    const response = await fetch(`${this.baseUrl}/reviews/comments/${commentId}`, {
      method: 'DELETE',
      headers: this.getHeaders()
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Updates a show review comment by its ID
   * @param commentId
   * @param updates
   */
  async updateShowReviewComment(commentId: number, updates: AddCommentDto): Promise<Response> {
    const response = await fetch(`${this.baseUrl}/reviews/comments/${commentId}`, {
      method: 'PATCH',
      headers: this.getHeaders(true),
      body: JSON.stringify(updates)
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
      headers: this.getHeaders(true),
      body: JSON.stringify(data)
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Retrieves the reviews for an episode by its ID
   * @param episodeId
   * @param page
   * @param sortOption
   */
  async fetchEpisodeReviews(episodeId: number, page ?: number, sortOption ?: string): Promise<PageData<EpisodeReviewData>> {
    const url = new URL(`${this.baseUrl}/episodes/${episodeId}/reviews`);
    if (page != null) url.searchParams.set('page', String(page));
    if (sortOption != null) url.searchParams.set('sort', sortOption);

    const response = await fetch(url, {
      headers: this.getHeaders()
    });
    return await response.json();
  }

  /**
   * Fetches an episode review by its ID, main usage is for profile notification clicks
   * @param reviewId
   */
  async fetchEpisodeReview(reviewId: number): Promise<EpisodeReviewData> {
    const response = await fetch(`${this.baseUrl}/episode-reviews/${reviewId}`, {
      headers: this.getHeaders()
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
      headers: this.getHeaders()
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
      headers: this.getHeaders()
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Deletes an episode review by its ID
   * @param reviewId
   */
  async deleteEpisodeReview(reviewId: number): Promise<Response> {
    const response = await fetch(`${this.baseUrl}/episode-reviews/${reviewId}`, {
      method: 'DELETE',
      headers: this.getHeaders()
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Updates an episode review by its ID
   * @param reviewId
   * @param updates
   */
  async updateEpisodeReview(reviewId: number, updates: UpdateReviewDto): Promise<Response> {
    const response = await fetch(`${this.baseUrl}/episode-reviews/${reviewId}`, {
      method: 'PATCH',
      headers: this.getHeaders(true),
      body: JSON.stringify(updates),
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
      headers: this.getHeaders(true),
      body: JSON.stringify(data)
    });

    this.checkUnauthorizedUser(response);
    return response.json();
  }

  /**
   * Retrieves all comments for an episode review by its ID
   * @param reviewId
   * @param page
   */
  async getEpisodeReviewComments(reviewId: number, page ?: number): Promise<PageData<ReviewCommentData>> {
    const params = page != null ? `?page=${page}` : '';
    const response = await fetch(`${this.baseUrl}/episode-reviews/${reviewId}/comments${params}`, {
      headers: this.getHeaders()
    });
    return response.json();
  }

  /**
   * Retrieves a comment for an episode review by its ID, main usage is for profile notification clicks
   * @param commentId
   */
  async getEpisodeReviewComment(commentId: number): Promise<ReviewCommentData> {
    const response = await fetch(`${this.baseUrl}/episode-reviews/comments/${commentId}`, {
      headers: this.getHeaders()
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
      headers: this.getHeaders()
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
      headers: this.getHeaders()
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Deletes an episode review comment by its ID
   * @param commentId
   */
  async deleteEpisodeReviewComment(commentId: number): Promise<Response> {
    const response = await fetch(`${this.baseUrl}/episode-reviews/comments/${commentId}`, {
      method: 'DELETE',
      headers: this.getHeaders()
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Updates an episode review comment by its ID
   * @param commentId
   * @param updates
   */
  async updateEpisodeReviewComment(commentId: number, updates: AddCommentDto): Promise<Response> {
    const response = await fetch(`${this.baseUrl}/episode-reviews/comments/${commentId}`, {
      method: 'PATCH',
      headers: this.getHeaders(true),
      body: JSON.stringify(updates)
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
      headers: this.getHeaders()
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
      headers: this.getHeaders()
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
