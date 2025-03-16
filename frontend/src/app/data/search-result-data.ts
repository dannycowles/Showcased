export class SearchResultData {
  id: number;
  name: string;
  firstAirDate: number;
  lastAirDate: number;
  posterPath: string;

  constructor(jsonObject: { [key: string]: any }) {
    this.id = jsonObject['id'];
    this.name = jsonObject['name'];
    this.firstAirDate = jsonObject['first_air_date'];
    this.lastAirDate = jsonObject['last_air_date'];

    if (jsonObject['poster_path'] === "default") {
      this.posterPath = "no-poster.svg"
    } else {
      this.posterPath = jsonObject['poster_path'];
    }
  }
}
