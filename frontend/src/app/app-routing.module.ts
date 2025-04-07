import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomePageComponent } from './pages/home-page/home-page.component';
import {LoginPageComponent} from "./pages/login-page/login-page.component";
import {PageNotFoundComponent} from "./pages/page-not-found/page-not-found.component";
import {RegisterPageComponent} from "./pages/register-page/register-page.component";
import {ProfilePageComponent} from "./pages/profile-page/profile-page.component";
import {UserPageComponent} from "./pages/user-page/user-page.component";
import {SearchPageComponent} from "./pages/search-page/search-page.component";
import {ShowPageComponent} from "./pages/show-page/show-page.component";
import {SeasonPageComponent} from "./pages/season-page/season-page.component";
import {EpisodePageComponent} from './pages/episode-page/episode-page.component';
import {ProfileWatchlistPageComponent} from './pages/profile-watchlist-page/profile-watchlist-page.component';
import {ProfileWatchingPageComponent} from './pages/profile-watching-page/profile-watching-page.component';
import {ProfileShowRankingPageComponent} from './pages/profile-show-ranking-page/profile-show-ranking-page.component';
import {
  ProfileEpisodeRankingPageComponent
} from './pages/profile-episode-ranking-page/profile-episode-ranking-page.component';
import {UserSearchPageComponent} from './pages/user-search-page/user-search-page.component';
import {UserWatchlistPageComponent} from './pages/user-watchlist-page/user-watchlist-page.component';
import {UserWatchingPageComponent} from './pages/user-watching-page/user-watching-page.component';
import {UserShowRankingPageComponent} from './pages/user-show-ranking-page/user-show-ranking-page.component';
import {UserEpisodeRankingPageComponent} from './pages/user-episode-ranking-page/user-episode-ranking-page.component';

const routes: Routes = [
    { path: '', component: HomePageComponent},
    { path: 'login', component: LoginPageComponent},
    { path: 'register', component: RegisterPageComponent},
    { path: 'profile', component: ProfilePageComponent},
    { path: 'profile/watchlist', component: ProfileWatchlistPageComponent},
    { path: 'profile/watching', component: ProfileWatchingPageComponent},
    { path: 'profile/show-ranking', component: ProfileShowRankingPageComponent},
    { path: 'profile/episode-ranking', component: ProfileEpisodeRankingPageComponent},
    { path: 'user/:id', component: UserPageComponent},
    { path: 'user/:id/watchlist', component: UserWatchlistPageComponent},
    { path: 'user/:id/watching', component: UserWatchingPageComponent},
    { path: 'user/:id/show-ranking', component: UserShowRankingPageComponent},
    { path: 'user/:id/episode-ranking', component: UserEpisodeRankingPageComponent},
    { path: 'search', component: SearchPageComponent},
    { path: 'search-users', component: UserSearchPageComponent},
    { path: 'show/:id', component: ShowPageComponent},
    { path: 'show/:id/season/:seasonNumber', component: SeasonPageComponent},
    { path: 'show/:id/season/:seasonNumber/episode/:episodeNumber', component: EpisodePageComponent },
    { path: '**', component: PageNotFoundComponent}
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule { }
