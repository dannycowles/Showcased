import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-season-page',
  templateUrl: './season-page.component.html',
  styleUrl: './season-page.component.css',
  standalone: false
})
export class SeasonPageComponent implements OnInit {
  showId: number;

  constructor(private route: ActivatedRoute) {}

  ngOnInit() {
    this.showId = this.route.snapshot.params['id'];
  }

}
