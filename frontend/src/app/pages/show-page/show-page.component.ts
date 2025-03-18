import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {ShowData} from '../../data/show-data';
import {ReviewData} from '../../data/review-data';
import {ShowService} from '../../services/show.service';
import {ProfileService} from '../../services/profile.service';
import {ToastDisplayService} from '../../services/toast.service';
import {UtilsService} from '../../services/utils.service';

@Component({
  selector: 'app-show-page',
  templateUrl: './show-page.component.html',
  styleUrl: './show-page.component.css',
  standalone: false
})
export class ShowPageComponent implements OnInit {
  showId: number;
  show: ShowData;
  reviews: ReviewData[];

  constructor(private route: ActivatedRoute,
              private showService: ShowService,
              private profileService: ProfileService,
              private toastService: ToastDisplayService,
              public utilsService: UtilsService) {
  }

  async ngOnInit() {
    this.showId = this.route.snapshot.params['id'];

    // Retrieve show data from backend
    try {
      this.show = await this.showService.fetchShowDetails(this.showId);
    } catch (error) {
      console.error(error);
    }

    // Retrieve reviews for show from backend
    try {
      this.reviews = await this.showService.fetchShowReviews(this.showId);
    } catch (error) {
      console.error(error);
    }
  }

  seasonSelected(seasonNumber:string) {
    window.location.href = `${window.location.pathname}/season/${seasonNumber}`;
  }

  // Adds the current show to the user's watchlist
  async addShowToWatchlist() {
    try {
      let data = {
        "showId": this.showId,
        "title": this.show.name,
        "posterPath": this.show.posterPath
      };

      // Display a toast that confirms the show was successfully added
      this.toastService.addToWatchlistToast(this.show.name);

      //await this.profileService.addShowToWatchlist(data);
    } catch (error) {
      console.error(error);
    }
  }

  // Adds the current show to the user's currently watching list
  async addShowToWatchingList() {
    try {
      let data = {
        "showId": this.showId,
        "title": this.show.name,
        "posterPath": this.show.posterPath
      };

      // Display a toast that confirms the show was successfully added
      this.toastService.addToWatchingListToast(this.show.name);

      //await this.profileService.addShowToWatchingList(data);
    } catch (error) {
      console.error(error);
    }
  }

  // Adds the current show to the user's ranking list
  async addShowToRankingList() {
    try {
      let data = {
        "showId": this.showId,
        "title": this.show.name,
        "posterPath": this.show.posterPath
      };

      // Display a toast that confirms the show was successfully added
      this.toastService.addToShowRankingToast(this.show.name);

      //await this.profileService.addShowToRankingList(data);
    } catch (error) {
      console.error(error);
    }
  }
}
