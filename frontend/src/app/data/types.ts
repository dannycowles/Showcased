import {ProfileEpisodeReviewData, ProfileShowReviewData} from './profile-reviews-data';
import {EpisodeReviewData, ShowReviewData} from './reviews-data';
import {ShowListData} from './lists/show-list-data';
import {ShowRankingData} from './lists/show-ranking-data';

export type ProfileReviewData = ProfileShowReviewData | ProfileEpisodeReviewData;

export type ReviewData = ShowReviewData | EpisodeReviewData;

export type ProfileShow = ShowListData | ShowRankingData;
