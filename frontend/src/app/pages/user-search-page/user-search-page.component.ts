import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-user-search-page',
  templateUrl: './user-search-page.component.html',
  styleUrl: './user-search-page.component.css',
  standalone: false
})
export class UserSearchPageComponent implements OnInit {
  searchString: string;

  constructor() {};

  ngOnInit() {};

  search() {
    console.log(this.searchString);
  }

}
