import {Component, OnInit} from '@angular/core';
import {SearchResultData} from '../../data/search-result-data';

@Component({
  selector: 'app-search-page',
  templateUrl: './search-page.component.html',
  styleUrl: './search-page.component.css',
  standalone: false
})
export class SearchPageComponent implements OnInit {
  searchString: string;
  searchResults: SearchResultData[];

  constructor() {}

  ngOnInit() {
  }

  async search() {

    // As long as the search is not blank, send a request to the backend search endpoint
    // to retrieve results
    if (this.searchString) {
      try {
        let response = await fetch(`http://localhost:8080/show/search?query=${encodeURIComponent(this.searchString)}`);

        let data = await response.json();
        this.searchResults = data.map((result: SearchResultData) => new SearchResultData(result));
      } catch (error) {
        console.error(error);
      }
    }
    console.log(this.searchResults);

    const result = {
      "id": "48477",
      "name": "Several Questions That Makes Us Happy",
      "firstAirDate": "",
      "lastAirDate": "",
      "posterPath": "no-poster.svg"
    };

    if (result.firstAirDate === "" && result.lastAirDate === "") {
      console.log("Air Dates Unknown");
    } else if (result.firstAirDate === result.lastAirDate) {
      console.log(result.firstAirDate);
    } else {
      console.log(result.firstAirDate + " - " + result.lastAirDate);
    }

  }

}
