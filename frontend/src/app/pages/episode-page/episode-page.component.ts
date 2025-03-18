import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {ShowService} from '../../services/show.service';

@Component({
  selector: 'app-episode-page',
  templateUrl: './episode-page.component.html',
  styleUrl: './episode-page.component.css',
  standalone: false
})
export class EpisodePageComponent implements OnInit {
  showId: number;
  seasonNumber: number;
  episodeNumber: number;

  constructor(private route: ActivatedRoute,
              private showService: ShowService) { }

  ngOnInit() {
    this.showId = this.route.snapshot.params['id'];
    this.seasonNumber = this.route.snapshot.params['seasonNumber'];
    this.episodeNumber = this.route.snapshot.params['episodeNumber'];

  }
}
