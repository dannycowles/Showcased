import {Component, OnInit} from '@angular/core';
import {ProfileService} from '../../../services/profile.service';
import {SingleCollectionData} from '../../../data/single-collection-data';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import {UtilsService} from '../../../services/utils.service';
import {UserService} from '../../../services/user.service';
import {Title} from '@angular/platform-browser';
import { ButtonHeartComponent } from '../../../components/button-heart.component';
import {NgOptimizedImage} from '@angular/common';
import {ShowListFullComponent} from '../../../components/show-list-full/show-list-full.component';

@Component({
  selector: 'app-profile-collection-details-page',
  templateUrl: './profile-collection-details-page.component.html',
  styleUrl: './profile-collection-details-page.component.css',
  imports: [
    ButtonHeartComponent,
    RouterLink,
    NgOptimizedImage,
    ShowListFullComponent,
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
    private title: Title,
  ) {
    this.collectionId = this.route.snapshot.params['id'];
  }

  async ngOnInit() {
    try {
      this.collectionData = await this.profileService.getCollectionDetails(
        this.collectionId,
      );
      this.title.setTitle(`${this.collectionData.name}, Your Collection | Showcased`);
    } catch (error) {
      console.error(error);
      this.router.navigate(['not-found']);
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
