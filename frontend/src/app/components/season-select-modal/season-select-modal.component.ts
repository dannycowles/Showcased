import {Component, Input, OnInit} from '@angular/core';
import {NgbActiveModal, NgbDropdownModule} from '@ng-bootstrap/ng-bootstrap';
import {ShowService} from '../../services/show.service';
import {SeasonRankingData} from '../../data/lists/season-ranking-data';
import {ProfileService} from '../../services/profile.service';
import {AddToSeasonRankingList} from '../../data/dto/add-to-list-dto';

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

  // These are used when the component using the modal is attempting to add a season to the user's list
  @Input() seasonRanking: boolean = false;
  @Input() onAddSeason: (season: {}) => void;
  message: string = "";
  messageColor: string;

  constructor (public activeModal: NgbActiveModal,
               private showService: ShowService,
               private profileService: ProfileService) {};

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

  async addSeasonToRankingList() {
    try {
      const data: AddToSeasonRankingList = {
        showId: this.selectedShowId,
        season: this.selectedSeason,
        showTitle: this.selectedShowTitle,
        posterPath: ''
      }
      const response = await this.profileService.addSeasonToRankingList(data);

      if (response.ok) {
        const newRanking: SeasonRankingData = await response.json();
        this.message = `Added ${this.selectedShowTitle} Season ${this.selectedSeason} to your ranking list!`;
        this.messageColor = "green";
        this.onAddSeason(newRanking);
      } else {
        this.message = `You already have ${this.selectedShowTitle} Season ${this.selectedSeason} on your ranking list.`;
        this.messageColor = "red";
      }
    } catch (error) {
      console.error(error);
    } finally {
      setTimeout(() => {
        this.message = "";
      }, 3000);
    }
  }
}
