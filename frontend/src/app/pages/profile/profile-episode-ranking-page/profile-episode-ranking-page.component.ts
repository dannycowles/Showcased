import {Component, OnInit} from '@angular/core';
import {ProfileService} from '../../../services/profile.service';
import {EpisodeRankingData} from '../../../data/lists/episode-ranking-data';
import {RouterLink} from '@angular/router';
import {NgOptimizedImage} from '@angular/common';
import {EpisodeListFullComponent} from '../../../components/episode-list-full/episode-list-full.component';

@Component({
  selector: 'app-profile-episode-ranking-page',
  templateUrl: './profile-episode-ranking-page.component.html',
  styleUrl: './profile-episode-ranking-page.component.css',
  imports: [
    RouterLink,
    NgOptimizedImage,
    EpisodeListFullComponent,
  ],
  standalone: true,
})
export class ProfileEpisodeRankingPageComponent implements OnInit {
  rankingEntries: EpisodeRankingData[] = [];
  loadingData: boolean = true;

  constructor(private profileService: ProfileService) {}

  async ngOnInit() {
    // Retrieve episode ranking entries for the profile
    try {
      this.rankingEntries = await this.profileService.getFullEpisodeRankingList();
    } catch (error) {
      console.error(error);
    } finally {
      this.loadingData = false;
    }
  }
}
