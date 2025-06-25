import {Component, OnInit} from '@angular/core';
import {ProfileService} from '../../../services/profile.service';
import {ShowListData} from '../../../data/lists/show-list-data';
import {SearchResultData} from '../../../data/search-result-data';

@Component({
  selector: 'app-profile-watching-page',
  templateUrl: './profile-watching-page.component.html',
  styleUrl: './profile-watching-page.component.css',
  standalone: false
})
export class ProfileWatchingPageComponent implements OnInit {
  watchingEntries: ShowListData[];
  modalMessage: string;
  modalColor: string;

  constructor(private profileService: ProfileService) { }

  async ngOnInit() {

    // Retrieve watching entries for profile
    try {
      this.watchingEntries = await this.profileService.getFullWatchingList();
    } catch(error) {
      console.error(error);
    }
  }

  async removeShowFromWatchingList(removeId: number) {
    try {
      await this.profileService.removeShowFromWatchingList(removeId);

      // Remove the show from the entries shown to the user
      this.watchingEntries = this.watchingEntries.filter(show => show.showId != removeId);
    } catch(error) {
      console.error(error);
    }
  }

  async moveShowToRankingList(moveId: number) {
    try {
      await this.profileService.moveShowToRankingList(moveId);

      // Remove the show from the entries shown to the user
      this.watchingEntries = this.watchingEntries.filter(show => show.showId != moveId);
    } catch(error) {
      console.error(error);
    }
  }

  async handleAddShow(show: SearchResultData) {
    try {
      const data: ShowListData = {
        showId: show.id,
        title: show.title,
        posterPath: show.posterPath
      };
      const response = await this.profileService.addShowToWatchingList(data);

      if (response.ok) {
        this.modalMessage = "Successfully added!";
        this.modalColor = "green";
        this.watchingEntries.push(data);
      } else if (response.status === 409) {
        this.modalMessage = "You already have this show on your watching list.";
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
