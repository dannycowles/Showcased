import {SearchResultData} from './search-result-data';

export class TrendingShowsData {
  readonly page: number;
  readonly results: SearchResultData[];
  readonly totalPages: number;
  readonly totalResults: number;

  constructor(jsonObject: {[key: string]: any}) {
    this.page = jsonObject['page'];
    this.results = jsonObject['results'].map((show: {}) => {
      return new SearchResultData(show);
    });
    this.totalPages = jsonObject['totalPages'];
    this.totalResults = jsonObject['totalResults'];
  }
}
