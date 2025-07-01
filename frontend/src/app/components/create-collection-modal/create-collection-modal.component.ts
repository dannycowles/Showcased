import {Component, Input} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {UtilsService} from '../../services/utils.service';
import {ProfileService} from '../../services/profile.service';

@Component({
  selector: 'app-create-collection-modal',
  imports: [FormsModule],
  templateUrl: './create-collection-modal.component.html',
  styleUrl: './create-collection-modal.component.css',
  standalone: true,
})
export class CreateCollectionModalComponent {
  newCollectionName: string = "";
  @Input({required: true}) onCreate: (collection: {}) => void;

  constructor (public activeModal: NgbActiveModal,
               public utilsService: UtilsService,
               private profileService: ProfileService) {};

  async createNewCollection() {
    const collectionMessage = document.getElementById("collectionError")
    try {
      const data = {
        collectionName: this.newCollectionName
      };

      const response = await this.profileService.createCollection(data);
      if (response.ok) {
        collectionMessage.innerText = "Collection created!";
        collectionMessage.style.color = "green";
        const newCollection = await response.json();
        this.onCreate(newCollection);
      } else {
        collectionMessage.innerText = "You already have a collection with this name!";
        collectionMessage.style.color = "red";
      }
    } catch(error) {
      console.error(error);
    } finally {
      setTimeout(() => collectionMessage.innerText = "", 3000);
    }
  }
}
