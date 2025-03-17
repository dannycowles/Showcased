export class SeasonEpisode {
  name: string;
  plot: string;
  imdbRating: number;
  airDate: Date;
  episodeNumber: number;
  stillPath: string;

  constructor(jsonObject: { [key: string]: any }) {
    this.name = jsonObject['name'];
    this.plot = jsonObject['plot'];
    this.imdbRating = jsonObject['imdbRating'];
    this.airDate = jsonObject['air_date'];
    this.episodeNumber = jsonObject['episode_number'];
    this.stillPath = jsonObject['still_path'];
  }
}
