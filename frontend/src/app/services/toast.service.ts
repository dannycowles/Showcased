import {Injectable} from '@angular/core';
import { ToastService } from 'angular-toastify';

@Injectable({
  providedIn: 'root'
})
export class ToastDisplayService {

  constructor(private toastService: ToastService) { }

  addToWatchlistToast(name: string) {
    this.toastService.success(`Added ${name} to your watchlist`);
  }

  addToWatchingListToast(name: string) {
    this.toastService.success(`Added ${name} to your watching list`);
  }

  addToShowRankingToast(name: string) {
    this.toastService.success(`Added ${name} to your ranking list`);
  }

  addToEpisodeRankingToast(name: string) {
    this.toastService.success(`Added ${name} to your ranking list`);
  }

  addConflictToast(name: string) {
    this.toastService.error(`${name} is already on one of your lists`);
  }
}
