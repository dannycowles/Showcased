import {Component, Input} from '@angular/core';
import {RouterLink} from '@angular/router';
import {NgOptimizedImage} from '@angular/common';
import {ShowListData} from '../../data/lists/show-list-data';

@Component({
  selector: 'app-show-list',
  imports: [
    RouterLink,
    NgOptimizedImage
  ],
  templateUrl: './show-list.component.html',
  styleUrl: './show-list.component.css',
  standalone: true
})
export class ShowListComponent {
  @Input({required : true}) title: string;
  @Input({required : true}) shows: ShowListData[];
}
