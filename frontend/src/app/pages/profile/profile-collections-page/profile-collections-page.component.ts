import {Component, OnInit} from '@angular/core';
import {ProfileService} from '../../../services/profile.service';
import {CollectionData} from '../../../data/collection-data';
import {UtilsService} from '../../../services/utils.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {
  CreateCollectionModalComponent
} from '../../../components/create-collection-modal/create-collection-modal.component';

@Component({
  selector: 'app-profile-collections-page',
  templateUrl: './profile-collections-page.component.html',
  styleUrl: './profile-collections-page.component.css',
  standalone: false
})
export class ProfileCollectionsPageComponent implements OnInit {
  collectionData: CollectionData[];
  searchCollectionString: string = "";
  debouncedSearchCollections: () => void;

  constructor(private profileService: ProfileService,
              public utilsService: UtilsService,
              private modalService: NgbModal) {
    this.debouncedSearchCollections = this.utilsService.debounce(() => this.searchCollections());
  };

  async ngOnInit() {
    try {
      this.collectionData = await this.profileService.getCollections();
    } catch (error) {
      console.error(error);
    }
  };

  openCreateCollectionModal() {
    const createCollectionModalRef = this.modalService.open(CreateCollectionModalComponent, {
      centered: true,
      ariaLabelledBy: "createCollectionModal"
    });
    createCollectionModalRef.componentInstance.onCreate = (collection: CollectionData)=> this.collectionData.push(collection);
  }

  async searchCollections() {
    try {
      this.collectionData = await this.profileService.getCollections(this.searchCollectionString);
    } catch (error) {
      console.error(error);
    }
  }
}
