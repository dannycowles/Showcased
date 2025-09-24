import {Component, Input} from '@angular/core';
import {RouterLink} from '@angular/router';
import {ProfileReviewData} from '../../data/types';
import {NgOptimizedImage} from '@angular/common';
import {ReviewType} from '../../data/enums';
import {UtilsService} from '../../services/utils.service';

@Component({
  selector: 'app-recent-reviews',
  imports: [RouterLink, NgOptimizedImage],
  templateUrl: './recent-reviews.component.html',
  styleUrl: './recent-reviews.component.css',
  standalone: true,
})
export class RecentReviewsComponent {
  @Input({ required: true }) reviews: ProfileReviewData[];

  constructor(public utilsService: UtilsService) {}

  protected readonly ReviewType = ReviewType;
}
