import {Component, OnInit} from '@angular/core';
import {UserService} from '../../../services/user.service';
import {ActivatedRoute} from '@angular/router';
import {CollectionData} from '../../../data/collection-data';
import {UtilsService} from '../../../services/utils.service';

@Component({
  selector: 'app-user-collections-page',
  templateUrl: './user-collections-page.component.html',
  styleUrl: './user-collections-page.component.css',
  standalone: false
})
export class UserCollectionsPageComponent implements OnInit {
  readonly username: string;
  collections: CollectionData[];
  newCollectionName: string;
  debouncedSearchCollections: () => void;

  constructor(private userService: UserService,
              private route: ActivatedRoute,
              private utilsService: UtilsService) {
    this.username = route.snapshot.params['username'];
  };

  async ngOnInit() {
    try {
      this.collections = await this.userService.getPublicCollections(this.username);
    } catch (error) {
      console.error(error);
    }

    this.debouncedSearchCollections = this.utilsService.debounce(() => {
      this.searchCollections();
    })

  };

  async searchCollections() {
    try {
      this.collections = await this.userService.getPublicCollections(this.username, this.newCollectionName);
    } catch (error) {
      console.error(error);
    }
  }

}
