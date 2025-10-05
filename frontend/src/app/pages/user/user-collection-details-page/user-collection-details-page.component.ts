import {Component, OnInit} from '@angular/core';
import {UserService} from '../../../services/user.service';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import {SingleCollectionData} from '../../../data/single-collection-data';
import {Title} from '@angular/platform-browser';
import {ButtonHeartComponent} from '../../../components/button-heart.component';
import {NgOptimizedImage} from '@angular/common';
import {ShowListFullComponent} from '../../../components/show-list-full/show-list-full.component';

@Component({
  selector: 'app-user-collection-details-page',
  templateUrl: './user-collection-details-page.component.html',
  styleUrl: './user-collection-details-page.component.css',
  imports: [RouterLink, ButtonHeartComponent, NgOptimizedImage, ShowListFullComponent],
  standalone: true,
})
export class UserCollectionDetailsPageComponent implements OnInit {
  readonly username: string;
  readonly collectionId: number;
  collectionData: SingleCollectionData;
  loadingData: boolean = true;

  constructor(
    private userService: UserService,
    private route: ActivatedRoute,
    private router: Router,
    private title: Title,
  ) {
    this.username = this.route.snapshot.params['username'];
    this.collectionId = this.route.snapshot.params['collectionId'];
  }

  async ngOnInit() {
    try {
      this.collectionData = await this.userService.getCollectionDetails(this.collectionId);
      this.title.setTitle(`${this.collectionData.name}, ${this.username}'s Collection | Showcased`);
    } catch (error) {
      console.error(error);
      this.router.navigate(['not-found']);
    } finally {
      this.loadingData = false;
    }
  }

  async toggleLikeState() {
    try {
      this.collectionData.isLikedByUser = !this.collectionData.isLikedByUser;
      if (this.collectionData.isLikedByUser) {
        const response = await this.userService.likeCollection(this.collectionId);

        if (response.status === 401) {
          this.router.navigate(['login']);
        } else {
          this.collectionData.numLikes++;
        }
      } else {
        const response = await this.userService.unlikeCollection(this.collectionId);

        if (response.status === 401) {
          this.router.navigate(['login']);
        } else {
          this.collectionData.numLikes--;
        }
      }
    } catch (error) {
      console.error(error);
    }
  }
}
