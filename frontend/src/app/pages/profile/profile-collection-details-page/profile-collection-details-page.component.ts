import {Component, OnInit} from '@angular/core';
import {ProfileService} from '../../../services/profile.service';
import {SingleCollectionData} from '../../../data/single-collection-data';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import {CdkDrag, CdkDragDrop, CdkDropList, moveItemInArray} from '@angular/cdk/drag-drop';
import {UtilsService} from '../../../services/utils.service';
import {UserService} from '../../../services/user.service';
import {CollectionShowData} from '../../../data/collection-show-data';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {SearchShowsModalComponent} from '../../../components/search-shows-modal/search-shows-modal.component';
import {AddShowType} from '../../../data/enums';
import {UpdateCollectionDetails} from '../../../data/dto/update-collection-details';
import {Title} from '@angular/platform-browser';
import {ConfirmationService} from '../../../services/confirmation.service';
import { ButtonHeartComponent } from '../../../components/button-heart.component';
import {NgOptimizedImage} from '@angular/common';

@Component({
  selector: 'app-profile-collection-details-page',
  templateUrl: './profile-collection-details-page.component.html',
  styleUrl: './profile-collection-details-page.component.css',
  imports: [
    ButtonHeartComponent,
    RouterLink,
    CdkDropList,
    CdkDrag,
    NgOptimizedImage,
  ],
  standalone: true,
})
export class ProfileCollectionDetailsPageComponent implements OnInit {
  collectionData: SingleCollectionData;
  readonly collectionId: number;

  constructor(
    private profileService: ProfileService,
    private route: ActivatedRoute,
    private router: Router,
    public utils: UtilsService,
    private userService: UserService,
    private modalService: NgbModal,
    private title: Title,
    private confirmationService: ConfirmationService,
  ) {
    this.collectionId = this.route.snapshot.params['id'];
  }

  async ngOnInit() {
    try {
      this.collectionData = await this.profileService.getCollectionDetails(
        this.collectionId,
      );
      this.title.setTitle(
        `${this.collectionData.name}, Your Collection | Showcased`,
      );
    } catch (error) {
      console.error(error);
      this.router.navigate(['not-found']);
    }
  }

  openSearchShowsModal() {
    const searchShowsModalRef = this.modalService.open(
      SearchShowsModalComponent,
      {
        ariaLabelledBy: 'searchShowsModal',
        centered: true,
      },
    );
    searchShowsModalRef.componentInstance.showRanking = true;
    searchShowsModalRef.componentInstance.modalTitle = 'Add Show to Collection';
    searchShowsModalRef.componentInstance.addType = AddShowType.Collection;
    searchShowsModalRef.componentInstance.collectionId = this.collectionId;
    searchShowsModalRef.componentInstance.onAddShow = (
      show: CollectionShowData,
    ) => this.collectionData.shows.push(show);
  }

  async removeShowFromCollection(removeShow: CollectionShowData) {
    try {
      const confirmation = await this.confirmationService.confirmRemove(
        removeShow.title,
      );

      if (confirmation) {
        const response = await this.profileService.removeShowFromCollection(
          this.collectionId,
          removeShow.showId,
        );
        if (response.ok) {
          this.collectionData.shows = this.collectionData.shows.filter(
            (show) => show.showId != removeShow.showId,
          );
        }
      }
    } catch (error) {
      console.error(error);
    }
  }

  async drop(event: CdkDragDrop<string[]>) {
    moveItemInArray(
      this.collectionData.shows,
      event.previousIndex,
      event.currentIndex,
    );

    // Update the rank numbers based on the index within the updated list
    this.collectionData.shows.forEach((show, index) => {
      show.rankNum = index + 1;
    });
    await this.updateCollectionRanking();
  }

  async updateCollectionRanking() {
    try {
      const data: UpdateCollectionDetails = {
        isRanked: this.collectionData.isRanked,
        shows: this.collectionData.shows.map((show) => ({
          id: show.showId,
        })),
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
