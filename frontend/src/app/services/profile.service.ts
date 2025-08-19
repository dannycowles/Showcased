import { Injectable } from '@angular/core';
import { UserData } from '../data/user-data';
import { ShowRankingData } from '../data/lists/show-ranking-data';
import { EpisodeRankingData } from '../data/lists/episode-ranking-data';
import { UserSearchData } from '../data/user-search-data';
import { SeasonRankingData } from '../data/lists/season-ranking-data';
import { CharacterRankingsData } from '../data/character-rankings-data';
import { CollectionData } from '../data/collection-data';
import { SingleCollectionData } from '../data/single-collection-data';
import { ShowListData } from '../data/lists/show-list-data';
import {DynamicRankingData} from '../data/lists/dynamic-ranking-data';
import {Router} from '@angular/router';
import {UpdateProfileDetailsDto} from '../data/dto/update-profile-details-dto';
import {AddSocialDto} from '../data/dto/add-social-dto';
import {
  AddToCharacterRankingList, AddToCollection, AddToDynamicRankingList,
  AddToEpisodeRankingList, AddToSeasonRankingList,
  AddToShowRankingList,
  AddToWatchingListDto,
  AddToWatchlistDto
} from '../data/dto/add-to-list-dto';
import {
  UpdateCharacterRankingDto, UpdateDynamicRankingDto,
  UpdateEpisodeRankingDto,
  UpdateSeasonRankingDto,
  UpdateShowRankingDto
} from '../data/dto/update-list-ranks-dto';
import {CreateCollectionDto} from '../data/dto/create-collection-dto';
import {UpdateCollectionDetails} from '../data/dto/update-collection-details';
import {ActivityData} from '../data/activity-data';
import {PageData} from '../data/page-data';
import {UserService} from './user.service';
import {ProfileEpisodeReviewData, ProfileShowReviewData} from '../data/profile-reviews-data';
import {ProfileReviewData} from '../data/types';
import {ProfileSettingsData} from '../data/profile-settings-data';

@Injectable({
  providedIn: 'root',
})
export class ProfileService {
  readonly baseUrl: string = 'http://localhost:8080/profile';
  private accessToken: string | null = localStorage.getItem("accessToken");

  constructor(public router: Router) {};

  // If the user is unauthorized, we redirect them to the login page
  checkUnauthorizedUser(response: Response): void {
    if (response.status === 403) {
      this.router.navigate(['/login']);
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

  /**
   * Retrieves all profile information
   */
  async getProfileDetails(): Promise<UserData> {
    const response = await fetch(`${this.baseUrl}/details`, {
      headers: this.getHeaders()
    });

    this.checkUnauthorizedUser(response);
    return await response.json();
  }

  /**
   * Retrieves all profile settings for the logged-in user
   */
  async getProfileSettings(): Promise<ProfileSettingsData> {
    const response = await fetch(`${this.baseUrl}/settings`, {
      headers: this.getHeaders()
    });

    this.checkUnauthorizedUser(response);
    return await response.json();
  }

  /**
   * Retrieves profile activity for the logged-in user
   */
  async getProfileActivity(page ?: number): Promise<PageData<ActivityData>> {
    const params = page != null ? `?page=${page}` : '';
    const response = await fetch(`${this.baseUrl}/activity${params}`, {
      headers: this.getHeaders()
    });

    this.checkUnauthorizedUser(response);
    return await response.json();
  }

  /**
   * Updates the profile information, currently it's just for bio updates
   * @param data
   */
  async updateProfileDetails(data: UpdateProfileDetailsDto): Promise<Response> {
    const response = await fetch(`${this.baseUrl}/details`, {
      method: 'PATCH',
      headers: this.getHeaders(true),
      body: JSON.stringify(data),
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Adds a new social account, if user already has a social account for
   * the given social page it will overwrite it
   * @param data
   */
  async addSocialAccount(data: AddSocialDto): Promise<Response> {
    const response = await fetch(`${this.baseUrl}/socials`, {
      method: 'POST',
      headers: this.getHeaders(true),
      body: JSON.stringify(data),
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Removes a social account by the id type of the social page
   * @param socialId
   */
  async removeSocialAccount(socialId: number): Promise<Response> {
    const response = await fetch(`${this.baseUrl}/socials/${socialId}`, {
      method: 'DELETE',
      headers: this.getHeaders()
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Uploads a profile picture for the logged-in user
   * @param formData
   */
  async uploadProfilePicture(formData: FormData): Promise<string> {
    const response = await fetch(`${this.baseUrl}/profile-picture`, {
      method: 'POST',
      headers: this.getHeaders(),
      body: formData,
    });

    this.checkUnauthorizedUser(response);
    return await response.text();
  }

  /**
   * Retrieves the combined list of all review types for the logged-in user, paged & sorted as requested
   * @param page
   * @param sort
   */
  async getCombinedReviews(page ?: number, sort ?: string): Promise<PageData<ProfileReviewData>>  {
    const url = new URL(`${this.baseUrl}/reviews`);
    if (page != null) url.searchParams.set('page', String(page));
    if (sort != null) url.searchParams.set('sort', sort);

    const response = await fetch(url, {
      headers: this.getHeaders()
    });

    this.checkUnauthorizedUser(response);
    return response.json();
  }

  /**
   * Retrieves show reviews for the logged-in user, paged & sorted as requested
   * @param page
   * @param sort
   */
  async getShowReviews(page ?: number, sort ?: string): Promise<PageData<ProfileShowReviewData>> {
    const url = new URL(`${this.baseUrl}/show-reviews`);
    if (page != null) url.searchParams.set('page', String(page));
    if (sort != null) url.searchParams.set('sort', sort);

    const response = await fetch(url, {
      headers: this.getHeaders()
    });

    this.checkUnauthorizedUser(response);
    return response.json();
  }

  /**
   * Retrieves episode reviews for the logged-in user, paged & sorted as requested
   * @param page
   * @param sort
   */
  async getEpisodeReviews(page ?: number, sort ?: string): Promise<PageData<ProfileEpisodeReviewData>> {
    const url = new URL(`${this.baseUrl}/episode-reviews`);
    if (page != null) url.searchParams.set('page', String(page));
    if (sort != null) url.searchParams.set('sort', sort);

    const response = await fetch(url, {
      headers: this.getHeaders()
    });

    this.checkUnauthorizedUser(response);
    return response.json();
  }

  /**
   * Adds a show to the logged-in user's watchlist
   * @param data
   */
  async addShowToWatchlist(data: AddToWatchlistDto): Promise<Response> {
    const response = await fetch(`${this.baseUrl}/watchlist`, {
      method: 'POST',
      headers: this.getHeaders(true),
      body: JSON.stringify(data),
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Retrieves the full watchlist for the profile
   */
  async getFullWatchlist(): Promise<ShowListData[]> {
    const response = await fetch(`${this.baseUrl}/watchlist`, {
      headers: this.getHeaders()
    });

    this.checkUnauthorizedUser(response);
    return await response.json();
  }

  /**
   * Removes a show from profile watchlist by ID
   * @param id
   */
  async removeShowFromWatchlist(id: number): Promise<Response> {
    const response = await fetch(`${this.baseUrl}/watchlist/${id}`, {
      method: 'DELETE',
      headers: this.getHeaders()
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Moves a show from watchlist to watching list by ID
   * @param id
   */
  async moveShowToWatchingList(id: number): Promise<Response> {
    const response = await fetch(`${this.baseUrl}/watchlist/${id}`, {
      method: 'PUT',
      headers: this.getHeaders()
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Adds a show to the logged-in user's currently watching list
   * @param data
   */
  async addShowToWatchingList(data: AddToWatchingListDto): Promise<Response> {
    const response = await fetch(`${this.baseUrl}/currently-watching`, {
      method: 'POST',
      headers: this.getHeaders(true),
      body: JSON.stringify(data),
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Retrieves the full watching list for the profile
   */
  async getFullWatchingList(): Promise<ShowListData[]> {
    const response = await fetch(`${this.baseUrl}/currently-watching`, {
      headers: this.getHeaders()
    });

    this.checkUnauthorizedUser(response);
    return await response.json();
  }

  /**
   * Removes a show from watching list by ID
   * @param id
   */
  async removeShowFromWatchingList(id: number): Promise<Response> {
    const response = await fetch(`${this.baseUrl}/currently-watching/${id}`, {
      method: 'DELETE',
      headers: this.getHeaders()
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Moves a show from currently watching to ranking list by ID
   * @param id
   */
  async moveShowToRankingList(id: number): Promise<Response> {
    const response = await fetch(`${this.baseUrl}/currently-watching/${id}`, {
      method: 'PUT',
      headers: this.getHeaders()
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Adds a show to the logged-in user's ranking list
   * @param data
   */
  async addShowToRankingList(data: AddToShowRankingList): Promise<Response> {
    const response = await fetch(`${this.baseUrl}/show-rankings`, {
      method: 'POST',
      headers: this.getHeaders(true),
      body: JSON.stringify(data),
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Retrieves the full show ranking list for the profile
   */
  async getFullShowRankingList(): Promise<ShowRankingData[]> {
    const response = await fetch(`${this.baseUrl}/show-rankings`, {
      headers: this.getHeaders()
    });

    this.checkUnauthorizedUser(response);
    return await response.json();
  }

  /**
   * Removes a show from ranking list by ID
   * @param id
   */
  async removeShowFromRankingList(id: number): Promise<Response> {
    const response = await fetch(`${this.baseUrl}/show-rankings/${id}`, {
      method: 'DELETE',
      headers: this.getHeaders()
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Updates the show ranking list, in particular it updates the rank numbers of each show
   * @param data
   */
  async updateShowRankingList(data: UpdateShowRankingDto[]): Promise<Response> {
    const response = await fetch(`${this.baseUrl}/show-rankings`, {
      method: 'PATCH',
      headers: this.getHeaders(true),
      body: JSON.stringify(data),
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Adds an episode to the logged-in user's ranking list
   * @param data
   */
  async addEpisodeToRankingList(data: AddToEpisodeRankingList): Promise<Response> {
    const response = await fetch(`${this.baseUrl}/episode-rankings`, {
      method: 'POST',
      headers: this.getHeaders(true),
      body: JSON.stringify(data),
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Retrieves the full episode ranking list for the profile
   */
  async getFullEpisodeRankingList(): Promise<EpisodeRankingData[]> {
    const response = await fetch(`${this.baseUrl}/episode-rankings`, {
      headers: this.getHeaders()
    });

    this.checkUnauthorizedUser(response);
    return await response.json();
  }

  /**
   * Removes an episode from ranking list by ID
   * @param id
   */
  async removeEpisodeFromRankingList(id: number): Promise<Response> {
    const response = await fetch(`${this.baseUrl}/episode-rankings/${id}`, {
      method: 'DELETE',
      headers: this.getHeaders()
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Updates the episode ranking list, in particular it updates the rank numbers of each episode
   * @param data
   */
  async updateEpisodeRankingList(data: UpdateEpisodeRankingDto[]): Promise<Response> {
    const response = await fetch(`${this.baseUrl}/episode-rankings`, {
      method: 'PATCH',
      headers: this.getHeaders(true),
      body: JSON.stringify(data),
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Adds a season to the logged-in user's ranking list
   * @param data
   */
  async addSeasonToRankingList(data: AddToSeasonRankingList): Promise<Response> {
    const response = await fetch(`${this.baseUrl}/season-rankings`, {
      method: 'POST',
      headers: this.getHeaders(true),
      body: JSON.stringify(data),
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Removes season from the logged-in user's ranking list by ID
   * @param id
   */
  async removeSeasonFromRankingList(id: number): Promise<Response> {
    const response = await fetch(`${this.baseUrl}/season-rankings/${id}`, {
      method: 'DELETE',
      headers: this.getHeaders()
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Retrieves the full season ranking list for the logged-in user
   */
  async getSeasonRankingList(): Promise<SeasonRankingData[]> {
    const response = await fetch(`${this.baseUrl}/season-rankings`, {
      headers: this.getHeaders()
    });

    this.checkUnauthorizedUser(response);
    return await response.json();
  }

  /**
   * Retrieves a list of all the users currently following the logged-in user
   * @param name
   * @param page
   */
  async getFollowersList(name?: string, page ?: number): Promise<PageData<UserSearchData>> {
    const url = new URL(`${this.baseUrl}/followers`);
    if (name?.length > 0) url.searchParams.set('name', name);
    if (page != null) url.searchParams.set('page', String(page));

    const response = await fetch(url, {
      headers: this.getHeaders()
    });

    this.checkUnauthorizedUser(response);
    return response.json();
  }

  /**
   * Retrieves a list of all the users the logged-in user is following
   * @param name
   * @param page
   */
  async getFollowingList(name?: string, page ?: number): Promise<PageData<UserSearchData>> {
    const url = new URL(`${this.baseUrl}/following`);
    if (name?.length > 0) url.searchParams.set('name', name);
    if (page != null) url.searchParams.set('page', String(page));

    const response = await fetch(url, {
      headers: this.getHeaders()
    });

    this.checkUnauthorizedUser(response);
    return response.json();
  }

  /**
   * Updates the season ranking list for the logged-in user, in particular the rank nums
   * @param data
   */
  async updateSeasonRankingList(data: UpdateSeasonRankingDto[]): Promise<Response> {
    const response = await fetch(`${this.baseUrl}/season-rankings`, {
      method: 'PATCH',
      headers: this.getHeaders(true),
      body: JSON.stringify(data),
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Retrieves all the character ranking lists for the logged-in user
   */
  async getCharacterRankingLists(): Promise<CharacterRankingsData> {
    const response = await fetch(`${this.baseUrl}/character-rankings`, {
      headers: this.getHeaders()
    });

    this.checkUnauthorizedUser(response);
    return await response.json();
  }

  /**
   * Adds a character to a character ranking list for the logged-in user
   * @param data
   */
  async addCharacterToRankingList(data: AddToCharacterRankingList): Promise<Response> {
    const response = await fetch(`${this.baseUrl}/character-rankings`, {
      method: 'POST',
      headers: this.getHeaders(true),
      body: JSON.stringify(data),
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Remove a character by ID
   */
  async removeCharacterFromRankingList(id: string): Promise<Response> {
    const response = await fetch(`${this.baseUrl}/character-rankings/${id}`, {
      method: 'DELETE',
      headers: this.getHeaders()
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Updates a character ranking list for the logged-in user
   */
  async updateCharacterRankingList(data: UpdateCharacterRankingDto): Promise<Response> {
    const response = await fetch(`${this.baseUrl}/character-rankings`, {
      method: 'PATCH',
      headers: this.getHeaders(true),
      body: JSON.stringify(data),
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Removes the specified id from the logged-in user's following list
   * @param id
   */
  async removeFollower(id: number): Promise<Response> {
    const response: Response = await fetch(`${this.baseUrl}/followers/${id}`, {
      method: 'DELETE',
      headers: this.getHeaders()
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Retrieves all collections for the logged-in user
   */
  async getCollections(name?: string): Promise<CollectionData[]> {
    const params = name?.length > 0 ? `?name=${encodeURIComponent(name)}` : '';
    const url = `${this.baseUrl}/collections${params}`;

    const response = await fetch(url, {
      headers: this.getHeaders()
    });

    this.checkUnauthorizedUser(response);
    return await response.json();
  }

  /**
   * Creates a new collection for the logged-in user
   * @param data
   */
  async createCollection(data: CreateCollectionDto): Promise<Response> {
    const response = await fetch(`${this.baseUrl}/collections`, {
      method: 'POST',
      headers: this.getHeaders(true),
      body: JSON.stringify(data),
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Deletes a collection for the logged-in user by ID
   * @param id
   */
  async deleteCollection(id: number): Promise<Response> {
    const response = await fetch(`${this.baseUrl}/collections/${id}`, {
      method: 'DELETE',
      headers: this.getHeaders()
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Updates a collection for the logged-in user by ID
   * @param id
   * @param data
   */
  async updateCollection(id: number, data: UpdateCollectionDetails): Promise<Response> {
    const response = await fetch(`${this.baseUrl}/collections/${id}`, {
      method: 'PATCH',
      headers: this.getHeaders(true),
      body: JSON.stringify(data),
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Adds a show to a collection for the logged-in user by ID
   * @param id
   * @param data
   */
  async addShowToCollection(id: number, data: AddToCollection): Promise<Response> {
    const response = await fetch(`${this.baseUrl}/collections/${id}/shows`, {
      method: 'POST',
      headers: this.getHeaders(true),
      body: JSON.stringify(data),
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Retrieves shows in a collection for the logged-in user by ID
   * @param id
   */
  async getCollectionDetails(id: number): Promise<SingleCollectionData> {
    const response = await fetch(`${this.baseUrl}/collections/${id}`, {
      headers: this.getHeaders()
    });

    this.checkUnauthorizedUser(response);
    return await response.json();
  }

  /**
   * Removes a show from a collection for the logged-in user by ID
   * @param collectionId
   * @param showId
   */
  async removeShowFromCollection(collectionId: number, showId: number): Promise<Response> {
    const response = await fetch(`${this.baseUrl}/collections/${collectionId}/shows/${showId}`, {
        method: 'DELETE',
      headers: this.getHeaders()
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Retrieves the full character dynamic ranking list for the logged-in user
   */
  async getDynamicRankingList(): Promise<DynamicRankingData[]> {
    const response = await fetch(`${this.baseUrl}/character-dynamics`, {
      headers: this.getHeaders()
    });

    this.checkUnauthorizedUser(response);
    return await response.json();
  }

  /**
   * Adds a character dynamic to the ranking list for the logged-in user
   * @param data
   */
  async addDynamicToRankingList(data: AddToDynamicRankingList): Promise<Response> {
    const response = await fetch(`${this.baseUrl}/character-dynamics`, {
      method: 'POST',
      headers: this.getHeaders(true),
      body: JSON.stringify(data)
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Removes a character dynamic from the ranking list for the logged-in user by its ID
   * @param dynamicId
   */
  async removeDynamicFromRankingList(dynamicId: number): Promise<Response> {
    const response = await fetch(`${this.baseUrl}/character-dynamics/${dynamicId}`, {
      method: 'DELETE',
      headers: this.getHeaders()
    });

    this.checkUnauthorizedUser(response);
    return response;
  }

  /**
   * Updates the character dynamic ranking list for the logged-in user
   * @param data
   */
  async updateDynamicRankingList(data: UpdateDynamicRankingDto[]): Promise<Response> {
    const response = await fetch(`${this.baseUrl}/character-dynamics`, {
      method: 'PATCH',
      headers: this.getHeaders(true),
      body: JSON.stringify(data)
    });

    this.checkUnauthorizedUser(response);
    return response;
  }
}
