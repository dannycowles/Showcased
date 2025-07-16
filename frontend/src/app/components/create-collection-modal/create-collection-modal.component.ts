import {Component, Input} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {UtilsService} from '../../services/utils.service';
import {ProfileService} from '../../services/profile.service';
import {CreateCollectionDto} from '../../data/dto/create-collection-dto';

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
  message: string = "";
  messageColor: string = "";

  readonly maxNameLength = 100;

  constructor (public activeModal: NgbActiveModal,
               public utilsService: UtilsService,
               private profileService: ProfileService) {};

  async createNewCollection() {
    try {
      const data: CreateCollectionDto = {
        collectionName: this.newCollectionName
      };

      const response = await this.profileService.createCollection(data);
      if (response.ok) {
        this.message = "Collection created!";
        this.messageColor = "green";
        const newCollection = await response.json();
        this.onCreate(newCollection);
      } else {
        this.message = "You already have a collection with this name!";
        this.messageColor = "red";
      }
    } catch(error) {
      console.error(error);
    } finally {
      setTimeout(() => this.message = "", 3000);
    }
  }
}
