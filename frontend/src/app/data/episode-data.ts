export class EpisodeData {
  name: string;
  showName: string;
  overview: string;
  imdbRating: string;
  imdbVotes: number;
  runtime: number;
  airDate: Date;
  stillPath: string;

  constructor(jsonObject: { [key: string]: any }) {
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
