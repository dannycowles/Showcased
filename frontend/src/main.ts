import "iconify-icon";
import {bootstrapApplication} from '@angular/platform-browser';
import {AppComponent} from './app/app.component';
import {provideRouter} from '@angular/router';
import {routes} from './app/app-routing.module';
import {provideCharts, withDefaultRegisterables} from 'ng2-charts';
import {inject} from '@vercel/analytics';
import {injectSpeedInsights} from '@vercel/speed-insights';

// Initialize Vercel analytics and speed insights
inject();
injectSpeedInsights();

bootstrapApplication(AppComponent, {
  providers: [
    provideRouter(routes),
    provideCharts(withDefaultRegisterables())
  ]
}).catch(err => console.error(err));
