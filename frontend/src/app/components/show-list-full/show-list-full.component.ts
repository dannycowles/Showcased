import {booleanAttribute, Component, EventEmitter, Input, Output} from '@angular/core';
import {NgOptimizedImage} from '@angular/common';
import {ShowListData} from '../../data/lists/show-list-data';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-show-list-full',
  imports: [
    NgOptimizedImage,
    RouterLink
  ],
  templateUrl: './show-list-full.component.html',
  styleUrl: './show-list-full.component.css',
  standalone: true
})
export class ShowListFullComponent {
  @Input({required : true}) title: string;
  @Input({required : true}) shows: ShowListData[];
  @Input({transform: booleanAttribute}) editable: boolean = false;
  @Input() moveTitle: string = "";

  @Output() remove = new EventEmitter<number>();
  @Output() move = new EventEmitter<number>();

  removeEvent(removeId: number) {
    this.remove.emit(removeId);
  }

  moveEvent(moveId: number) {
    this.move.emit(moveId);
  }
}
