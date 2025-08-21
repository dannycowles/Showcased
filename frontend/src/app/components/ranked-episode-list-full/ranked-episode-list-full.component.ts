import {booleanAttribute, Component, ElementRef, EventEmitter, Input, Output, ViewChild} from '@angular/core';
import {EpisodeRankingData} from '../../data/lists/episode-ranking-data';
import {CdkDrag, CdkDragDrop, CdkDragMove, CdkDropList, moveItemInArray} from '@angular/cdk/drag-drop';
import {RouterLink} from '@angular/router';
import {NgOptimizedImage} from '@angular/common';
import {CdkScrollableModule} from '@angular/cdk/scrolling';
import {SeasonRankingData} from '../../data/lists/season-ranking-data';
import {ConfirmationService} from '../../services/confirmation.service';

@Component({
  selector: 'app-ranked-episode-list-full',
  imports: [
    CdkDropList,
    CdkDrag,
    RouterLink,
    NgOptimizedImage,
    CdkScrollableModule,
  ],
  templateUrl: './ranked-episode-list-full.component.html',
  styleUrl: './ranked-episode-list-full.component.css',
})
export class RankedEpisodeListFullComponent {
  @Input({ required: true }) title: string;
  @Input({ required: true }) episodes: EpisodeRankingData[];
  @Input({ transform: booleanAttribute }) editable: boolean = false;

  @Output() update = new EventEmitter<void>();
  @Output() remove = new EventEmitter<number>();

  @ViewChild('scrollableContainer') scrollableContainer!: ElementRef<HTMLElement>;
  private readonly  scrollThreshold = 50;
  private readonly scrollSpeed = 10;

  constructor(private confirmationService: ConfirmationService) {}

  drop(event: CdkDragDrop<string[]>) {
    moveItemInArray(this.episodes, event.previousIndex, event.currentIndex);

    // Update the rank numbers based on the index within the updated list
    this.episodes.forEach((episode, index) => {
      episode.rankNum = index + 1;
    });
    this.update.emit();
  }

  async removeEvent(removeEpisode: EpisodeRankingData) {
    const confirmation = await this.confirmationService.confirmRemove(`${removeEpisode.showTitle} S${removeEpisode.season}E${removeEpisode.episode}`);
    if (confirmation) {
      this.remove.emit(removeEpisode.episodeId);
    }
  }

  onDragMoved(event: CdkDragMove) {
    const scrollElement = this.scrollableContainer.nativeElement;
    const { y } = event.pointerPosition;
    const rect = scrollElement.getBoundingClientRect();

    if (y - rect.top < this.scrollThreshold) {
      scrollElement.scrollTop -= this.scrollSpeed;
    } else if (rect.bottom - y < this.scrollThreshold) {
      scrollElement.scrollTop += this.scrollSpeed;
    }
  }
}
