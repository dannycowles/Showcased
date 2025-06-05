import {booleanAttribute, Component, EventEmitter, Input, Output} from '@angular/core';
import {RouterLink} from '@angular/router';
import {NgOptimizedImage} from '@angular/common';
import {CdkDrag, CdkDragDrop, CdkDropList, moveItemInArray} from '@angular/cdk/drag-drop';
import {ShowRankingData} from '../../data/lists/show-ranking-data';

@Component({
  selector: 'app-ranked-show-list-full',
  imports: [
    RouterLink,
    NgOptimizedImage,
    CdkDropList,
    CdkDrag
  ],
  templateUrl: './ranked-show-list-full.component.html',
  styleUrl: './ranked-show-list-full.component.css',
  standalone: true
})
export class RankedShowListFullComponent {
  @Input({required : true}) title: string;
  @Input({required : true}) shows: ShowRankingData[];
  @Input({transform: booleanAttribute}) editable: boolean = false;

  @Output() update = new EventEmitter<void>();
  @Output() remove = new EventEmitter<number>();

  drop(event: CdkDragDrop<string[]>) {
    moveItemInArray(this.shows, event.previousIndex, event.currentIndex);

    // Update the rank numbers based on the index within the updated list
    this.shows.forEach((show, index) => {
      show.rankNum = index + 1;
    });
    this.updateEvent();
  }

  updateEvent() {
    this.update.emit();
  }

  removeEvent(removeId: number) {
    this.remove.emit(removeId);
  }
}
