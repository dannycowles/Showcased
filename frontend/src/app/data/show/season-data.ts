import {SeasonEpisode} from './season-episode';

export class SeasonData {
  overview: string;
  seasonNumber: number;
  posterPath: string;
  episodes: SeasonEpisode[];

  constructor(jsonObject: { [key: string]: any }) {
    this.overview = jsonObject['overview'];
    this.seasonNumber = jsonObject['season_number'];
    this.posterPath = jsonObject['poster_path'];

    this.episodes = jsonObject['episodes'].map((episode: {}) => {
      return new SeasonEpisode(episode);
    })
  }
}


