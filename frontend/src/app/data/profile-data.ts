import {WatchlistData} from './watchlist-data';
import {WatchingData} from './watching-data';
import {ShowRankingData} from './show-ranking-data';
import {EpisodeRankingData} from './episode-ranking-data';
import {ReviewData} from './review-data';
import {UtilsService} from '../services/utils.service';

export class ProfileData {
  username: string; // TODO
  profilePicture: string; // TODO
  watchlistTop: WatchlistData[];
  moreWatchlist: boolean;
  watchingTop: WatchingData[];
  moreWatching: boolean;
  showRankingTop: ShowRankingData[];
  moreShowRanking: boolean;
  episodeRankingTop: EpisodeRankingData[];
  moreEpisodeRanking: boolean;
  reviews: ReviewData[];

  constructor(jsonObject: { [key : string]: any }) {
    this.username = jsonObject['username'];

    this.watchlistTop = jsonObject['watchlistTop'].map(( show: {} ) => {
      return new WatchlistData(show);
    });
    this.moreWatchlist = jsonObject['moreWatchlist'];

    this.watchingTop = jsonObject['watchingTop'].map(( show: {} ) => {
      return new WatchingData(show);
    });
    this.moreWatching = jsonObject['moreWatching'];

    this.showRankingTop = jsonObject['showRankingTop'].map(( show: {} ) => {
      return new ShowRankingData(show);
    });
    this.moreShowRanking = jsonObject['moreShowRanking'];

    this.episodeRankingTop = jsonObject['episodeRankingTop'].map(( episode: {} ) => {
      return new EpisodeRankingData(episode);
    });
    this.moreEpisodeRanking = jsonObject['moreEpisodeRanking'];

    this.reviews = jsonObject['reviews'].map(( review: {} ) => {
      return new ReviewData(review, new UtilsService());
    });
  }
}
