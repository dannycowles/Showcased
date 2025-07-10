import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {ShowService} from '../../../services/show.service';
import {EpisodeData} from '../../../data/show/episode-data';
import {ProfileService} from '../../../services/profile.service';
import {ToastDisplayService} from '../../../services/toast.service';
import {UtilsService} from '../../../services/utils.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {AddReviewModalComponent} from '../../../components/add-review-modal/add-review-modal.component';
import {EpisodeReviewData} from '../../../data/reviews-data';
import {ReviewType} from '../../../data/enums';


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

  constructor(private route: ActivatedRoute,
              private showService: ShowService,
              private profileService: ProfileService,
              private toastService: ToastDisplayService,
              public utilsService: UtilsService,
              private modalService: NgbModal) {
    this.showId = this.route.snapshot.params['id'];
    this.seasonNumber = this.route.snapshot.params['seasonNumber'];
    this.episodeNumber = this.route.snapshot.params['episodeNumber'];
  }

  async ngOnInit() {
    // Fetch episode details from the backend
    try {
      this.episode = await this.showService.fetchEpisodeDetails(this.showId, this.seasonNumber, this.episodeNumber);
      this.reviews = await this.showService.fetchEpisodeReviews(this.episode.id);
    } catch (error) {
      console.error(error);
    }
  }

  // Add the episode to the user's ranking list
  async addToRankingList() {
    try {
      const data = {
        "id": this.episode.id,
        "showId": this.showId,
        "showTitle": this.episode.showTitle,
        "episodeTitle": this.episode.episodeTitle,
        "season": this.seasonNumber,
        "episode": this.episodeNumber,
        "posterPath": this.episode.stillPath
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
    const addReviewModalRef = this.modalService.open(AddReviewModalComponent, {
      ariaLabelledBy: "addReviewModal",
      centered: true
    });
    addReviewModalRef.componentInstance.modalTitle = `Add New Review for ${this.episode.showTitle} S${this.seasonNumber} E${this.episodeNumber}: ${this.episode.episodeTitle}`;

    const result = await addReviewModalRef.result;
    const newReview = {
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
      await this.showService.addEpisodeReview(this.episode.id, newReview);
    } catch (error) {
      console.error(error);
    }
  }
}
