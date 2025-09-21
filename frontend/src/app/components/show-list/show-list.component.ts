import {booleanAttribute, Component, Input} from '@angular/core';
import {RouterLink} from '@angular/router';
import {NgOptimizedImage} from '@angular/common';
import {ShowListData} from '../../data/lists/show-list-data';
import {ShowRankingData} from '../../data/lists/show-ranking-data';

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
  @Input({required : true}) shows: ShowListData[] | ShowRankingData[];
  @Input({transform: booleanAttribute}) ranked: boolean = false;
  @Input({transform: booleanAttribute}) editable: boolean = false;
}
