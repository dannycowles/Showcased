import {booleanAttribute, Component, EventEmitter, Input, Output} from '@angular/core';
import {EpisodeRankingData} from '../../data/lists/episode-ranking-data';
import {CdkDrag, CdkDragDrop, CdkDropList, moveItemInArray} from '@angular/cdk/drag-drop';
import {RouterLink} from '@angular/router';
import {NgOptimizedImage} from '@angular/common';

@Component({
  selector: 'app-ranked-episode-list-full',
  imports: [
    CdkDropList,
    CdkDrag,
    RouterLink,
    NgOptimizedImage
  ],
  templateUrl: './ranked-episode-list-full.component.html',
  styleUrl: './ranked-episode-list-full.component.css'
})
export class RankedEpisodeListFullComponent {
  @Input({required : true}) title: string;
  @Input({required : true}) episodes: EpisodeRankingData[];
  @Input({transform : booleanAttribute}) editable: boolean = false;

  @Output() update = new EventEmitter<void>();
  @Output() remove = new EventEmitter<number>();

  drop(event: CdkDragDrop<string[]>) {
    moveItemInArray(this.episodes, event.previousIndex, event.currentIndex);

    // Update the rank numbers based on the index within the updated list
    this.episodes.forEach((episode, index) => {
      episode.rankNum = index + 1;
    });
    this.update.emit();
  }

  removeEvent(removeId: number) {
    this.remove.emit(removeId);
  }
}
