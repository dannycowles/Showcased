import {SearchResultData} from '../search-result-data';

export interface ResultPageData {
  readonly page: number;
  readonly results: SearchResultData[];
  readonly totalPages: number;
  readonly totalResults: number;
}

export interface GenreResultPageData extends ResultPageData {
  readonly genre: string;
}
