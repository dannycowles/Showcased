import {ProfileEpisodeReviewData, ProfileSeasonReviewData, ProfileShowReviewData} from './profile-reviews-data';
import {EpisodeReviewData, SeasonReviewData, ShowReviewData} from './reviews-data';
import {ShowListData} from './lists/show-list-data';
import {ShowRankingData} from './lists/show-ranking-data';

export type ProfileReviewData = ProfileShowReviewData | ProfileEpisodeReviewData | ProfileSeasonReviewData;

export type ReviewData = ShowReviewData | EpisodeReviewData | SeasonReviewData;

export type ProfileShow = ShowListData | ShowRankingData;
