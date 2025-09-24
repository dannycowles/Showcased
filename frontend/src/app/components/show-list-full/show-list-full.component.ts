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
  @Input({required : true}) shows: ShowListData[];
}
