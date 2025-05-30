import {Component, OnInit} from '@angular/core';
import {ProfileService} from '../../../services/profile.service';
import {SingleCollectionData} from '../../../data/single-collection-data';
import {ActivatedRoute, Router} from '@angular/router';
import $ from 'jquery';
import 'jquery-serializejson';
import {CdkDragDrop, moveItemInArray} from '@angular/cdk/drag-drop';
import {UtilsService} from '../../../services/utils.service';
import {UserService} from '../../../services/user.service';
import {SearchResultData} from '../../../data/search-result-data';
import {CollectionShowData} from '../../../data/collection-show-data';

@Component({
  selector: 'app-profile-collection-details-page',
  templateUrl: './profile-collection-details-page.component.html',
  styleUrl: './profile-collection-details-page.component.css',
  standalone: false
})
export class ProfileCollectionDetailsPageComponent implements OnInit {
  collectionData: SingleCollectionData;
  readonly collectionId: number;
  modalMessage: string;
  modalColor: string;

  constructor(private profileService: ProfileService,
              private route: ActivatedRoute,
              private router: Router,
              public utils : UtilsService,
              private userService: UserService) {
    this.collectionId = this.route.snapshot.params['id'];
  };

  async ngOnInit() {
    try {
      this.collectionData = await this.profileService.getCollectionDetails(this.collectionId);
    } catch (error) {
      console.error(error);
      this.router.navigate(['not-found']);
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

  async updateCollectionDetails() {
    const collectionError = document.getElementById("edit-collection-error-message");
    try {
      // @ts-ignore
      const collectionForm = $("#collection-form").serializeJSON();

      // Prevent user from entering blank collection name
      if (collectionForm["collectionName"].length === 0) {
        collectionError.innerText = "Collection name cannot be empty";
        collectionError.style.color = "red";
        return;
      }

      const data: any = {};
      if (collectionForm["collectionName"] !== this.collectionData.name) data.collectionName = collectionForm["collectionName"];
      if (collectionForm["description"] !== this.collectionData.description) data.description = collectionForm["description"];

      if (Object.keys(data).length === 0) {
        collectionError.innerText = "No changes to save.";
        collectionError.style.color = "gray";
        return;
      }

      const response = await this.profileService.updateCollection(this.collectionId, data);
      if (response.ok) {
        collectionError.innerText = "Changes saved!";
        collectionError.style.color = "green";

        // Update local state
        if (data.collectionName) this.collectionData.name = data.collectionName;
        if (data.description) this.collectionData.description = data.description;
      } else if (response.status == 409) {
        collectionError.innerText = "You already have a collection with that name";
        collectionError.style.color = "red";
      }
    } catch (error) {
      console.error(error);
    } finally {
      setTimeout(() => {
        collectionError.innerText = "";
      }, 3000);
    }
  }

  async toggleLikeState() {
    try {
      this.collectionData.likedByUser = !this.collectionData.likedByUser;
      if (this.collectionData.likedByUser) {
        await this.userService.likeCollection(this.collectionId);
        this.collectionData.numLikes++;
      } else {
        await this.userService.unlikeCollection(this.collectionId);
        this.collectionData.numLikes--;
      }
    } catch (error) {
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
      const response = await this.profileService.addShowToCollection(this.collectionId, data);

      if (response.ok) {
        this.modalMessage = `Successfully added ${show.name}`;
        this.modalColor = "green";
        this.collectionData.shows.push(new CollectionShowData(data));
      } else {
        this.modalMessage = "You already have this show in this collection.";
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
