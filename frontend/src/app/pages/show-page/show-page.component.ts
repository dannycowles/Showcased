import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {ShowData} from '../../data/show-data';
import {ReviewData} from '../../data/review-data';

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

  constructor(private route: ActivatedRoute) {
  }

  async ngOnInit() {
    this.showId = this.route.snapshot.params['id'];

    // Retrieve show data from backend
    try {
      let response = await fetch(`http://localhost:8080/show/${this.showId}`);

      let data = await response.json();
      this.show = new ShowData(data);
    } catch (error) {
      console.error(error);
    }

    // Retrieve review data for show from backend
    try {
      let response = await fetch(`http://localhost:8080/show/${this.showId}/reviews`);

      let data = await response.json();
      this.reviews = data.map((review: {}) => new ReviewData(review));
    } catch (error) {
      console.error(error);
    }
  }

  seasonSelected(seasonNumber:string) {
    window.location.href = `${window.location.pathname}/season/${seasonNumber}`;
  }
}
