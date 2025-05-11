import {Component, OnInit} from '@angular/core';
import {ShowService} from '../../../services/show.service';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-discover-genres-page',
  templateUrl: './discover-genres-page.component.html',
  styleUrl: './discover-genres-page.component.css',
  standalone: false
})
export class DiscoverGenresPageComponent implements OnInit {


  constructor(showService: ShowService,
              route: ActivatedRoute) {

  };

  ngOnInit() {};

}
