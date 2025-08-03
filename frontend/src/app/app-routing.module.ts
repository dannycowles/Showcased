import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomePageComponent } from './pages/home-page/home-page.component';
import {LoginPageComponent} from "./pages/auth/login-page/login-page.component";
import {PageNotFoundComponent} from "./pages/page-not-found/page-not-found.component";
import {RegisterPageComponent} from "./pages/auth/register-page/register-page.component";
import {ProfilePageComponent} from "./pages/profile/profile-page/profile-page.component";
import {UserPageComponent} from "./pages/user/user-page/user-page.component";
import {SearchPageComponent} from "./pages/show/search-page/search-page.component";
import {ShowPageComponent} from "./pages/show/show-page/show-page.component";
import {SeasonPageComponent} from "./pages/show/season-page/season-page.component";
import {EpisodePageComponent} from './pages/show/episode-page/episode-page.component';
import {ProfileWatchlistPageComponent} from './pages/profile/profile-watchlist-page/profile-watchlist-page.component';
import {ProfileWatchingPageComponent} from './pages/profile/profile-watching-page/profile-watching-page.component';
import {ProfileShowRankingPageComponent} from './pages/profile/profile-show-ranking-page/profile-show-ranking-page.component';
import {
  ProfileEpisodeRankingPageComponent
} from './pages/profile/profile-episode-ranking-page/profile-episode-ranking-page.component';
import {UserSearchPageComponent} from './pages/user/user-search-page/user-search-page.component';
import {UserWatchlistPageComponent} from './pages/user/user-watchlist-page/user-watchlist-page.component';
import {UserWatchingPageComponent} from './pages/user/user-watching-page/user-watching-page.component';
import {UserShowRankingPageComponent} from './pages/user/user-show-ranking-page/user-show-ranking-page.component';
import {UserEpisodeRankingPageComponent} from './pages/user/user-episode-ranking-page/user-episode-ranking-page.component';
import {ResetPasswordPageComponent} from './pages/auth/reset-password-page/reset-password-page.component';
import {ProfileFollowersPageComponent} from './pages/profile/profile-followers-page/profile-followers-page.component';
import {ProfileFollowingPageComponent} from './pages/profile/profile-following-page/profile-following-page.component';
import {UserFollowersPageComponent} from './pages/user/user-followers-page/user-followers-page.component';
import {UserFollowingPageComponent} from './pages/user/user-following-page/user-following-page.component';
import {ProfileSeasonRankingPageComponent} from './pages/profile/profile-season-ranking-page/profile-season-ranking-page.component';
import {UserSeasonRankingPageComponent} from './pages/user/user-season-ranking-page/user-season-ranking-page.component';
import {
  ProfileCharacterRankingPageComponent
} from './pages/profile/profile-character-ranking-page/profile-character-ranking-page.component';
import {
  UserCharacterRankingPageComponent
} from './pages/user/user-character-ranking-page/user-character-ranking-page.component';
import {DiscoverPageComponent} from './pages/discover/discover-page/discover-page.component';
import {DiscoverGenresPageComponent} from './pages/discover/discover-genres-page/discover-genres-page.component';
import {
  ProfileCollectionsPageComponent
} from './pages/profile/profile-collections-page/profile-collections-page.component';
import {
  ProfileCollectionDetailsPageComponent
} from './pages/profile/profile-collection-details-page/profile-collection-details-page.component';
import {UserCollectionsPageComponent} from './pages/user/user-collections-page/user-collections-page.component';
import {
  UserCollectionDetailsPageComponent
} from './pages/user/user-collection-details-page/user-collection-details-page.component';
import {
  ProfileDynamicsRankingPageComponent
} from './pages/profile/profile-dynamics-ranking-page/profile-dynamics-ranking-page.component';
import {
  UserDynamicRankingPageComponent
} from './pages/user/user-dynamic-ranking-page/user-dynamic-ranking-page.component';
import {DiscoverTrendingPageComponent} from './pages/discover/discover-trending-page/discover-trending-page.component';
import {
  DiscoverTopRatedPageComponent
} from './pages/discover/discover-top-rated-page/discover-top-rated-page.component';
import {ProfileActivityPageComponent} from './pages/profile/profile-activity-page/profile-activity-page.component';
import {ProfileReviewsPageComponent} from './pages/profile/profile-reviews-page/profile-reviews-page.component';
import {UserReviewsPageComponent} from './pages/user/user-reviews-page/user-reviews-page.component';

const routes: Routes = [
    { path: '', component: HomePageComponent},
    { path: 'login', component: LoginPageComponent},
    { path: 'register', component: RegisterPageComponent},
    { path: 'reset-password', component: ResetPasswordPageComponent},
    { path: 'profile', component: ProfilePageComponent},
    { path: 'profile/watchlist', component: ProfileWatchlistPageComponent},
    { path: 'profile/watching', component: ProfileWatchingPageComponent},
    { path: 'profile/show-ranking', component: ProfileShowRankingPageComponent},
    { path: 'profile/episode-ranking', component: ProfileEpisodeRankingPageComponent},
    { path: 'profile/season-ranking', component: ProfileSeasonRankingPageComponent},
    { path: 'profile/followers', component: ProfileFollowersPageComponent},
    { path: 'profile/following', component: ProfileFollowingPageComponent},
    { path: 'profile/character-ranking/:type', component: ProfileCharacterRankingPageComponent },
    { path: 'profile/dynamic-ranking', component: ProfileDynamicsRankingPageComponent },
    { path: 'profile/collections', component: ProfileCollectionsPageComponent },
    { path: 'profile/collections/:id', component: ProfileCollectionDetailsPageComponent },
    { path: 'profile/activity', component: ProfileActivityPageComponent },
    { path: 'profile/reviews', component: ProfileReviewsPageComponent },
    { path: 'user/:id', component: UserPageComponent},
    { path: 'user/:id/watchlist', component: UserWatchlistPageComponent},
    { path: 'user/:id/watching', component: UserWatchingPageComponent},
    { path: 'user/:id/show-ranking', component: UserShowRankingPageComponent},
    { path: 'user/:id/season-ranking', component: UserSeasonRankingPageComponent},
    { path: 'user/:id/episode-ranking', component: UserEpisodeRankingPageComponent},
    { path: 'user/:id/character-ranking/:type', component: UserCharacterRankingPageComponent},
    { path: 'user/:id/followers', component: UserFollowersPageComponent},
    { path: 'user/:id/following', component: UserFollowingPageComponent},
    { path: 'user/:id/collections', component: UserCollectionsPageComponent},
    { path: 'user/:id/collections/:collectionId', component: UserCollectionDetailsPageComponent},
    { path: 'user/:id/dynamic-ranking', component: UserDynamicRankingPageComponent},
    { path: 'user/:id/reviews', component: UserReviewsPageComponent },
    { path: 'search', component: SearchPageComponent},
    { path: 'search-users', component: UserSearchPageComponent},
    { path: 'show/:id', component: ShowPageComponent},
    { path: 'show/:id/season/:seasonNumber', component: SeasonPageComponent},
    { path: 'show/:id/season/:seasonNumber/episode/:episodeNumber', component: EpisodePageComponent },
    { path: 'discover', component: DiscoverPageComponent},
    { path: 'discover/show', component: DiscoverGenresPageComponent},
    { path: 'discover/trending', component: DiscoverTrendingPageComponent },
    { path: 'discover/top', component: DiscoverTopRatedPageComponent},
    { path: 'not-found', component: PageNotFoundComponent},
    { path: '**', redirectTo: 'not-found'}
];

@NgModule({
    imports: [RouterModule.forRoot(routes, {scrollPositionRestoration:'top'})],
    exports: [RouterModule]
})
export class AppRoutingModule { }
