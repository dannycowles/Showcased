import {Component, OnInit} from '@angular/core';
import {ProfileService} from '../../../services/profile.service';
import {CollectionData} from '../../../data/collection-data';

@Component({
  selector: 'app-profile-collections-page',
  templateUrl: './profile-collections-page.component.html',
  styleUrl: './profile-collections-page.component.css',
  standalone: false
})
export class ProfileCollectionsPageComponent implements OnInit {
  collectionData: CollectionData[];

  constructor(private profileService: ProfileService) { };

  async ngOnInit() {
    try {
      this.collectionData = await this.profileService.getCollections();
    } catch (error) {
      console.error(error);
    }
  };
}
