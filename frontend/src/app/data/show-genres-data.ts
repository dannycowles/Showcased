import {GenreData} from './show/genre-data';

export class ShowGenresData {
  readonly genres: GenreData[];

  constructor(jsonObject: {[key: string]: any}) {
    this.genres = jsonObject['genres'].map((genre: {}) => {
      return new GenreData(genre);
    });
  }
}
