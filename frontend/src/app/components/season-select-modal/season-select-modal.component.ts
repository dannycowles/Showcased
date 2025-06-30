import {Component, Input, OnInit} from '@angular/core';
import {NgbActiveModal, NgbDropdownModule} from '@ng-bootstrap/ng-bootstrap';
import {ShowService} from '../../services/show.service';

@Component({
  selector: 'app-season-select-modal',
  imports: [NgbDropdownModule],
  templateUrl: './season-select-modal.component.html',
  styleUrl: './season-select-modal.component.css',
  standalone: true
})
export class SeasonSelectModalComponent implements OnInit {
  @Input({required: true}) selectedShowId: number;
  @Input({required: true}) selectedShowTitle: string;
  numSeasons: number | null = null;
  selectedSeason: number = 1;

  message: string = "";
  messageColor: string;

  constructor (public activeModal: NgbActiveModal,
               private showService: ShowService) {};

  async ngOnInit() {
    await this.getNumberOfSeasons();
  }

  async getNumberOfSeasons() {
    try {
      this.numSeasons = await this.showService.fetchNumberOfSeasons(this.selectedShowId);
    } catch (error) {
      console.error(error);
    }
  }

  passBack() {
    this.activeModal.close({selectedSeason: this.selectedSeason});
  }

  goBack() {
    this.activeModal.dismiss("backFromSeason");
  }
}
