import {SeasonEpisodeFull} from './season-episode';

export interface SeasonData {
  readonly id: number;
  readonly showTitle: string;
  readonly airYear: string;
  readonly overview: string;
  readonly seasonNumber: number;
  readonly posterPath: string;
  readonly episodes: SeasonEpisodeFull[];
  onRankingList: boolean;
}
