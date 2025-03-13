import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-show-page',
  templateUrl: './show-page.component.html',
  styleUrl: './show-page.component.css',
  standalone: false
})
export class ShowPageComponent implements OnInit {
  showId: number;

  constructor(private route: ActivatedRoute) {}

  ngOnInit() {
    this.showId = this.route.snapshot.params['id'];
  }

}
