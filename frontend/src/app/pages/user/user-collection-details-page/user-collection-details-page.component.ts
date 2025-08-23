import {Component, OnInit} from '@angular/core';
import {UserService} from '../../../services/user.service';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import {SingleCollectionData} from '../../../data/single-collection-data';
import {Title} from '@angular/platform-browser';
import {ButtonHeartComponent} from '../../../components/button-heart.component';
import {NgOptimizedImage} from '@angular/common';

@Component({
  selector: 'app-user-collection-details-page',
  templateUrl: './user-collection-details-page.component.html',
  styleUrl: './user-collection-details-page.component.css',
  imports: [RouterLink, ButtonHeartComponent, NgOptimizedImage],
  standalone: true,
})
export class UserCollectionDetailsPageComponent implements OnInit {
  readonly username: string;
  readonly collectionId: number;
  collection: SingleCollectionData;

  constructor(
    private userService: UserService,
    private route: ActivatedRoute,
    private router: Router,
    private title: Title,
  ) {
    this.username = route.snapshot.params['username'];
    this.collectionId = route.snapshot.params['collectionId'];
  }

  async ngOnInit() {
    try {
      this.collection = await this.userService.getCollectionDetails(
        this.collectionId,
      );
      this.title.setTitle(
        `${this.collection.name}, ${this.username}'s Collection | Showcased`,
      );
    } catch (error) {
      console.error(error);
      this.router.navigate(['not-found']);
    }
  }

  async toggleLikeState() {
    try {
      this.collection.isLikedByUser = !this.collection.isLikedByUser;
      if (this.collection.isLikedByUser) {
        const response = await this.userService.likeCollection(
          this.collectionId,
        );

        if (response.status === 401) {
          this.router.navigate(['login']);
        } else {
          this.collection.numLikes++;
        }
      } else {
        const response = await this.userService.unlikeCollection(
          this.collectionId,
        );

        if (response.status === 401) {
          this.router.navigate(['login']);
        } else {
          this.collection.numLikes--;
        }
      }
    } catch (error) {
      console.error(error);
    }
  }
}
