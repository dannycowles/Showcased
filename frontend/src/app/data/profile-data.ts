import {ReviewData} from './review-data';
import {UtilsService} from '../services/utils.service';
import {WatchlistData} from './lists/watchlist-data';
import {WatchingData} from './lists/watching-data';
import {ShowRankingData} from './lists/show-ranking-data';
import {EpisodeRankingData} from './lists/episode-ranking-data';

export class ProfileData {
  username: string; // TODO
  profilePicture: string;
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
    this.profilePicture = jsonObject['profilePicture'];

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
