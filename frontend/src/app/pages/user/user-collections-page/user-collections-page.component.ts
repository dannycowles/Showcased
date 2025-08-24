import {Component, OnInit} from '@angular/core';
import {UserService} from '../../../services/user.service';
import {ActivatedRoute, RouterLink} from '@angular/router';
import {CollectionData} from '../../../data/collection-data';
import {UtilsService} from '../../../services/utils.service';
import {Title} from '@angular/platform-browser';
import {FormsModule} from '@angular/forms';
import {CollectionComponent} from '../../../components/collection/collection.component';
import {PageData} from '../../../data/page-data';
import {InfiniteScrollDirective} from 'ngx-infinite-scroll';

@Component({
  selector: 'app-user-collections-page',
  templateUrl: './user-collections-page.component.html',
  styleUrl: './user-collections-page.component.css',
  imports: [
    RouterLink,
    FormsModule,
    CollectionComponent,
    InfiniteScrollDirective,
  ],
  standalone: true,
})
export class UserCollectionsPageComponent implements OnInit {
  readonly username: string;
  collectionData: PageData<CollectionData>;
  searchCollectionString: string;
  debouncedSearchCollections: () => void;

  constructor(
    private userService: UserService,
    private route: ActivatedRoute,
    private title: Title,
    private utilsService: UtilsService,
  ) {
    this.username = route.snapshot.params['username'];
    this.title.setTitle(`${this.username}'s Collections | Showcased`);
  }

  async ngOnInit() {
    try {
      this.collectionData = await this.userService.getPublicCollections(this.username);
    } catch (error) {
      console.error(error);
    }

    this.debouncedSearchCollections = this.utilsService.debounce(() => {
      this.searchCollections();
    });
  }

  async searchCollections() {
    try {
      this.collectionData = await this.userService.getPublicCollections(this.username, this.searchCollectionString);
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
      const response = await this.userService.getPublicCollections(this.username, this.searchCollectionString, this.collectionData.page.number + 2);
      this.collectionData.content.push(...response.content);
      this.collectionData.page = response.page;
    } catch (error) {
      console.error(error);
    }
  }
}
