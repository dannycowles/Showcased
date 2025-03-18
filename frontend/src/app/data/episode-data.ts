export class EpisodeData {
  name: string;
  plot: string;
  imdbRating: string;
  imdbVotes: number;
  runtime: number;
  airDate: Date;
  stillPath: string;

  constructor(jsonObject: { [key: string]: any }) {
    this.name = jsonObject['name'];
    this.plot = jsonObject['plot'];

    if (jsonObject['imdbRating'] === "N/A" || !jsonObject['imdbRating']) {
      this.imdbRating = "Unknown";
    } else {
      this.imdbRating = jsonObject['imdbRating'];
    }

    this.imdbVotes = jsonObject['imdbVotes'];
    this.runtime = jsonObject['runtime'];
    this.airDate = jsonObject['air_date'];
    this.stillPath = jsonObject['still_path'];
  }
}
