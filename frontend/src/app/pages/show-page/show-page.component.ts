import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {ShowData} from '../../data/show-data';
import {ReviewData} from '../../data/review-data';
import {ShowService} from '../../services/show.service';
import {ProfileService} from '../../services/profile.service';
import {ToastDisplayService} from '../../services/toast.service';
import {UtilsService} from '../../services/utils.service';
import {AuthenticationService} from '../../services/auth.service';
import $ from 'jquery';
import 'jquery-serializejson';

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
              public utilsService: UtilsService,
              private authService: AuthenticationService) {
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
      let response = await this.profileService.addShowToWatchlist(data);

      if (response.status == 201) {
        // Display a toast that confirms the show was successfully added
        this.toastService.addToWatchlistToast(this.show.name);
      }
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
      let response = await this.profileService.addShowToWatchingList(data);

      if (response.status == 201) {
        // Display a toast that confirms the show was successfully added
        this.toastService.addToWatchingListToast(this.show.name);
      }
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
      let response = await this.profileService.addShowToRankingList(data);

      if (response.status == 201) {
        // Display a toast that confirms the show was successfully added
        this.toastService.addToShowRankingToast(this.show.name);
      }
    } catch (error) {
      console.error(error);
    }
  }

  // Likes a show review
  async likeShowReview(reviewId: number) {
    try {
      await this.showService.likeShowReview(reviewId);
    } catch (error) {
      console.error(error);
    }
  }

  // If the user is not logged in they are redirected, else the review modal will appear
  async addReviewPressed() {
    try {
      let loginStatus = await this.authService.loginStatus();

      if (!loginStatus) {
        window.location.href = "login";
      }
    } catch(error) {
      console.error(error);
    }
  }

  // Triggered when the user enters or pastes into the review commentary box, computes and display the remaining characters
  displayCommentaryCharactersLeft() {
    let commentaryTextArea = document.getElementById("commentaryInput") as HTMLTextAreaElement ;
    document.getElementById("commentaryHelpBlock").innerText = String(5000 - commentaryTextArea.value.length) + " characters left";
  }

  // Triggered when the user submits a review, it will format and send the data to the backend
  async reviewSubmitted() {
    // @ts-ignore
    let reviewForm = $("#review-form").serializeJSON();
    let data = {
      "rating": reviewForm["rating"],
      "showTitle": this.show.name,
      "commentary": reviewForm["commentary"],
      "containsSpoilers": "spoilers" in reviewForm
    };

    try {
      await this.showService.addShowReview(this.showId, data);
    } catch(error) {
      console.error(error);
    }
  }

}
