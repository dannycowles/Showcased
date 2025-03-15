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
  ],
  imports: [
    BrowserModule,
    FormsModule,
    RouterModule,
    AppRoutingModule,
    NgOptimizedImage
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
