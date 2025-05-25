import {Component, OnInit} from '@angular/core';
import {ProfileService} from '../../../services/profile.service';
import {SingleCollectionData} from '../../../data/single-collection-data';
import {ActivatedRoute, Router} from '@angular/router';
import $ from 'jquery';
import {CdkDragDrop, moveItemInArray} from '@angular/cdk/drag-drop';

@Component({
  selector: 'app-profile-collection-details-page',
  templateUrl: './profile-collection-details-page.component.html',
  styleUrl: './profile-collection-details-page.component.css',
  standalone: false
})
export class ProfileCollectionDetailsPageComponent implements OnInit {
  collectionData: SingleCollectionData;
  readonly collectionId: number;

  constructor(private profileService: ProfileService,
              private route: ActivatedRoute,
              private router: Router) {
    this.collectionId = this.route.snapshot.params['id'];
  };

  async ngOnInit() {
    try {
      this.collectionData = await this.profileService.getCollectionDetails(this.collectionId);
    } catch (error) {
      console.error(error);
    }
  };

  async removeShowFromCollection(showId: number ) {
    try {
      await this.profileService.removeShowFromCollection(this.collectionId, showId);
      this.collectionData.shows = this.collectionData.shows.filter(show => show.id != showId);
    } catch (error) {
      console.error(error);
    }
  }

  async toggleCollectionVisibility() {
    const visibility = $('#visibility-radios input:radio:checked').val() as "private" | "public";

    try {
      const data = {
        isPrivate: visibility === 'private'
      };
      await this.profileService.updateCollection(this.collectionId, data);
    } catch (error) {
      console.error(error);
    }
  }

  async toggleRankSetting() {
    const ranking = $('#ranked-radios input:radio:checked').val() as "ranked" | "unranked";
    this.collectionData.isRanked = ranking === "ranked";
    await this.updateCollectionRanking();
  }

  async drop(event: CdkDragDrop<string[]>) {
    moveItemInArray(this.collectionData.shows, event.previousIndex, event.currentIndex);

    // Update the rank numbers based on the index within the updated list
    this.collectionData.shows.forEach((show, index) => {
      show.rankNum = index + 1;
    });
    await this.updateCollectionRanking();
  }

  async deleteCollection() {
    try {
      await this.profileService.deleteCollection(this.collectionId);
      this.router.navigate(['/profile/collections']);
    } catch (error) {
      console.error(error);
    }
  }

  async updateCollectionRanking() {
    try {
      const data = {
        isRanked: this.collectionData.isRanked,
        shows: this.collectionData.shows.map(show => {
          return { showId: show.id };
        })
      };
      await this.profileService.updateCollection(this.collectionId, data);
    } catch (error) {
      console.error(error);
    }
  }

}
