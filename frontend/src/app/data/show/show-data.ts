import {GenreData} from './genre-data';
import {CreatorData} from './creator-data';
import {CastData} from './cast-data';
import {WatchOptionData} from './watch-option-data';
import {SearchResultData} from '../search-result-data';

export class ShowData {
  readonly id: number;
  readonly imdbId: string;
  readonly imdbRating: number;
  readonly imdbVotes: number;
  readonly backdropPath: string;
  readonly posterPath: string;
  readonly name: string;
  readonly awards: string;
  readonly tagline: string;
  readonly plot: string;
  readonly rating: string;
  readonly averageRuntime: string;
  readonly firstAirDate: number;
  readonly lastAirDate: number;
  readonly numEpisodes: number;
  readonly numSeasons: number;
  readonly genres: GenreData[];
  readonly creators: CreatorData[];
  readonly cast: CastData[];
  readonly streamingOptions: WatchOptionData[];
  readonly buyOptions: WatchOptionData[];
  readonly recommendations: SearchResultData[];
  onWatchlist: boolean;
  onWatchingList: boolean;
  onRankingList: boolean;

  constructor(jsonObject: { [key: string]: any }) {
    this.id = jsonObject['id'];
    this.imdbId = jsonObject['imdbId'];
    this.imdbRating = jsonObject['imdbRating'];
    this.imdbVotes = jsonObject['imdbVotes'];
    this.awards = jsonObject['awards'];

    if (jsonObject['backdrop_path'] === "default") {
      this.backdropPath = "no-backdrop.svg";
    } else {
      this.backdropPath = jsonObject['backdrop_path'];
    }

    if (jsonObject['poster_path'] === "default") {
      this.posterPath = "no-poster-full.svg";
    } else {
      this.posterPath = jsonObject['poster_path'];
    }

    this.name = jsonObject['name'];
    this.tagline = jsonObject['tagline'];
    this.plot = jsonObject['plot'];
    this.rating = jsonObject['rating'];
    this.averageRuntime = jsonObject['average_runtime'];
    this.firstAirDate = jsonObject['first_air_date'];
    this.lastAirDate = jsonObject['last_air_date'];
    this.numEpisodes = jsonObject['number_of_episodes'];
    this.numSeasons = jsonObject['number_of_seasons'];

    this.genres = jsonObject['genres'].map((genre: {} ) => {
      return new GenreData(genre);
    });

    this.creators = jsonObject['created_by'].map((creator: {} ) => {
      return new CreatorData(creator);
    });

    this.cast = jsonObject['cast'].map((cast: {} ) => {
      return new CastData(cast);
    });

    this.streamingOptions = jsonObject['streamOptions'].map((option: {}) => {
      return new WatchOptionData(option);
    });

    this.buyOptions = jsonObject['buyOptions'].map((option: {}) => {
      return new WatchOptionData(option);
    });

    this.recommendations = jsonObject['recommendations'].map((recommendation: {}) => {
      return new SearchResultData(recommendation);
    });

    this.onWatchlist = jsonObject['onWatchlist'];
    this.onWatchingList = jsonObject['onWatchingList'];
    this.onRankingList = jsonObject['onRankingList'];
  }

  get genreString():string {
    let returnStr:string = "";
    for (let i = 0; i < this.genres.length; i++) {
      returnStr += this.genres[i].name;

      if (i < this.genres.length - 1) {
        returnStr += ", ";
      }
    }
    return returnStr;
  }
}




