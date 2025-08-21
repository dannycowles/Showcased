import {booleanAttribute, Component, EventEmitter, Input, Output} from '@angular/core';
import {NgOptimizedImage} from '@angular/common';
import {ShowListData} from '../../data/lists/show-list-data';
import {RouterLink} from '@angular/router';
import {ConfirmationService} from '../../services/confirmation.service';

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

  constructor(private confirmationService: ConfirmationService) {}

  async removeEvent(removeShow: ShowListData) {
    const confirmation = await this.confirmationService.confirmRemove(removeShow.title);
    if (confirmation) {
      this.remove.emit(removeShow.showId);
    }
  }

  moveEvent(moveId: number) {
    this.move.emit(moveId);
  }
}
