import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ShowService} from '../../../services/show.service';
import {EpisodeData} from '../../../data/show/episode-data';
import {ProfileService} from '../../../services/profile.service';
import {ToastDisplayService} from '../../../services/toast.service';
import {UtilsService} from '../../../services/utils.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {AddReviewModalComponent} from '../../../components/add-review-modal/add-review-modal.component';
import {EpisodeReviewData} from '../../../data/reviews-data';
import {ReviewType} from '../../../data/enums';
import {AuthenticationService} from '../../../services/auth.service';
import {AddEpisodeReviewDto} from '../../../data/dto/add-review-dto';
import {AddToEpisodeRankingList} from '../../../data/dto/add-to-list-dto';

@Component({
  selector: 'app-episode-page',
  templateUrl: './episode-page.component.html',
  styleUrl: './episode-page.component.css',
  standalone: false
})
export class EpisodePageComponent implements OnInit {
  readonly showId: number;
  readonly seasonNumber: number;
  readonly episodeNumber: number;
  episode: EpisodeData;
  reviews: EpisodeReviewData[];
  readonly ReviewType = ReviewType;
  isLoggedIn: boolean = false;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private showService: ShowService,
              private profileService: ProfileService,
              private toastService: ToastDisplayService,
              public utilsService: UtilsService,
              private modalService: NgbModal,
              private authService: AuthenticationService) {
    this.showId = this.route.snapshot.params['id'];
    this.seasonNumber = this.route.snapshot.params['seasonNumber'];
    this.episodeNumber = this.route.snapshot.params['episodeNumber'];
  }

  async ngOnInit() {
    // Fetch episode details from the backend
    try {
      this.episode = await this.showService.fetchEpisodeDetails(this.showId, this.seasonNumber, this.episodeNumber);
      this.reviews = await this.showService.fetchEpisodeReviews(this.episode.id);
      this.isLoggedIn = await this.authService.loginStatus();
    } catch (error) {
      console.error(error);
    }
  }

  // Add the episode to the user's ranking list
  async addToRankingList() {
    try {
      const data: AddToEpisodeRankingList = {
        episodeId: this.episode.id,
        showId: this.showId,
        showTitle: this.episode.showTitle,
        episodeTitle: this.episode.episodeTitle,
        season: this.seasonNumber,
        episode: this.episodeNumber,
        posterPath: this.episode.stillPath
      };

      const response = await this.profileService.addEpisodeToRankingList(data);
      if (response.ok) {
        this.episode.isOnRankingList = true;
        this.toastService.addToEpisodeRankingToast(this.episode.episodeTitle);
      }
    } catch (error) {
    console.error(error);}
  }

  async removeFromRankingList() {
    try {
      const response = await this.profileService.removeEpisodeFromRankingList(this.episode.id);

      if (response.ok) {
        this.episode.isOnRankingList = false;
      }
    } catch (error) {
      console.error(error);
    }
  }

  async openAddReviewModal() {
    if (!this.isLoggedIn){
      this.router.navigate(['/login']);
      return;
    }

    const addReviewModalRef = this.modalService.open(AddReviewModalComponent, {
      ariaLabelledBy: "addReviewModal",
      centered: true
    });
    addReviewModalRef.componentInstance.modalTitle = `Add New Review for ${this.episode.showTitle} S${this.seasonNumber} E${this.episodeNumber}: ${this.episode.episodeTitle}`;

    const result = await addReviewModalRef.result;
    const reviewData: AddEpisodeReviewDto = {
      rating: result.rating,
      showId: this.showId,
      showTitle: this.episode.showTitle,
      episodeTitle: this.episode.episodeTitle,
      season: this.seasonNumber,
      episode: this.episodeNumber,
      commentary: result.commentary,
      containsSpoilers: result.containsSpoilers,
      posterPath: this.episode.stillPath
    };

    try {
      const response = await this.showService.addEpisodeReview(this.episode.id, reviewData);

      if (response.ok) {
        const newReview: EpisodeReviewData = await response.json();
        this.reviews.unshift(newReview);
      }
    } catch (error) {
      console.error(error);
    }
  }
}
