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

const routes: Routes = [
    { path: '', component: HomePageComponent},
    { path: 'login', component: LoginPageComponent},
    { path: 'register', component: RegisterPageComponent},
    { path: 'profile', component: ProfilePageComponent},
    { path: 'user/:id', component: UserPageComponent},
    { path: 'search', component: SearchPageComponent},
    { path: 'show/:id', component: ShowPageComponent},
    { path: 'show/:id/season/:seasonNumber', component: SeasonPageComponent},
    { path: '**', component: PageNotFoundComponent}
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule { }
