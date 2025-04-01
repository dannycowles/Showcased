import {WatchlistData} from './watchlist-data';
import {WatchingData} from './watching-data';
import {ShowRankingData} from './show-ranking-data';
import {EpisodeRankingData} from './episode-ranking-data';

export class ProfileData {
  username: string; // TODO
  profilePicture: string; // TODO
  watchlistTop: WatchlistData[];
  watchingTop: WatchingData[];
  showRankingTop: ShowRankingData[];
  episodeRankingTop: EpisodeRankingData[];

  constructor(jsonObject: { [key : string]: any }) {
    this.username = jsonObject['username'];

    this.watchlistTop = jsonObject['watchlistTop'].map(( show: {} ) => {
      return new WatchlistData(show);
    });

    this.watchingTop = jsonObject['watchingTop'].map(( show: {} ) => {
      return new WatchingData(show);
    });

    this.showRankingTop = jsonObject['showRankingTop'].map(( show: {} ) => {
      return new ShowRankingData(show);
    });

    this.episodeRankingTop = jsonObject['episodeRankingTop'].map(( show: {} ) => {
      return new EpisodeRankingData(show);
    });
  }
}
