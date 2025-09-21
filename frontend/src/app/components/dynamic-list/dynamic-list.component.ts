import {booleanAttribute, Component, Input} from '@angular/core';
import {DynamicRankingData} from '../../data/lists/dynamic-ranking-data';
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-dynamic-list',
  imports: [RouterLink],
  templateUrl: './dynamic-list.component.html',
  styleUrl: './dynamic-list.component.css',
  standalone: true,
})
export class DynamicListComponent {
  @Input({ required: true }) dynamics: DynamicRankingData[];
  @Input({transform: booleanAttribute}) editable = false;
}
