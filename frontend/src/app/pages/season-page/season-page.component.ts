import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {SeasonData} from '../../data/season-data';
import {ShowService} from '../../services/show.service';

@Component({
  selector: 'app-season-page',
  templateUrl: './season-page.component.html',
  styleUrl: './season-page.component.css',
  standalone: false
})
export class SeasonPageComponent implements OnInit {
  showId: number;
  seasonNumber: number;
  season: SeasonData

  constructor(private route: ActivatedRoute,
              private showService: ShowService) {}

  async ngOnInit() {
    this.showId = this.route.snapshot.params['id'];
    this.seasonNumber = this.route.snapshot.params['seasonNumber'];

    // Retrieve season details from backend
    try {
      this.season = await this.showService.fetchSeasonDetails(this.showId, this.seasonNumber);

      console.log(this.season);

    } catch(error) {
      console.error(error);
    }
  }

  episodeSelected(episodeNumber: number) {
    window.location.href = `${window.location.pathname}/episode/${episodeNumber}`;
  }

}
