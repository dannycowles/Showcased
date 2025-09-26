import {Component, ElementRef, EventEmitter, Input, Output, ViewChild} from '@angular/core';
import {SeasonRankingData} from '../../data/lists/season-ranking-data';
import {CdkDragDrop, CdkDragMove, DragDropModule, moveItemInArray} from '@angular/cdk/drag-drop';
import {RouterLink} from '@angular/router';
import {NgOptimizedImage} from '@angular/common';
import {ConfirmationService} from '../../services/confirmation.service';

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
  @Input({required : true}) seasons: SeasonRankingData[];

  @Output() update = new EventEmitter<void>();
  @Output() remove = new EventEmitter<number>();

  @ViewChild("scrollableContainer") scrollableContainer!: ElementRef<HTMLElement>;
  private readonly scrollThreshold = 100;
  private readonly scrollSpeed = 10;

  constructor(private confirmationService: ConfirmationService) {}

  drop(event: CdkDragDrop<string[]>) {
    moveItemInArray(this.seasons, event.previousIndex, event.currentIndex);

    // Update the rank numbers based on the index within the updated list
    this.seasons.forEach((season, index) => {
      season.rankNum = index + 1;
    });
    this.update.emit();
  }

  moveToPosition(event: any, currentIndex: number) {
    const inputElement = event.target as HTMLInputElement;
    const newIndex: number = +event.target.value;

    // Validate input aka must be between 1 and size of seasons
    if (isNaN(newIndex) || newIndex < 1 || newIndex > this.seasons.length) {
      // Reset to the current rank number
      inputElement.value = String(this.seasons[currentIndex].rankNum);
      return;
    }

    moveItemInArray(this.seasons, currentIndex, newIndex - 1);

    // Update the rank numbers based on the index within the updated list
    this.seasons.forEach((season, index) => {
      season.rankNum = index + 1;
    });
    this.update.emit();
  }

  async removeEvent(removeSeason: SeasonRankingData) {
    const confirmation = await this.confirmationService.confirmRemove(`${removeSeason.showTitle} S${removeSeason.season}`);
    if (confirmation) {
      this.remove.emit(removeSeason.id);
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
