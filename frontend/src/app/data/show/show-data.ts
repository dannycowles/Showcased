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
  readonly backdrop_path: string;
  readonly poster_path: string;
  readonly name: string;
  readonly awards: string;
  readonly tagline: string;
  readonly plot: string;
  readonly rating: string;
  readonly average_runtime: string;
  readonly startYear: number;
  readonly endYear: number;
  readonly numEpisodes: number;
  readonly numSeasons: number;
  readonly genres: GenreData[];
  readonly creators: CreatorData[];
  readonly cast: CastData[];
  readonly streamOptions: WatchOptionData[];
  readonly buyOptions: WatchOptionData[];
  readonly recommendations: SearchResultData[];
  onWatchlist: boolean;
  onWatchingList: boolean;
  onRankingList: boolean;

/*  get genreString():string {
    let returnStr:string = "";
    for (let i = 0; i < this.genres.length; i++) {
      returnStr += this.genres[i].name;

      if (i < this.genres.length - 1) {
        returnStr += ", ";
      }
    }
    return returnStr;
  }*/
}
