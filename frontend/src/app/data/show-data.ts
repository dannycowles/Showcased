import {GenreData} from './genre-data';
import {CreatorData} from './creator-data';
import {CastData} from './cast-data';
import {WatchOptionData} from './watch-option-data';

export class ShowData {
  id: number;
  imdbId: string;
  imdbRating: number;
  imdbVotes: number;
  backdropPath: string;
  posterPath: string;
  name: string;
  awards: string;
  tagline: string;
  plot: string;
  rating: string;
  averageRuntime: string;
  firstAirDate: number;
  lastAirDate: number;
  numEpisodes: number;
  numSeasons: number;
  genres: GenreData[];
  creators: CreatorData[];
  cast: CastData[];
  streamingOptions: WatchOptionData[];
  buyOptions: WatchOptionData[];

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
    })
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




