import {Component, Input} from '@angular/core';
import {DynamicRankingData} from '../../data/lists/dynamic-ranking-data';

@Component({
  selector: 'app-dynamic-list-full',
  imports: [],
  templateUrl: './dynamic-list-full.component.html',
  styleUrl: './dynamic-list-full.component.css'
})
export class DynamicListFullComponent {
  @Input({required: true}) dynamics: DynamicRankingData[];
}
