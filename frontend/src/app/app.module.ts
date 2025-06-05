import { BrowserModule } from '@angular/platform-browser';
import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';

import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import {HomePageComponent} from './pages/home-page/home-page.component';
import {RouterModule} from '@angular/router';
import {LoginPageComponent} from './pages/auth/login-page/login-page.component';
import {PageNotFoundComponent} from './pages/page-not-found/page-not-found.component';
import {RegisterPageComponent} from './pages/auth/register-page/register-page.component';
import {ProfilePageComponent} from './pages/profile/profile-page/profile-page.component';
import {UserPageComponent} from './pages/user/user-page/user-page.component';
import {SearchPageComponent} from './pages/show/search-page/search-page.component';
import {ShowPageComponent} from './pages/show/show-page/show-page.component';
import {SeasonPageComponent} from './pages/show/season-page/season-page.component';
import {NgOptimizedImage} from '@angular/common';
import {AngularToastifyModule, ToastService} from 'angular-toastify';
import {EpisodePageComponent} from './pages/show/episode-page/episode-page.component';
import {ProfileWatchlistPageComponent} from './pages/profile/profile-watchlist-page/profile-watchlist-page.component';
import {ProfileWatchingPageComponent} from './pages/profile/profile-watching-page/profile-watching-page.component';
import {ProfileShowRankingPageComponent} from './pages/profile/profile-show-ranking-page/profile-show-ranking-page.component';
import {
  ProfileEpisodeRankingPageComponent
} from './pages/profile/profile-episode-ranking-page/profile-episode-ranking-page.component';
import { DragDropModule} from '@angular/cdk/drag-drop';
import {UserSearchPageComponent} from './pages/user/user-search-page/user-search-page.component';
import {UserWatchlistPageComponent} from './pages/user/user-watchlist-page/user-watchlist-page.component';
import {UserWatchingPageComponent} from './pages/user/user-watching-page/user-watching-page.component';
import {UserShowRankingPageComponent} from './pages/user/user-show-ranking-page/user-show-ranking-page.component';
import {UserEpisodeRankingPageComponent} from './pages/user/user-episode-ranking-page/user-episode-ranking-page.component';
import {ResetPasswordPageComponent} from './pages/auth/reset-password-page/reset-password-page.component';
import {NgOtpInputModule} from 'ng-otp-input';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {ButtonHeartComponent} from './pages/show/button-heart.component';
import {ProfileFollowingPageComponent} from './pages/profile/profile-following-page/profile-following-page.component';
import {ProfileFollowersPageComponent} from './pages/profile/profile-followers-page/profile-followers-page.component';
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
import {AddShowModalComponent} from "./components/add-show-modal/add-show-modal.component";
import {FollowersFollowingComponent} from "./components/followers-following/followers-following.component";
import {UserInfoComponent} from './components/user-info/user-info.component';
import {ShowListComponent} from './components/show-list/show-list.component';
import {SeasonListComponent} from './components/season-list/season-list.component';
import {EpisodeListComponent} from './components/episode-list/episode-list.component';
import {CharacterListComponent} from './components/character-list/character-list.component';
import {ProfileShowReviewComponent} from './components/profile-show-review/profile-show-review.component';
import {ShowListFullComponent} from './components/show-list-full/show-list-full.component';
import {RankedShowListFullComponent} from './components/ranked-show-list-full/ranked-show-list-full.component';
import {RankedSeasonListFullComponent} from './components/ranked-season-list-full/ranked-season-list-full.component';
import {RankedEpisodeListFullComponent} from './components/ranked-episode-list-full/ranked-episode-list-full.component';

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
    UserEpisodeRankingPageComponent,
    ResetPasswordPageComponent,
    ProfileFollowingPageComponent,
    ProfileFollowersPageComponent,
    UserFollowersPageComponent,
    UserFollowingPageComponent,
    ProfileSeasonRankingPageComponent,
    UserSeasonRankingPageComponent,
    ProfileCharacterRankingPageComponent,
    UserCharacterRankingPageComponent,
    DiscoverPageComponent,
    DiscoverGenresPageComponent,
    ProfileCollectionsPageComponent,
    ProfileCollectionDetailsPageComponent,
    UserCollectionsPageComponent,
    UserCollectionDetailsPageComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    RouterModule,
    AppRoutingModule,
    NgOptimizedImage,
    AngularToastifyModule,
    DragDropModule,
    NgOtpInputModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    ButtonHeartComponent,
    AddShowModalComponent,
    FollowersFollowingComponent,
    UserInfoComponent,
    ShowListComponent,
    SeasonListComponent,
    EpisodeListComponent,
    CharacterListComponent,
    ProfileShowReviewComponent,
    ShowListFullComponent,
    RankedShowListFullComponent,
    RankedSeasonListFullComponent,
    RankedEpisodeListFullComponent
  ],
  providers: [ToastService],
  bootstrap: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AppModule { }
