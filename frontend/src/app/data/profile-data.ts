import {WatchlistData} from './watchlist-data';

export class ProfileData {
  username: string;
  profilePicture: string; // TODO
  watchlistTop: WatchlistData[];

  constructor(jsonObject: { [key : string]: any }) {
    this.username = jsonObject['username'];

    this.watchlistTop = jsonObject['watchlistTop'].map(( show: {} ) => {
      return new WatchlistData(show);
    });
  }
}
