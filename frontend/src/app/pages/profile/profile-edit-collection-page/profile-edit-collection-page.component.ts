import {Component, OnInit} from '@angular/core';
import {NgOptimizedImage} from '@angular/common';
import {ActivatedRoute, RouterLink} from '@angular/router';
import {SingleCollectionData} from '../../../data/single-collection-data';
import {ProfileService} from '../../../services/profile.service';
import {RankedShowListFullComponent} from '../../../components/ranked-show-list-full/ranked-show-list-full.component';
import {UpdateCollectionDetails} from '../../../data/dto/update-collection-details';
import {SearchShowsModalComponent} from '../../../components/search-shows-modal/search-shows-modal.component';
import {AddShowType} from '../../../data/enums';
import {CollectionShowData} from '../../../data/collection-show-data';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ConfirmationService} from '../../../services/confirmation.service';
import {Title} from '@angular/platform-browser';

@Component({
  selector: 'app-profile-edit-collection-page',
  imports: [
    NgOptimizedImage,
    RouterLink,
    RankedShowListFullComponent
  ],
  templateUrl: './profile-edit-collection-page.component.html',
  styleUrl: './profile-edit-collection-page.component.css',
  standalone: true
})
export class ProfileEditCollectionPageComponent implements OnInit {
  readonly collectionId: number;
  collectionDetails: SingleCollectionData;
  loadingData: boolean = true;

  constructor(private profileService: ProfileService,
              private route: ActivatedRoute,
              private modalService: NgbModal,
              private confirmationService: ConfirmationService,
              private title: Title) {
    this.collectionId = this.route.snapshot.params['id'];
  };

  async ngOnInit() {
    try {
      this.collectionDetails = await this.profileService.getCollectionDetails(this.collectionId);
      this.title.setTitle(`${this.collectionDetails.name}, Your Collection | Showcased`);
    } catch(error) {
      console.error(error);
    } finally {
      this.loadingData = false;
    }
  }

  openSearchShowsModal() {
    const searchShowsModalRef = this.modalService.open(SearchShowsModalComponent, {
      ariaLabelledBy: 'searchShowsModal',
      centered: true
    });
    searchShowsModalRef.componentInstance.showRanking = true;
    searchShowsModalRef.componentInstance.modalTitle = 'Add Show to Collection';
    searchShowsModalRef.componentInstance.addType = AddShowType.Collection;
    searchShowsModalRef.componentInstance.collectionId = this.collectionId;
    searchShowsModalRef.componentInstance.onAddShow = (show: CollectionShowData) => this.collectionDetails.shows.push(show);
  }

  async removeUnrankedShowFromCollection(removeShow: CollectionShowData) {
    const confirmation = await this.confirmationService.confirmRemove(removeShow.title);
    if (confirmation) {
      await this.removeShowFromCollection(removeShow.showId);
    }
  }

  async removeShowFromCollection(removeId: number) {
    try {
      await this.profileService.removeShowFromCollection(this.collectionId, removeId);

      // Remove the show from entries shown to user
      this.collectionDetails.shows = this.collectionDetails.shows.filter((show) => show.showId != removeId);
    } catch (error) {
      console.error(error);
    }
  }

  async updateCollectionRanking() {
    try {
      const data: UpdateCollectionDetails = {
        isRanked: this.collectionDetails.isRanked,
        shows: this.collectionDetails.shows.map((show) => ({
          id: show.showId,
      }))};
      await this.profileService.updateCollection(this.collectionId, data);
    } catch (error) {
      console.error(error);
    }
  }
}
