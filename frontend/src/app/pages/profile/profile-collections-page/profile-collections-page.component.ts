import {Component, OnInit} from '@angular/core';
import {ProfileService} from '../../../services/profile.service';
import {CollectionData} from '../../../data/collection-data';
import {UtilsService} from '../../../services/utils.service';

@Component({
  selector: 'app-profile-collections-page',
  templateUrl: './profile-collections-page.component.html',
  styleUrl: './profile-collections-page.component.css',
  standalone: false
})
export class ProfileCollectionsPageComponent implements OnInit {
  collectionData: CollectionData[];
  searchCollectionString: string;
  newCollectionName: string;
  debouncedSearchCollections: () => void;

  constructor(private profileService: ProfileService,
              public utilsService: UtilsService) { };

  async ngOnInit() {
    try {
      this.collectionData = await this.profileService.getCollections();
    } catch (error) {
      console.error(error);
    }

    this.debouncedSearchCollections = this.utilsService.debounce(() => {
      this.searchCollections();
    });
  };

  async searchCollections() {
    try {
      this.collectionData = await this.profileService.getCollections(this.searchCollectionString);
    } catch (error) {
      console.error(error);
    }
  }

  async createNewCollection() {
    const collectionMessage = document.getElementById("collectionError")
    try {
      const data = {
        collectionName: this.newCollectionName
      };

      const response = await this.profileService.createCollection(data);
      if (response.ok) {
        this.collectionData = await this.profileService.getCollections();
        collectionMessage.innerText = "Collection created!";
        collectionMessage.style.color = "green";
      } else {
        collectionMessage.innerText = "You already have a collection with this name!";
        collectionMessage.style.color = "red";
      }
    } catch(error) {
      console.error(error);
    } finally {
      setTimeout(() => {
        collectionMessage.innerText = "";
      }, 3000);
    }
  }
}
