import {Component, OnInit} from '@angular/core';
import {UserService} from '../../../services/user.service';
import {ActivatedRoute, Router} from '@angular/router';
import {SingleCollectionData} from '../../../data/single-collection-data';

@Component({
  selector: 'app-user-collection-details-page',
  templateUrl: './user-collection-details-page.component.html',
  styleUrl: './user-collection-details-page.component.css',
  standalone: false
})
export class UserCollectionDetailsPageComponent implements OnInit {
  readonly userId: number;
  readonly collectionId: number;
  collection: SingleCollectionData;

  constructor(private userService: UserService,
              private route: ActivatedRoute,
              private router: Router) {
    this.userId = route.snapshot.params['id'];
    this.collectionId = route.snapshot.params['collectionId'];
  };

  async ngOnInit() {
    try {
      this.collection = await this.userService.getCollectionDetails(this.userId, this.collectionId);
    } catch (error) {
      console.error(error);
      this.router.navigate(['not-found']);
    }
  };

  async toggleLikeState() {
    try {
      this.collection.likedByUser = !this.collection.likedByUser;
      if (this.collection.likedByUser) {
        await this.userService.likeCollection(this.collectionId);
        this.collection.numLikes++;
      } else {
        await this.userService.unlikeCollection(this.collectionId);
        this.collection.numLikes--;
      }
    } catch (error) {
      console.error(error);
    }
  }
}
