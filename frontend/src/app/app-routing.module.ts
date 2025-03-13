import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomePageComponent } from './pages/home-page/home-page.component';
import {LoginPageComponent} from "./pages/login-page/login-page.component";

const routes: Routes = [
    { path: '', component: HomePageComponent},
    { path: 'login', component: LoginPageComponent},
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule { }
