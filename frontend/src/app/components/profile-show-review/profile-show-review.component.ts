import {Component, Input} from '@angular/core';
import {ReviewData} from '../../data/review-data';
import {UtilsService} from '../../services/utils.service';
import {ButtonHeartComponent} from '../../pages/show/button-heart.component';
import {ShowService} from '../../services/show.service';

@Component({
  selector: 'app-profile-show-review',
  imports: [
    ButtonHeartComponent
  ],
  templateUrl: './profile-show-review.component.html',
  styleUrl: './profile-show-review.component.css',
  standalone: true
})
export class ProfileShowReviewComponent {
  @Input({required : true}) review : ReviewData;

  constructor(public utilsService: UtilsService,
              private showService: ShowService) {};

  async toggleLikeState(review: ReviewData) {
    try {
      review.likedByUser = !review.likedByUser;
      if (review.likedByUser) {
        await this.showService.likeShowReview(review.id);
        review.likes++;
      } else {
        await this.showService.unlikeShowReview(review.id);
        review.likes--;
      }
    } catch (error) {
      console.error(error);
    }
  }
}
