import {Component, Input, OnInit} from '@angular/core';
import {NgbActiveModal, NgbDropdownModule} from '@ng-bootstrap/ng-bootstrap';
import {ShowService} from '../../services/show.service';
import {SeasonEpisode, SeasonEpisodes} from '../../data/show/season-episode';
import {ProfileService} from '../../services/profile.service';
import {AddToEpisodeRankingList} from '../../data/dto/add-to-list-dto';

@Component({
  selector: 'app-episode-select-modal',
  imports: [NgbDropdownModule],
  templateUrl: './episode-select-modal.component.html',
  styleUrl: './episode-select-modal.component.css',
  standalone: true,
})
export class EpisodeSelectModalComponent implements OnInit {
  @Input({ required: true }) selectedShowId: number | null;
  @Input({ required: true }) selectedShowTitle: string;
  @Input({ required: true }) selectedSeason: number;
  @Input({ required: true }) onAddEpisode: (episode: {}) => void;
  seasonEpisodes: SeasonEpisodes | null = null;
  selectedEpisode: SeasonEpisode | null = null;

  message: string = "";
  messageColor: string = "";

  constructor(public activeModal: NgbActiveModal,
              private showService: ShowService,
              private profileService: ProfileService) {};

  async ngOnInit() {
    try {
      this.seasonEpisodes = await this.showService.fetchSeasonEpisodes(this.selectedShowId, this.selectedSeason);
      this.selectedEpisode = this.seasonEpisodes.episodes[0];
    } catch (error) {
      console.error(error);
    }
  }

  async addEpisodeToRankingList() {
    try {
      const data: AddToEpisodeRankingList = {
        episodeId: this.selectedEpisode.id,
        showId: this.selectedShowId,
        showTitle: this.selectedShowTitle,
        episodeTitle: this.selectedEpisode.name,
        season: this.selectedSeason,
        episode: this.selectedEpisode.episodeNumber,
        posterPath: this.selectedEpisode.stillPath
      }
      const response = await this.profileService.addEpisodeToRankingList(data);

      if (response.ok) {
        this.message = `Added ${this.selectedShowTitle} Season ${this.selectedSeason} Episode ${this.selectedEpisode.episodeNumber} to your ranking list!`;
        this.messageColor = "green";
        this.onAddEpisode(data);
      } else {
        this.message = `You already have ${this.selectedShowTitle} Season ${this.selectedSeason} Episode ${this.selectedEpisode.episodeNumber} on your ranking list.`;
        this.messageColor = "red";
      }
    } catch (error) {
      console.error(error);
    } finally {
      setTimeout(() => this.message = "", 3000);
    }
  }

  goBack() {
    this.activeModal.dismiss('backFromEpisode');
  }
}
