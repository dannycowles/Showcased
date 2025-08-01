import {GenreData} from './genre-data';
import {CreatorData} from './creator-data';
import {CastData} from './cast-data';
import {WatchOptionData} from './watch-option-data';
import {SearchResultData} from '../search-result-data';

export interface ShowData {
  readonly id: number;
  readonly imdbId: string;
  readonly imdbRating: number;
  readonly imdbVotes: number;
  readonly backdropPath: string;
  readonly posterPath: string;
  readonly title: string;
  readonly awards: string;
  readonly tagline: string;
  readonly plot: string;
  readonly rating: string;
  readonly averageRuntime: string;
  readonly startYear: number;
  readonly endYear: number;
  readonly numEpisodes: number;
  readonly numSeasons: number;
  readonly genres: GenreData[];
  readonly creators: CreatorData[];
  readonly cast: CastData[];
  readonly streamingOptions: WatchOptionData[];
  readonly buyOptions: WatchOptionData[];
  readonly recommendations: SearchResultData[];
  readonly trailerPath: string;
  isOnWatchlist: boolean;
  isOnWatchingList: boolean;
  isOnRankingList: boolean;
}
