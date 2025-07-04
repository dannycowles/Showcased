import {Component, OnInit} from '@angular/core';
import {ProfileService} from '../../../services/profile.service';
import {SingleCollectionData} from '../../../data/single-collection-data';
import {ActivatedRoute, Router} from '@angular/router';
import $ from 'jquery';
import 'jquery-serializejson';
import {CdkDragDrop, moveItemInArray} from '@angular/cdk/drag-drop';
import {UtilsService} from '../../../services/utils.service';
import {UserService} from '../../../services/user.service';
import {CollectionShowData} from '../../../data/collection-show-data';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {SearchShowsModalComponent} from '../../../components/search-shows-modal/search-shows-modal.component';
import {AddShowType} from '../../../data/enums';
import {EditCollectionModalComponent} from '../../../components/edit-collection-modal/edit-collection-modal.component';

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
              private router: Router,
              public utils : UtilsService,
              private userService: UserService,
              private modalService: NgbModal) {
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

  openSearchShowsModal() {
    const searchShowsModalRef = this.modalService.open(SearchShowsModalComponent, {
      ariaLabelledBy: "searchShowsModal",
      centered: true
    });
    searchShowsModalRef.componentInstance.showRanking = true;
    searchShowsModalRef.componentInstance.modalTitle = "Add Show to Collection";
    searchShowsModalRef.componentInstance.addType = AddShowType.Collection;
    searchShowsModalRef.componentInstance.collectionId = this.collectionId;
    searchShowsModalRef.componentInstance.onAddShow = (show: CollectionShowData) => this.collectionData.shows.push(show);
  }

  openEditCollectionModal() {
    const editCollectionModalRef = this.modalService.open(EditCollectionModalComponent, {
      ariaLabelledBy: "editCollectionModal",
      centered: true
    });
    editCollectionModalRef.componentInstance.collectionId = this.collectionId;
    editCollectionModalRef.componentInstance.collectionName = this.collectionData.name;
    editCollectionModalRef.componentInstance.collectionDescription = this.collectionData.description;
    editCollectionModalRef.componentInstance.onUpdate = (updates: any) => {
      if (updates.collectionName) this.collectionData.name = updates.collectionName;
      if (updates.description) this.collectionData.description = updates.description;
    }
  }

  async removeShowFromCollection(showId: number ) {
    try {
      await this.profileService.removeShowFromCollection(this.collectionId, showId);
      this.collectionData.shows = this.collectionData.shows.filter(show => show.showId != showId);
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
          return { showId: show.showId };
        })
      };
      await this.profileService.updateCollection(this.collectionId, data);
    } catch (error) {
      console.error(error);
    }
  }

  async toggleLikeState() {
    try {
      this.collectionData.isLikedByUser = !this.collectionData.isLikedByUser;
      if (this.collectionData.isLikedByUser) {
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
}
