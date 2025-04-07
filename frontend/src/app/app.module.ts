import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import {HomePageComponent} from './pages/home-page/home-page.component';
import {RouterModule} from '@angular/router';
import {LoginPageComponent} from './pages/login-page/login-page.component';
import {PageNotFoundComponent} from './pages/page-not-found/page-not-found.component';
import {RegisterPageComponent} from './pages/register-page/register-page.component';
import {ProfilePageComponent} from './pages/profile-page/profile-page.component';
import {UserPageComponent} from './pages/user-page/user-page.component';
import {SearchPageComponent} from './pages/search-page/search-page.component';
import {ShowPageComponent} from './pages/show-page/show-page.component';
import {SeasonPageComponent} from './pages/season-page/season-page.component';
import {NgOptimizedImage} from '@angular/common';
import {AngularToastifyModule, ToastService} from 'angular-toastify';
import {EpisodePageComponent} from './pages/episode-page/episode-page.component';
import {ProfileWatchlistPageComponent} from './pages/profile-watchlist-page/profile-watchlist-page.component';
import {ProfileWatchingPageComponent} from './pages/profile-watching-page/profile-watching-page.component';
import {ProfileShowRankingPageComponent} from './pages/profile-show-ranking-page/profile-show-ranking-page.component';
import {
  ProfileEpisodeRankingPageComponent
} from './pages/profile-episode-ranking-page/profile-episode-ranking-page.component';
import { DragDropModule} from '@angular/cdk/drag-drop';
import {UserSearchPageComponent} from './pages/user-search-page/user-search-page.component';
import {UserWatchlistPageComponent} from './pages/user-watchlist-page/user-watchlist-page.component';
import {UserWatchingPageComponent} from './pages/user-watching-page/user-watching-page.component';
import {UserShowRankingPageComponent} from './pages/user-show-ranking-page/user-show-ranking-page.component';
import {UserEpisodeRankingPageComponent} from './pages/user-episode-ranking-page/user-episode-ranking-page.component';

@NgModule({
  declarations: [
    AppComponent,
    HomePageComponent,
    LoginPageComponent,
    PageNotFoundComponent,
    RegisterPageComponent,
    ProfilePageComponent,
    UserPageComponent,
    SearchPageComponent,
    ShowPageComponent,
    SeasonPageComponent,
    EpisodePageComponent,
    ProfileWatchlistPageComponent,
    ProfileWatchingPageComponent,
    ProfileShowRankingPageComponent,
    ProfileEpisodeRankingPageComponent,
    UserSearchPageComponent,
    UserWatchlistPageComponent,
    UserWatchingPageComponent,
    UserShowRankingPageComponent,
    UserEpisodeRankingPageComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    RouterModule,
    AppRoutingModule,
    NgOptimizedImage,
    AngularToastifyModule,
    DragDropModule
  ],
  providers: [ToastService],
  bootstrap: [AppComponent]
})
export class AppModule { }
