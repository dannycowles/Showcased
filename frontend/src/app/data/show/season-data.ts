import {SeasonEpisode} from './season-episode';

export interface SeasonData {
  readonly id: number;
  readonly showTitle: string;
  readonly overview: string;
  readonly seasonNumber: number;
  readonly posterPath: string;
  readonly episodes: SeasonEpisode[];
  onRankingList: boolean;
}
