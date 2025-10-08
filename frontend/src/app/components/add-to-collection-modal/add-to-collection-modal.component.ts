import {Component, Input, OnInit} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {CollectionData} from '../../data/collection-data';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {UtilsService} from '../../services/utils.service';
import {ProfileService} from '../../services/profile.service';
import {ShowListData} from '../../data/lists/show-list-data';
import {AddToCollection} from '../../data/dto/add-to-list-dto';
import {Router} from '@angular/router';
import {PageData} from '../../data/page-data';

@Component({
  selector: 'app-add-to-collection-modal',
  imports: [FormsModule],
  templateUrl: './add-to-collection-modal.component.html',
  styleUrl: './add-to-collection-modal.component.css',
  standalone: true,
})
export class AddToCollectionModalComponent implements OnInit {
  @Input({ required: true }) show: ShowListData;
  searchCollectionString: string = '';
  collections: PageData<CollectionData>;
  selectedCollection: CollectionData | null = null;

  message: string = '';
  messageColor: string = '';
  isLoading: boolean = false;

  debouncedSearchCollections: () => void;

  constructor(public activeModal: NgbActiveModal,
              public utilsService: UtilsService,
              private profileService: ProfileService,
              private router: Router) {
    this.debouncedSearchCollections = this.utilsService.debounce(() => this.searchCollections());
  }

  async ngOnInit() {
    await this.searchCollections();
  }

  async searchCollections() {
    this.isLoading = true;
    try {
      this.collections = await this.profileService.getCollections(this.searchCollectionString);
    } catch (error) {
      console.error(error);
    } finally {
      this.isLoading = false;
    }
  }

  navigateToCreateNewCollection() {
    this.activeModal.close();
    this.router.navigate(['/profile/collections/new']);
  }

  async addToCollection() {
    try {
      const showData: AddToCollection = {
        showId: this.show.showId,
        showTitle: this.show.title,
        posterPath: this.show.posterPath,
      };

      const response = await this.profileService.addShowToCollection(this.selectedCollection.id, showData);
      if (response.ok) {
        this.message = `Successfully added to ${this.selectedCollection.name}!`;
        this.messageColor = 'green';
      } else {
        this.message = `Show is already in ${this.selectedCollection.name}!`;
        this.messageColor = 'red';
      }
    } catch (error) {
      console.error(error);
    } finally {
      setTimeout(() => (this.message = ''), 3000);
    }
  }
}
