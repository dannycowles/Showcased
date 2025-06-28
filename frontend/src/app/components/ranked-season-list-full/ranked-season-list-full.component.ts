import {booleanAttribute, Component, ElementRef, EventEmitter, Input, Output, ViewChild} from '@angular/core';
import {SeasonRankingData} from '../../data/lists/season-ranking-data';
import {CdkDrag, CdkDragDrop, CdkDragMove, CdkDropList, DragDropModule, moveItemInArray} from '@angular/cdk/drag-drop';
import {RouterLink} from '@angular/router';
import {NgOptimizedImage} from '@angular/common';
import {CdkScrollableModule} from '@angular/cdk/scrolling';

@Component({
  selector: 'app-ranked-season-list-full',
  imports: [
    RouterLink,
    NgOptimizedImage,
    DragDropModule,
    CdkScrollableModule
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

  @ViewChild("scrollableContainer") scrollableContainer!: ElementRef<HTMLElement>;
  private scrollThreshold = 50;
  private scrollSpeed = 10;

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
