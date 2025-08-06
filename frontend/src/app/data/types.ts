import {ProfileEpisodeReviewData, ProfileShowReviewData} from './profile-reviews-data';
import {EpisodeReviewData, ShowReviewData} from './reviews-data';

export type ProfileReviewData = ProfileShowReviewData | ProfileEpisodeReviewData;

export type ReviewData = ShowReviewData | EpisodeReviewData;
