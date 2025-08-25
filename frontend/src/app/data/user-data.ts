import {ShowRankingData} from './lists/show-ranking-data';
import {EpisodeRankingData} from './lists/episode-ranking-data';
import {SeasonRankingData} from './lists/season-ranking-data';
import {ShowListData} from './lists/show-list-data';
import {UserHeaderData} from './user-header-data';
import {CharacterRankingsData} from './character-rankings-data';
import {DynamicRankingData} from './lists/dynamic-ranking-data';
import {ProfileReviewData} from './types';
import {CollectionData} from './collection-data';

export interface UserData {
  readonly headerData: UserHeaderData
  readonly watchlistTop: ShowListData[];
  readonly watchingTop: ShowListData[];
  readonly showRankingTop: ShowRankingData[];
  readonly episodeRankingTop: EpisodeRankingData[];
  readonly seasonRankingTop: SeasonRankingData[];
  readonly characterRankings: CharacterRankingsData;
  readonly dynamicRankingTop: DynamicRankingData[];
  readonly hasMoreWatchlist: boolean;
  readonly hasMoreWatching: boolean;
  readonly hasMoreShowRanking: boolean;
  readonly hasMoreEpisodeRanking: boolean;
  readonly reviews: ProfileReviewData[];
  readonly collections: CollectionData[];
}
