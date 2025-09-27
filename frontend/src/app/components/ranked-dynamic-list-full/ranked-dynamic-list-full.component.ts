import {booleanAttribute, Component, ElementRef, EventEmitter, Input, Output, ViewChild} from '@angular/core';
import {CdkDrag, CdkDragDrop, CdkDragHandle, CdkDragMove, CdkDropList, moveItemInArray} from '@angular/cdk/drag-drop';
import {DynamicRankingData} from '../../data/lists/dynamic-ranking-data';
import {ConfirmationService} from '../../services/confirmation.service';
import {NgOptimizedImage} from '@angular/common';

@Component({
  selector: 'app-ranked-dynamic-list-full',
  imports: [CdkDrag, CdkDropList, CdkDragHandle, NgOptimizedImage],
  templateUrl: './ranked-dynamic-list-full.component.html',
  styleUrl: './ranked-dynamic-list-full.component.css',
  standalone: true,
})
export class RankedDynamicListFullComponent {
  @Input({ required: true }) dynamics: DynamicRankingData[];
  @Input({ transform: booleanAttribute }) editable: boolean = false;

  @Output() update = new EventEmitter<void>();
  @Output() remove = new EventEmitter<number>();

  @ViewChild('scrollableContainer') scrollableContainer!: ElementRef<HTMLElement>;
  private readonly scrollThreshold = 50;
  private readonly scrollSpeed = 10;

  constructor(private confirmationService: ConfirmationService) {}

  drop(event: CdkDragDrop<DynamicRankingData[]>) {
    moveItemInArray(this.dynamics, event.previousIndex, event.currentIndex);

    // Update the object rank numbers based on the index
    this.dynamics.forEach((dynamic, index) => {
      dynamic.rankNum = index + 1;
    });
    this.update.emit();
  }

  moveToPosition(event: any, currentIndex: number) {
    const inputElement = event.target as HTMLInputElement;
    const newIndex: number = +event.target.value;

    // Validate input aka must be between 1 and size of dynamics
    if (isNaN(newIndex) || newIndex < 1 || newIndex > this.dynamics.length) {
      // Reset to the current rank number
      inputElement.value = String(this.dynamics[currentIndex].rankNum);
      return;
    }

    moveItemInArray(this.dynamics, currentIndex, newIndex - 1);

    // Update the rank numbers based on the index within the updated list
    this.dynamics.forEach((dynamic, index) => {
      dynamic.rankNum = index + 1;
    });
    this.update.emit();
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

  async removeEvent(removeDynamic: DynamicRankingData) {
    const confirmation = await this.confirmationService.confirmRemove(`${removeDynamic.character1Name} & ${removeDynamic.character2Name}`);
    if (confirmation) {
      this.remove.emit(removeDynamic.id);
    }
  }
}
