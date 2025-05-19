import {Component, OnInit} from '@angular/core';
import {ProfileService} from '../../../services/profile.service';
import {SingleCollectionData} from '../../../data/single-collection-data';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-profile-collection-details-page',
  templateUrl: './profile-collection-details-page.component.html',
  styleUrl: './profile-collection-details-page.component.css',
  standalone: false
})
export class ProfileCollectionDetailsPageComponent implements OnInit {
  collectionData: SingleCollectionData;
  readonly id: number;

  constructor(private profileService: ProfileService,
              private route: ActivatedRoute) {
    this.id = this.route.snapshot.params['id'];
  };

  async ngOnInit() {
    try {
      this.collectionData = await this.profileService.getCollectionDetails(this.id);
    } catch (error) {
      console.error(error);
    }
  };
}
