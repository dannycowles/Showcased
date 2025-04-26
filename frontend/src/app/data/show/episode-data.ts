export class EpisodeData {
  readonly id: number;
  readonly name: string;
  readonly showName: string;
  readonly overview: string;
  readonly imdbRating: string;
  readonly imdbVotes: number;
  readonly runtime: number;
  readonly airDate: Date;
  readonly stillPath: string;

  constructor(jsonObject: { [key: string]: any }) {
    this.id = jsonObject['id'];
    this.name = jsonObject['name'];
    this.showName = jsonObject['showTitle'];

    if (jsonObject['overview'] === "N/A" || !jsonObject['overview']) {
      this.overview = "Unknown";
    } else {
      this.overview = jsonObject['overview'];
    }

    if (jsonObject['imdbRating'] === "N/A" || !jsonObject['imdbRating']) {
      this.imdbRating = "Unknown";
    } else {
      this.imdbRating = jsonObject['imdbRating'];
    }

    this.imdbVotes = jsonObject['imdbVotes'];
    this.runtime = jsonObject['runtime'];
    this.airDate = new Date(jsonObject['air_date']);
    this.stillPath = jsonObject['still_path'];
  }
}
