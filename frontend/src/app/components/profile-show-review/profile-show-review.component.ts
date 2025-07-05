import {Component, Input} from '@angular/core';
import {UtilsService} from '../../services/utils.service';
import {ButtonHeartComponent} from '../button-heart.component';
import {ShowService} from '../../services/show.service';
import {ShowReviewData} from '../../data/reviews-data';

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
  @Input({required : true}) review : ShowReviewData;

  constructor(public utilsService: UtilsService,
              private showService: ShowService) {};

  async toggleLikeState(review: ShowReviewData) {
    try {
      review.isLikedByUser = !review.isLikedByUser;
      if (review.isLikedByUser) {
        await this.showService.likeShowReview(review.id);
        review.numLikes++;
      } else {
        await this.showService.unlikeShowReview(review.id);
        review.numLikes--;
      }
    } catch (error) {
      console.error(error);
    }
  }
}
