import {Component, OnInit} from '@angular/core';
import {ShowService} from '../../../services/show.service';
import {ShowGenresData} from '../../../data/show-genres-data';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-discover-page',
  templateUrl: './discover-page.component.html',
  styleUrl: './discover-page.component.css',
  imports: [RouterLink],
  standalone: true,
})
export class DiscoverPageComponent implements OnInit {
  showGenres: ShowGenresData;

  constructor(private showService: ShowService) {}

  async ngOnInit() {
    // Retrieve the show genres from the backend
    try {
      this.showGenres = await this.showService.fetchShowGenres();
    } catch (error) {
      console.error(error);
    }
  }
}
