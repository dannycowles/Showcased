import {SeasonEpisode} from './season-episode';

export class SeasonData {
  readonly id: number;
  readonly showTitle: string;
  readonly overview: string;
  readonly seasonNumber: number;
  readonly posterPath: string;
  readonly episodes: SeasonEpisode[];
  onRankingList: boolean;

  constructor(jsonObject: { [key: string]: any }) {
    this.id = jsonObject['id'];
    this.showTitle = jsonObject['showTitle'];
    this.overview = jsonObject['overview'];
    this.seasonNumber = jsonObject['season_number'];
    this.posterPath = jsonObject['poster_path'];

    this.episodes = jsonObject['episodes'].map((episode: {}) => {
      return new SeasonEpisode(episode);
    });
    this.onRankingList = jsonObject['onRankingList'];
  }
}


