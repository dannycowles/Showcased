import {Injectable} from '@angular/core';
import {UserSearchData} from '../data/user-search-data';
import {UserData} from '../data/user-data';
import {ShowRankingData} from '../data/lists/show-ranking-data';
import {EpisodeRankingData} from '../data/lists/episode-ranking-data';
import {SeasonRankingData} from '../data/lists/season-ranking-data';
import {CharacterRankingsData} from '../data/character-rankings-data';
import {CollectionData} from '../data/collection-data';
import {SingleCollectionData} from '../data/single-collection-data';
import {ShowListData} from '../data/lists/show-list-data';
import {Router} from '@angular/router';
import {DynamicRankingData} from '../data/lists/dynamic-ranking-data';
import {PageData} from '../data/page-data';
import {ProfileReviewData} from '../data/types';
import {ProfileEpisodeReviewData, ProfileShowReviewData} from '../data/profile-reviews-data';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  readonly baseUrl: string = "http://localhost:8080/users";
  private accessToken: string | null = localStorage.getItem("accessToken");

  constructor(public router: Router) {};

  // If the user is unauthorized, we redirect them to the login page
  checkUnauthorizedUser(response: Response): void {
    if (response.status === 401) {
      this.router.navigate(['/login']);
    }
  }

  getHeaders() {
    const headers = new Headers();
    if (this.accessToken) {
      headers.append("Authorization", `Bearer ${this.accessToken}`);
    }
    return headers;
  }

  /**
   * Searches for users given a query
   * @param query
   */
  async searchUsers(query: string): Promise<UserSearchData[]> {
    const response = await fetch(`${this.baseUrl}?query=${encodeURIComponent(query)}`);
    return await response.json();
  }

  /**
   * Retrieves the user details for the given username
   * @param username
   */
  async getUserDetails(username: string): Promise<UserData> {
    const response = await fetch(`${this.baseUrl}/${username}/details`, {
      headers: this.getHeaders()
    });

    return await response.json();
  }

  /**
   * Retrieves the full watchlist for the user with the specified username
   * @param username
   */
  async getFullWatchlist(username: string): Promise<ShowListData[]> {
    const response = await fetch(`${this.baseUrl}/${username}/watchlist`);
    return await response.json();
  }

  /**
   * Retrieves the full watching list for the user with the specified username
   * @param username
   */
  async getFullWatchingList(username: string): Promise<ShowListData[]> {
    const response = await fetch(`${this.baseUrl}/${username}/currently-watching`);
    return await response.json();
  }

  /**
   * Retrieves the full show ranking list for the user with the specified username
   * @param username
   */
  async getFullShowRankingList(username: string): Promise<ShowRankingData[]> {
    const response = await fetch(`${this.baseUrl}/${username}/show-rankings`);
    return await response.json();
  }

  /**
   * Retrieves the full episode ranking list for the user with the specified username
   * @param username
   */
  async getFullEpisodeRankingList(username: string): Promise<EpisodeRankingData[]> {
    const response = await fetch(`${this.baseUrl}/${username}/episode-rankings`);
    return await response.json();
  }

  /**
   * Retrieves the full season ranking list for the user with the specified username
   * @param username
   */
  async getFullSeasonRankingList(username: string): Promise<SeasonRankingData[]> {
    const response = await fetch(`${this.baseUrl}/${username}/season-rankings`);
    return await response.json();
  }

  /**
   * Retrieves all the character ranking lists for the user with the specified username
   * @param username
   */
  async getAllCharacterRankingLists(username: string): Promise<CharacterRankingsData> {
    const response = await fetch(`${this.baseUrl}/${username}/character-rankings`);
    return await response.json();
  }

  /**
   * Unfollows the user with the specified id
   * @param id
   */
  async unfollowUser(id: number): Promise<Response> {
    const response = await fetch(`${this.baseUrl}/${id}/followers`, {
      method: 'DELETE',
      headers: this.getHeaders()
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Follows the user with the specified id
   * @param id
   */
  async followUser(id: number): Promise<Response> {
    const response = await fetch(`${this.baseUrl}/${id}/followers`, {
      method: 'POST',
      headers: this.getHeaders()
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Retrieves the followers list for user with the specified username
   * @param username
   * @param name
   * @param page
   */
  async getFollowersList(username: string, name ?: string, page ?: number): Promise<PageData<UserSearchData>> {
    const url = new URL(`${this.baseUrl}/${username}/followers`);
    if (name?.length > 0) url.searchParams.set('name', name);
    if (page != null) url.searchParams.set('page', String(page));

    const response = await fetch(url, {
      headers: this.getHeaders()
    });

    return response.json();
  }

  /**
   * Retrieves the following list for user with the specified username
   * @param username
   * @param name
   * @param page
   */
  async getFollowingList(username: string, name ?: string, page ?: number): Promise<PageData<UserSearchData>> {
    const url = new URL(`${this.baseUrl}/${username}/following`);
    if (name?.length > 0) url.searchParams.set('name', name);
    if (page != null) url.searchParams.set('page', String(page));

    const response = await fetch(url, {
      headers: this.getHeaders()
    });

    return response.json();
  }

  /**
   * Retrieves the public collections for user with the specified username
   * @param username
   * @param name
   * @param page
   */
  async getPublicCollections(username: string, name ?: string, page?: number): Promise<PageData<CollectionData>> {
    const url = new URL(`${this.baseUrl}/${username}/collections`);
    if (name?.length > 0) url.searchParams.set('name', name);
    if (page != null) url.searchParams.set('page', String(page));

    const response = await fetch(url);
    return await response.json();
  }

  /**
   * Retrieves the details for a collection with the specified id
   * @param collectionId
   */
  async getCollectionDetails(collectionId: number): Promise<SingleCollectionData> {
    const response = await fetch(`${this.baseUrl}/collections/${collectionId}`, {
      headers: this.getHeaders()
    });
    return await response.json();
  }

  /**
   * Likes a user's collection by the specified id
   * @param collectionId
   */
  async likeCollection(collectionId: number): Promise<Response> {
    const response = await fetch(`${this.baseUrl}/collections/${collectionId}/likes`, {
      method: 'POST',
      headers: this.getHeaders()
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Unlikes a user's collection by the specified id
   * @param collectionId
   */
  async unlikeCollection(collectionId: number): Promise<Response> {
    const response = await fetch(`${this.baseUrl}/collections/${collectionId}/likes`, {
      method: 'DELETE',
      headers: this.getHeaders()
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Retrieves the full character dynamic ranking list for the user with the specified username
   * @param username
   */
  async getFullDynamicRankingList(username: string): Promise<DynamicRankingData[]> {
    const response = await fetch(`${this.baseUrl}/${username}/character-dynamics`);
    return await response.json();
  }

  /**
   * Retrieves the combined list of all review types for the user with the specified username, paged & sorted as requested
   * @param username
   * @param page
   * @param sort
   */
  async getCombinedReviews(username: string, page ?: number, sort ?: string): Promise<PageData<ProfileReviewData>>  {
    const url = new URL(`${this.baseUrl}/${username}/reviews`);
    if (page != null) url.searchParams.set('page', String(page));
    if (sort != null) url.searchParams.set('sort', sort);

    const response = await fetch(url, {
      headers: this.getHeaders()
    });

    this.checkUnauthorizedUser(response);
    return response.json();
  }

  /**
   * Retrieves show reviews for the user with the specified username, paged & sorted as requested
   * @param username
   * @param page
   * @param sort
   */
  async getShowReviews(username: string, page ?: number, sort ?: string): Promise<PageData<ProfileShowReviewData>>  {
    const url = new URL(`${this.baseUrl}/${username}/show-reviews`);
    if (page != null) url.searchParams.set('page', String(page));
    if (sort != null) url.searchParams.set('sort', sort);

    const response = await fetch(url, {
      headers: this.getHeaders()
    });

    this.checkUnauthorizedUser(response);
    return response.json();
  }

  /**
   * Retrieves episode reviews for the user with the specified username, paged & sorted as requested
   * @param username
   * @param page
   * @param sort
   */
  async getEpisodeReviews(username: string, page ?: number, sort ?: string): Promise<PageData<ProfileEpisodeReviewData>>  {
    const url = new URL(`${this.baseUrl}/${username}/episode-reviews`);
    if (page != null) url.searchParams.set('page', String(page));
    if (sort != null) url.searchParams.set('sort', sort);

    const response = await fetch(url, {
      headers: this.getHeaders()
    });

    this.checkUnauthorizedUser(response);
    return response.json();
  }
}
