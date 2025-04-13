export class SearchResultData {
  readonly id: number;
  readonly name: string;
  readonly firstAirDate: number;
  readonly lastAirDate: number;
  readonly posterPath: string;

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
