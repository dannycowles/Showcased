import {UtilsService} from '../services/utils.service';

export class ReviewData {
  readonly id: number;
  readonly showTitle: string;
  readonly reviewerUsername: string;
  readonly reviewerProfilePicture: string;
  readonly reviewerId: number;
  readonly rating: number;
  readonly commentary: string;
  readonly containsSpoilers: boolean;
  likes: number;
  readonly reviewDate: Date;
  readonly utilsService: UtilsService;
  likedByUser: boolean;

  constructor(jsonObject: { [key: string]: any }, utilsService: UtilsService) {
    this.id = jsonObject['id'];
    this.showTitle = jsonObject['showTitle'];
    this.reviewerUsername = jsonObject['username'];
    this.reviewerProfilePicture = jsonObject['profilePicture'];
    this.reviewerId = jsonObject['reviewerId'];
    this.rating = jsonObject['rating'];
    this.commentary = jsonObject['commentary'];
    this.containsSpoilers = jsonObject['containsSpoilers'];
    this.likes = jsonObject['numLikes'];
    this.reviewDate = new Date(jsonObject['reviewDate']);
    this.utilsService = utilsService;
    this.likedByUser = jsonObject['likedByUser'];
  }

  /**
   * This function returns the time since the review was left relative to the
   * user's current time
   */
  get relativeDate():string {
    let diffInSeconds = Math.floor(new Date().getTime() - this.reviewDate.getTime()) / 1000;
    if (diffInSeconds < 60) {
      return `${diffInSeconds} seconds ago`;
    }

    let diffInMinutes = Math.floor(diffInSeconds / 60);
    if (diffInMinutes < 60) {
      if (diffInMinutes === 1) {
        return `${diffInMinutes} minute ago`;
      } else {
        return `${diffInMinutes} minutes ago`;
      }
    }

    let diffInHours = Math.floor(diffInMinutes / 60);
    if (diffInHours < 24) {
      if (diffInHours === 1) {
        return `${diffInHours} hour ago`;
      } else {
        return `${diffInHours} hours ago`;
      }
    }

    let diffInDays = Math.floor(diffInHours / 24);
    if (diffInDays < 7) {
      if (diffInDays === 1) {
        return `${diffInDays} day ago`;
      } else {
        return `${diffInDays} days ago`;
      }
    }

    return this.utilsService.getFormattedDate(this.reviewDate);
  }
}
