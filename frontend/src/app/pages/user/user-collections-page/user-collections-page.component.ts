import {Component, OnInit} from '@angular/core';
import {UserService} from '../../../services/user.service';
import {ActivatedRoute} from '@angular/router';
import {CollectionData} from '../../../data/collection-data';

@Component({
  selector: 'app-user-collections-page',
  templateUrl: './user-collections-page.component.html',
  styleUrl: './user-collections-page.component.css',
  standalone: false
})
export class UserCollectionsPageComponent implements OnInit {
  readonly userId: number;
  collections: CollectionData[];

  constructor(private userService: UserService,
              private route: ActivatedRoute) {
    this.userId = route.snapshot.params['id'];
  };

  async ngOnInit() {
    try {
      this.collections = await this.userService.getPublicCollections(this.userId);
    } catch (error) {
      console.error(error);
    }
  };

}
