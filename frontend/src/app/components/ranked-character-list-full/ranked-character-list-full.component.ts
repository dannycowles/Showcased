import {Component, ElementRef, EventEmitter, Input, Output, ViewChild} from '@angular/core';
import {CdkDrag, CdkDragDrop, CdkDragHandle, CdkDragMove, CdkDropList, moveItemInArray} from '@angular/cdk/drag-drop';
import {CharacterRankingData} from '../../data/lists/character-ranking-data';
import {ConfirmationService} from '../../services/confirmation.service';
import {NgOptimizedImage} from '@angular/common';

@Component({
  selector: 'app-ranked-character-list-full',
  imports: [CdkDropList, CdkDrag, CdkDragHandle, NgOptimizedImage],
  templateUrl: './ranked-character-list-full.component.html',
  styleUrl: './ranked-character-list-full.component.css',
  standalone: true,
})
export class RankedCharacterListFullComponent {
  @Input({ required: true }) characters: CharacterRankingData[];
  @Input({ required: true }) characterType: string;

  @Output() update = new EventEmitter<void>();
  @Output() remove = new EventEmitter<string>();

  @ViewChild('scrollableContainer') scrollableContainer!: ElementRef<HTMLElement>;
  private readonly scrollThreshold = 50;
  private readonly scrollSpeed = 10;

  constructor(private confirmationService: ConfirmationService) {}

  drop(event: CdkDragDrop<string[]>) {
    moveItemInArray(this.characters, event.previousIndex, event.currentIndex);

    // Update the rank numbers based on the index within the updated list
    this.characters.forEach((character, index) => {
      character.rankNum = index + 1;
    });
    this.update.emit();
  }

  moveToPosition(event: any, currentIndex: number) {
    const inputElement = event.target as HTMLInputElement;
    const newIndex: number = +event.target.value;

    // Validate input aka must be between 1 and size of characters
    if (isNaN(newIndex) || newIndex < 1 || newIndex > this.characters.length) {
      // Reset to the current rank number
      inputElement.value = String(this.characters[currentIndex].rankNum);
      return;
    }

    moveItemInArray(this.characters, currentIndex, newIndex - 1);

    // Update the rank numbers based on the index within the updated list
    this.characters.forEach((character, index) => {
      character.rankNum = index + 1;
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

  async removeEvent(removeCharacter: CharacterRankingData) {
    const confirmation = await this.confirmationService.confirmRemove(
      removeCharacter.name,
    );
    if (confirmation) {
      this.remove.emit(removeCharacter.id);
    }
  }
}
