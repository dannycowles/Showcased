export class SeasonEpisode {
  name: string;
  overview: string;
  imdbRating: string;
  airDate: Date;
  episodeNumber: number;
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

    this.airDate = new Date(jsonObject['air_date']);
    this.episodeNumber = jsonObject['episode_number'];
    this.stillPath = jsonObject['still_path'];
  }
}
