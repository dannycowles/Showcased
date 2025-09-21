import {booleanAttribute, Component, Input} from '@angular/core';
import {SeasonRankingData} from '../../data/lists/season-ranking-data';
import {RouterLink} from '@angular/router';
import {NgOptimizedImage} from '@angular/common';

@Component({
  selector: 'app-season-list',
  imports: [
    RouterLink,
    NgOptimizedImage
  ],
  templateUrl: './season-list.component.html',
  styleUrl: './season-list.component.css',
  standalone: true
})
export class SeasonListComponent {
  @Input({required: true}) seasons: SeasonRankingData[];
  @Input({transform: booleanAttribute}) editable: boolean = false;
}
