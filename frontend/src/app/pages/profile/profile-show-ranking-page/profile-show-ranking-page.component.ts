import {Component, OnInit} from '@angular/core';
import {CdkDragDrop, moveItemInArray} from '@angular/cdk/drag-drop';
import {ProfileService} from '../../../services/profile.service';
import {ShowRankingData} from '../../../data/lists/show-ranking-data';
import {SearchResultData} from '../../../data/search-result-data';

@Component({
  selector: 'app-profile-show-ranking-page',
  templateUrl: './profile-show-ranking-page.component.html',
  styleUrl: './profile-show-ranking-page.component.css',
  standalone: false
})
export class ProfileShowRankingPageComponent implements OnInit {
  rankingEntries: ShowRankingData[];
  modalMessage: string;
  modalColor: string;

  constructor(private profileService: ProfileService) { }

  async ngOnInit() {

    // Retrieve the show ranking entries for the profile
    try {
      this.rankingEntries = await this.profileService.getFullShowRankingList();
    } catch (error) {
      console.error(error);
    }
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

  async updateShowRankingList() {
    try {
      let updates = this.rankingEntries.map(show => ({
          "showId": show.showId,
          "rankNum": show.rankNum
      }));
      await this.profileService.updateShowRankingList(updates);
    } catch(error) {
      console.error(error);
    }
  }

  async handleAddShow(show: SearchResultData) {
    try {
      const data = {
        showId: show.id,
        title: show.name,
        posterPath: show.posterPath
      };
      const response = await this.profileService.addShowToRankingList(data);

      if (response.ok) {
        this.modalMessage = "Successfully added!";
        this.modalColor = "green";
        this.rankingEntries.push(new ShowRankingData(data));
      } else if (response.status === 409) {
        this.modalMessage = "You already have this show on your ranking list.";
        this.modalColor = "red";
      }
    } catch (error) {
      console.error(error);
    } finally {
      setTimeout(() => {
        this.modalMessage = "";
      }, 3000);
    }
  }

}
