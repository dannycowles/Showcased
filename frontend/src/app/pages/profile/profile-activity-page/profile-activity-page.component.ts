import {Component, OnInit} from '@angular/core';
import {ActivityData} from '../../../data/activity-data';
import {ProfileService} from '../../../services/profile.service';
import {ActivityType} from '../../../data/enums';
import {UtilsService} from '../../../services/utils.service';
import {Router} from '@angular/router';
import {PageData} from '../../../data/page-data';

@Component({
  selector: 'app-profile-activity-page',
  templateUrl: './profile-activity-page.component.html',
  styleUrl: './profile-activity-page.component.css',
  standalone: false
})
export class ProfileActivityPageComponent implements OnInit {
  activityData: PageData<ActivityData>;
  readonly ActivityType = ActivityType;
  isLoading: boolean = false;
  isLoadingMore: boolean = false;

  constructor(private profileService: ProfileService,
              public utilsService: UtilsService,
              private router: Router) {};

  async ngOnInit() {
    try {
      this.isLoading = true;
      this.activityData = await this.profileService.getProfileActivity();
    } catch (error) {
      console.error(error);
    } finally {
      this.isLoading = false;
    }
  }

  async getMoreActivity() {
    try {
      // Retrieves the next page from the backend, adds by 2 because zero indexed on frontend and 1 indexed on backend
      this.isLoadingMore = true;
      const result = await this.profileService.getProfileActivity(this.activityData.page.number + 2);
      this.activityData.content.push(...result.content);
      this.activityData.page = result.page;
    } catch (error) {
      console.error(error);
    } finally {
      this.isLoadingMore = false;
    }
  }

  getFollowDescription(activity: ActivityData): string[] {
    return activity.description.split('{user}');
  }

  getShowReviewLikeDescription(activity: ActivityData): string[] {
    const replacedDescription = activity.description
      .replace('{showTitle}', activity.showReviewLike.showTitle);
    return replacedDescription.split('{user}');
  }

  getShowReviewCommentDescription(activity: ActivityData): string[] {
    const replacedDescription = activity.description
      .replace('{showTitle}', activity.showReviewComment.showTitle)
      .replace('{comment}', activity.showReviewComment.commentText);
    return replacedDescription.split('{user}');
  }

  getEpisodeReviewLikeDescription(activity: ActivityData): string[] {
    const replacedDescription = activity.description
      .replace('{showTitle}', activity.episodeReviewLike.showTitle)
      .replace('{season}', String(activity.episodeReviewLike.season))
      .replace('{episode}', String(activity.episodeReviewLike.episode))
      .replace('{episodeTitle}', activity.episodeReviewLike.episodeTitle);
    return replacedDescription.split('{user}');
  }

  getEpisodeReviewCommentDescription(activity: ActivityData): string[] {
    const replacedDescription = activity.description
      .replace('{showTitle}', activity.episodeReviewComment.showTitle)
      .replace('{season}', String(activity.episodeReviewComment.season))
      .replace('{episode}', String(activity.episodeReviewComment.episode))
      .replace('{episodeTitle}', activity.episodeReviewComment.episodeTitle)
      .replace('{comment}', activity.episodeReviewComment.commentText);
    return replacedDescription.split('{user}');
  }

  getShowReviewCommentLikeDescription(activity: ActivityData): string[] {
    const replacedDescription = (activity.showReviewCommentLike.isOwnComment)
      ? activity.description.replace("{reviewUser}'s", "your")
      : activity.description.replace('{reviewUser}', activity.showReviewCommentLike.reviewUser.username);
    return replacedDescription.replace('{showTitle}', activity.showReviewCommentLike.showTitle).split('{user}');
  }

  getEpisodeReviewCommentLikeDescription(activity: ActivityData): string[] {
    const replacedDescription = (activity.episodeReviewCommentLike.isOwnComment)
      ? activity.description.replace("{reviewUser}'s", "your")
      : activity.description.replace('{reviewUser}', activity.episodeReviewCommentLike.reviewUser.username);
    return replacedDescription.replace('{showTitle}', activity.episodeReviewCommentLike.showTitle)
      .replace('{season}', String(activity.episodeReviewCommentLike.season))
      .replace('{episode}', String(activity.episodeReviewCommentLike.episode))
      .replace('{episodeTitle}', activity.episodeReviewCommentLike.episodeTitle)
      .split('{user}');
  }

  getCollectionLikeDescription(activity: ActivityData): string[] {
    return activity.description.replace('{collectionName}', activity.collectionLike.collectionName).split('{user}');
  }

  navigateToActivity(activity: ActivityData) {
    switch (activity.activityType) {
      case ActivityType.Follow:
        break;
      case ActivityType.LikeShowReview:
        break;
      case ActivityType.CommentShowReview:
        break;
      case ActivityType.LikeEpisodeReview:
        break;
      case ActivityType.CommentEpisodeReview:
        break;
      case ActivityType.LikeShowReviewComment:
        break;
      case ActivityType.LikeEpisodeReviewComment:
        break;
      case (ActivityType.LikeCollection):
        this.router.navigate(['profile/collections', activity.collectionLike.collectionId]);
        break;
    }
  }
}
