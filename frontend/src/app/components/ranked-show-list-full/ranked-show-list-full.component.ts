import {Component, ElementRef, EventEmitter, Input, Output, ViewChild} from '@angular/core';
import {RouterLink} from '@angular/router';
import {NgOptimizedImage} from '@angular/common';
import {CdkDrag, CdkDragDrop, CdkDragHandle, CdkDragMove, CdkDropList, moveItemInArray} from '@angular/cdk/drag-drop';
import {ShowRankingData} from '../../data/lists/show-ranking-data';
import {ConfirmationService} from '../../services/confirmation.service';

@Component({
  selector: 'app-ranked-show-list-full',
  imports: [
    RouterLink,
    NgOptimizedImage,
    CdkDropList,
    CdkDrag,
    CdkDragHandle
  ],
  templateUrl: './ranked-show-list-full.component.html',
  styleUrl: './ranked-show-list-full.component.css',
  standalone: true,
})
export class RankedShowListFullComponent {
  @Input({ required: true }) shows: ShowRankingData[];

  @Output() update = new EventEmitter<void>();
  @Output() remove = new EventEmitter<number>();

  @ViewChild('scrollableContainer') scrollableContainer!: ElementRef<HTMLElement>;
  private readonly scrollThreshold = 100;
  private readonly scrollSpeed = 10;

  constructor(private confirmationService: ConfirmationService) {}

  drop(event: CdkDragDrop<string[]>) {
    moveItemInArray(this.shows, event.previousIndex, event.currentIndex);

    // Update the rank numbers based on the index within the updated list
    this.shows.forEach((show, index) => {
      show.rankNum = index + 1;
    });
    this.update.emit();
  }

  moveToPosition(event: any, currentIndex: number) {
    const inputElement = event.target as HTMLInputElement;
    const newIndex: number = +event.target.value;

    // Validate input aka must be between 1 and size of shows
    if (isNaN(newIndex) || newIndex < 1 || newIndex > this.shows.length) {
      // Reset to the current rank number
      inputElement.value = String(this.shows[currentIndex].rankNum);
      return;
    }

    moveItemInArray(this.shows, currentIndex, newIndex - 1);

    // Update the rank numbers based on the index within the updated list
    this.shows.forEach((show, index) => {
      show.rankNum = index + 1;
    });
    this.update.emit();
  }

  async removeEvent(removeShow: ShowRankingData) {
    const confirmation = await this.confirmationService.confirmRemove(removeShow.title);
    if (confirmation) {
      this.remove.emit(removeShow.showId);
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
