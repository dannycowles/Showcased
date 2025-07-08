import {Component, Input} from '@angular/core';
import {DynamicRankingData} from '../../data/lists/dynamic-ranking-data';

@Component({
  selector: 'app-dynamic-list',
  imports: [],
  templateUrl: './dynamic-list.component.html',
  styleUrl: './dynamic-list.component.css',
  standalone: true
})
export class DynamicListComponent {
  @Input({required: true}) dynamics: DynamicRankingData[];
}
