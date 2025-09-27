import {Component, OnInit} from '@angular/core';
import {ProfileService} from '../../../services/profile.service';
import {DynamicRankingData} from '../../../data/lists/dynamic-ranking-data';
import {SearchResultData} from '../../../data/search-result-data';
import {RoleData} from '../../../data/role-data';
import {RouterLink} from '@angular/router';
import {NgOptimizedImage} from '@angular/common';
import {DynamicListFullComponent} from '../../../components/dynamic-list-full/dynamic-list-full.component';

@Component({
  selector: 'app-profile-dynamics-ranking-page',
  templateUrl: './profile-dynamics-ranking-page.component.html',
  styleUrl: './profile-dynamics-ranking-page.component.css',
  standalone: true,
  imports: [
    RouterLink,
    NgOptimizedImage,
    DynamicListFullComponent,
  ],
})
export class ProfileDynamicsRankingPageComponent implements OnInit {
  dynamics: DynamicRankingData[];
  selectedShow: SearchResultData | null = null;
  selectedCharacter1: RoleData | null = null;

  constructor(private profileService: ProfileService) {}

  async ngOnInit() {
    try {
      this.dynamics = await this.profileService.getDynamicRankingList();
    } catch (error) {
      console.error(error);
    }
  }
}
