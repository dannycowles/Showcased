import {booleanAttribute, Component, EventEmitter, Input, Output} from '@angular/core';
import {CdkDrag, CdkDragDrop, CdkDropList, moveItemInArray} from '@angular/cdk/drag-drop';
import {DynamicRankingData} from '../../data/lists/dynamic-ranking-data';

@Component({
  selector: 'app-ranked-dynamic-list-full',
  imports: [CdkDrag, CdkDropList],
  templateUrl: './ranked-dynamic-list-full.component.html',
  styleUrl: './ranked-dynamic-list-full.component.css',
  standalone: true,
})
export class RankedDynamicListFullComponent {
  @Input({required: true}) dynamics: DynamicRankingData[];
  @Input({transform: booleanAttribute}) editable: boolean = false;

  @Output() update = new EventEmitter<void>();
  @Output() remove = new EventEmitter<number>();

  drop(event: CdkDragDrop<DynamicRankingData[]>) {
    moveItemInArray(this.dynamics, event.previousIndex, event.currentIndex);

    // Update the object rank numbers based on the index
    this.dynamics.forEach((dynamic, index) => {
      dynamic.rankNum = index + 1;
    });
    this.update.emit();
  }

  removeEvent(removeId: number) {
    this.remove.emit(removeId);
  }
}
