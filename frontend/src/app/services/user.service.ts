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

  constructor(public router: Router) {};

  // If the user is unauthorized, we redirect them to the login page
  checkUnauthorizedUser(response: Response): void {
    if (response.status === 401) {
      this.router.navigate(['/login']);
    }
  }

  /**
   * Searches for users given a query
   * @param query
   */
  async searchUsers(query: string): Promise<UserSearchData[]> {
    const response = await fetch(`${this.baseUrl}/search?query=${encodeURIComponent(query)}`);
    return await response.json();
  }

  /**
   * Retrieves the user details for the given user id
   * @param id
   */
  async getUserDetails(id: number): Promise<UserData> {
    const response = await fetch(`${this.baseUrl}/${id}/details`, {
      credentials: 'include'
    });

    return await response.json();
  }

  /**
   * Retrieves the full watchlist for the user with the specified id
   * @param id
   */
  async getFullWatchlist(id: number): Promise<ShowListData[]> {
    const response = await fetch(`${this.baseUrl}/${id}/watchlist`);
    return await response.json();
  }

  /**
   * Retrieves the full watching list for the user with the specified id
   * @param id
   */
  async getFullWatchingList(id:number): Promise<ShowListData[]> {
    const response = await fetch(`${this.baseUrl}/${id}/currently-watching`);
    return await response.json();
  }

  /**
   * Retrieves the full show ranking list for the user with the specified id
   * @param id
   */
  async getFullShowRankingList(id:number): Promise<ShowRankingData[]> {
    const response = await fetch(`${this.baseUrl}/${id}/show-rankings`);
    return await response.json();
  }

  /**
   * Retrieves the full episode ranking list for the user with the specified id
   * @param id
   */
  async getFullEpisodeRankingList(id:number): Promise<EpisodeRankingData[]> {
    const response = await fetch(`${this.baseUrl}/${id}/episode-rankings`);
    return await response.json();
  }

  /**
   * Retrieves the full season ranking list for the user with the specified id
   * @param id
   */
  async getFullSeasonRankingList(id:number): Promise<SeasonRankingData[]> {
    const response = await fetch(`${this.baseUrl}/${id}/season-rankings`);
    return await response.json();
  }

  /**
   * Retrieves all the character ranking lists for the user with the specified id
   * @param id
   */
  async getAllCharacterRankingLists(id: number): Promise<CharacterRankingsData> {
    const response = await fetch(`${this.baseUrl}/${id}/character-rankings`);
    return await response.json();
  }

  /**
   * Unfollows the user with the specified id
   * @param id
   */
  async unfollowUser(id: number): Promise<Response> {
    const response = await fetch(`${this.baseUrl}/${id}/followers`, {
      method: 'DELETE',
      credentials: 'include'
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
      credentials: 'include'
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Retrieves the followers list for user with the specified id
   * @param id
   * @param name
   * @param page
   */
  async getFollowersList(id: number, name ?: string, page ?: number): Promise<PageData<UserSearchData>> {
    const url = new URL(`${this.baseUrl}/${id}/followers`);
    if (name?.length > 0) url.searchParams.set('name', name);
    if (page != null) url.searchParams.set('page', String(page));

    const response = await fetch(url, {
      credentials: 'include'
    });

    return response.json();
  }

  /**
   * Retrieves the following list for user with the specified id
   * @param id
   * @param name
   * @param page
   */
  async getFollowingList(id: number, name ?: string, page ?: number): Promise<PageData<UserSearchData>> {
    const url = new URL(`${this.baseUrl}/${id}/following`);
    if (name?.length > 0) url.searchParams.set('name', name);
    if (page != null) url.searchParams.set('page', String(page));

    const response = await fetch(url, {
      credentials: 'include'
    });

    return response.json();
  }

  /**
   * Retrieves the public collections for user with the specified id
   * @param name
   * @param id
   */
  async getPublicCollections(id: number, name ?: string): Promise<CollectionData[]> {
    const params = (name?.length > 0) ? `?name=${encodeURIComponent(name)}` : '';
    const url = `${this.baseUrl}/${id}/collections${params}`;

    const response = await fetch(url);
    return await response.json();
  }

  /**
   * Retrieves the details for a collection with the specified id
   * @param collectionId
   */
  async getCollectionDetails(collectionId: number): Promise<SingleCollectionData> {
    const response = await fetch(`${this.baseUrl}/collections/${collectionId}`, {
      credentials: 'include'
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
      credentials: 'include'
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
      credentials: 'include'
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Retrieves the full character dynamic ranking list for the user with the specified id
   * @param id
   */
  async getFullDynamicRankingList(id: number): Promise<DynamicRankingData[]> {
    const response = await fetch(`${this.baseUrl}/${id}/character-dynamics`);
    return await response.json();
  }

  /**
   * Retrieves the combined list of all review types for the user with the specified id, paged & sorted as requested
   * @param id
   * @param page
   * @param sort
   */
  async getCombinedReviews(id: number, page ?: number, sort ?: string): Promise<PageData<ProfileReviewData>>  {
    const url = new URL(`${this.baseUrl}/${id}/reviews`);
    if (page != null) url.searchParams.set('page', String(page));
    if (sort != null) url.searchParams.set('sort', sort);

    const response = await fetch(url, {
      credentials: 'include'
    });

    this.checkUnauthorizedUser(response);
    return response.json();
  }

  /**
   * Retrieves show reviews for the user with the specified id, paged & sorted as requested
   * @param id
   * @param page
   * @param sort
   */
  async getShowReviews(id: number, page ?: number, sort ?: string): Promise<PageData<ProfileShowReviewData>>  {
    const url = new URL(`${this.baseUrl}/${id}/show-reviews`);
    if (page != null) url.searchParams.set('page', String(page));
    if (sort != null) url.searchParams.set('sort', sort);

    const response = await fetch(url, {
      credentials: 'include'
    });

    this.checkUnauthorizedUser(response);
    return response.json();
  }

  /**
   * Retrieves episode reviews for the user with the specified id, paged & sorted as requested
   * @param id
   * @param page
   * @param sort
   */
  async getEpisodeReviews(id: number, page ?: number, sort ?: string): Promise<PageData<ProfileEpisodeReviewData>>  {
    const url = new URL(`${this.baseUrl}/${id}/episode-reviews`);
    if (page != null) url.searchParams.set('page', String(page));
    if (sort != null) url.searchParams.set('sort', sort);

    const response = await fetch(url, {
      credentials: 'include'
    });

    this.checkUnauthorizedUser(response);
    return response.json();
  }
}
