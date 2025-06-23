import {ShowRankingData} from './lists/show-ranking-data';
import {EpisodeRankingData} from './lists/episode-ranking-data';
import {SeasonRankingData} from './lists/season-ranking-data';
import {CharacterRankingData} from './lists/character-ranking-data';
import {UserSocialData} from './user-social-data';
import {ShowListData} from './lists/show-list-data';
import {EpisodeReviewData, ShowReviewData} from './reviews-data';

export interface ProfileData {
  readonly username: string;
  profilePicture: string;
  bio: string;
  numFollowers: number;
  numFollowing: number;
  socialAccounts: UserSocialData[];
  readonly watchlistTop: ShowListData[];
  readonly watchingTop: ShowListData[];
  readonly showRankingTop: ShowRankingData[];
  readonly episodeRankingTop: EpisodeRankingData[];
  readonly seasonRankingTop: SeasonRankingData[];
  readonly protagonistRankingTop: CharacterRankingData[];
  readonly deuteragonistRankingTop: CharacterRankingData[];
  readonly antagonistRankingTop: CharacterRankingData[];
  readonly tritagonistRankingTop: CharacterRankingData[];
  readonly sideCharacterRankingTop: CharacterRankingData[];
  readonly moreWatchlist: boolean;
  readonly moreWatching: boolean;
  readonly moreShowRanking: boolean;
  readonly moreEpisodeRanking: boolean;
  readonly showReviews: ShowReviewData[];
  readonly episodeReviews: EpisodeReviewData[];
  following: boolean;
  readonly ownProfile: boolean;
}
