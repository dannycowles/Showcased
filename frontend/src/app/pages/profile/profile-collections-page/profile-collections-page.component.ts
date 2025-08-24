import {Component, OnInit} from '@angular/core';
import {ProfileService} from '../../../services/profile.service';
import {CollectionData} from '../../../data/collection-data';
import {UtilsService} from '../../../services/utils.service';
import {Router, RouterLink} from '@angular/router';
import {FormsModule} from '@angular/forms';
import {CollectionComponent} from '../../../components/collection/collection.component';
import {PageData} from '../../../data/page-data';
import {InfiniteScrollDirective} from 'ngx-infinite-scroll';

@Component({
  selector: 'app-profile-collections-page',
  templateUrl: './profile-collections-page.component.html',
  styleUrl: './profile-collections-page.component.css',
  imports: [
    FormsModule,
    RouterLink,
    CollectionComponent,
    InfiniteScrollDirective,
  ],
  standalone: true,
})
export class ProfileCollectionsPageComponent implements OnInit {
  collectionData: PageData<CollectionData>;
  searchCollectionString: string = '';
  debouncedSearchCollections: () => void;

  constructor(
    private profileService: ProfileService,
    public utilsService: UtilsService,
    private router: Router,
  ) {
    this.debouncedSearchCollections = this.utilsService.debounce(() =>
      this.searchCollections(),
    );
  }

  async ngOnInit() {
    try {
      this.collectionData = await this.profileService.getCollections();
    } catch (error) {
      console.error(error);
    }
  }

  createNewCollection() {
    this.router.navigate(['/profile/collections/new']);
  }

  async searchCollections() {
    try {
      this.collectionData = await this.profileService.getCollections(
        this.searchCollectionString,
      );
    } catch (error) {
      console.error(error);
    }
  }

  async loadMoreCollections() {
    // If all pages have already been loaded, return
    if (this.collectionData.page.number + 1 >= this.collectionData.page.totalPages) {
      return;
    }

    try {
      const response = await this.profileService.getCollections(this.searchCollectionString, this.collectionData.page.number + 2);
      this.collectionData.content.push(...response.content);
      this.collectionData.page = response.page;
    } catch (error) {
      console.error(error);
    }
  }
}
