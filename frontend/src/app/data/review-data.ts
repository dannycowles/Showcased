import {retry} from 'rxjs';

export class ReviewData {
  id: number;
  reviewerUsername: string;
  reviewerId: number;
  rating: number;
  commentary: string;
  containsSpoilers: boolean;
  likes: number;
  reviewDate: Date;

  constructor(jsonObject: { [key: string]: any }) {
    this.id = jsonObject['id'];
    this.reviewerUsername = jsonObject['username'];
    this.reviewerId = jsonObject['reviewerId'];
    this.rating = jsonObject['rating'];
    this.commentary = jsonObject['commentary'];
    this.containsSpoilers = jsonObject['containsSpoilers'];
    this.likes = jsonObject['numLikes'];
    this.reviewDate = new Date(jsonObject['reviewDate']);
  }

  get formattedDate():string {
    return this.reviewDate.toLocaleString();
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

    return this.formattedDate;
  }
}
