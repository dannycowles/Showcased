import {Component, OnInit} from '@angular/core';
import {CdkDragDrop, moveItemInArray} from '@angular/cdk/drag-drop';
import {ProfileService} from '../../services/profile.service';
import {ShowRankingData} from '../../data/show-ranking-data';

@Component({
  selector: 'app-profile-show-ranking-page',
  templateUrl: './profile-show-ranking-page.component.html',
  styleUrl: './profile-show-ranking-page.component.css',
  standalone: false
})
export class ProfileShowRankingPageComponent implements OnInit {
  rankingEntries: ShowRankingData[];

  constructor(private profileService: ProfileService) { }

  async ngOnInit() {

    // Retrieve the show ranking entries for the profile
    try {
      this.rankingEntries = await this.profileService.getFullShowRankingList();
    } catch (error) {
      console.error(error);
    }
  }

  returnToProfilePressed() {
    window.location.href = "profile";
  }

  async removeShowFromRankingList(removeId: number) {
    try {
      await this.profileService.removeShowFromRankingList(removeId);

      // Remove the show from entries shown to user
      this.rankingEntries = this.rankingEntries.filter(show => show.showId != removeId);
    } catch(error) {
      console.error(error);
    }
  }

  drop(event: CdkDragDrop<string[]>) {
    moveItemInArray(this.rankingEntries, event.previousIndex, event.currentIndex);

    // TODO: here run a loop to update the ranking entries rank number before calling service update method
  }

}
