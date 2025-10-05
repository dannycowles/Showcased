import {Component, OnInit} from '@angular/core';
import {ProfileService} from '../../../services/profile.service';
import {ShowListData} from '../../../data/lists/show-list-data';
import {ShowListFullComponent} from '../../../components/show-list-full/show-list-full.component';
import {RouterLink} from '@angular/router';
import {NgOptimizedImage} from '@angular/common';

@Component({
  selector: 'app-profile-watching-page',
  templateUrl: './profile-watching-page.component.html',
  styleUrl: './profile-watching-page.component.css',
  imports: [ShowListFullComponent, RouterLink, NgOptimizedImage],
  standalone: true,
})
export class ProfileWatchingPageComponent implements OnInit {
  watchingEntries: ShowListData[] = [];
  loadingData: boolean = true;

  constructor(private profileService: ProfileService) {}

  async ngOnInit() {
    // Retrieve watching entries for profile
    try {
      this.watchingEntries = await this.profileService.getFullWatchingList();
    } catch (error) {
      console.error(error);
    } finally {
      this.loadingData = false;
    }
  }
}
