import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {ShowData} from '../../data/show-data';

@Component({
  selector: 'app-show-page',
  templateUrl: './show-page.component.html',
  styleUrl: './show-page.component.css',
  standalone: false
})
export class ShowPageComponent implements OnInit {
  showId: number;
  show: ShowData;
  selectedSeason: string;

  constructor(private route: ActivatedRoute) {
  }

  async ngOnInit() {
    this.showId = this.route.snapshot.params['id'];

    try {
      let response = await fetch(`http://localhost:8080/show/${this.showId}`);

      let data = await response.json();
      this.show = new ShowData(data);
    } catch (error) {
      console.error(error);
    }

    console.log(this.show);
  }

  seasonSelected() {
    window.location.href = `${window.location.pathname}/season/${this.selectedSeason}`;
  }
}
