export class EpisodeData {
  name: string;
  overview: string;
  imdbRating: string;
  imdbVotes: number;
  runtime: number;
  airDate: Date;
  stillPath: string;

  constructor(jsonObject: { [key: string]: any }) {
    this.name = jsonObject['name'];

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
    this.airDate = jsonObject['air_date'];
    this.stillPath = jsonObject['still_path'];
  }
}
