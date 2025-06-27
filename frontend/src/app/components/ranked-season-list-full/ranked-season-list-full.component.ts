import {booleanAttribute, Component, EventEmitter, Input, Output} from '@angular/core';
import {SeasonRankingData} from '../../data/lists/season-ranking-data';
import {CdkDrag, CdkDragDrop, CdkDropList, DragDropModule, moveItemInArray} from '@angular/cdk/drag-drop';
import {RouterLink} from '@angular/router';
import {NgOptimizedImage} from '@angular/common';

@Component({
  selector: 'app-ranked-season-list-full',
  imports: [
    RouterLink,
    NgOptimizedImage,
    DragDropModule
  ],
  templateUrl: './ranked-season-list-full.component.html',
  styleUrl: './ranked-season-list-full.component.css',
  standalone: true
})
export class RankedSeasonListFullComponent {
  @Input({required : true}) title: string;
  @Input({required : true}) seasons: SeasonRankingData[];
  @Input({transform : booleanAttribute}) editable: boolean = false;

  @Output() update = new EventEmitter<void>();
  @Output() remove = new EventEmitter<number>();

  drop(event: CdkDragDrop<string[]>) {
    moveItemInArray(this.seasons, event.previousIndex, event.currentIndex);

    // Update the rank numbers based on the index within the updated list
    this.seasons.forEach((season, index) => {
      season.rankNum = index + 1;
    });
    this.update.emit();
  }

  removeEvent(removeId: number) {
    this.remove.emit(removeId);
  }
}
