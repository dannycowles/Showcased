import {booleanAttribute, Component, Input} from '@angular/core';
import {NgOptimizedImage} from '@angular/common';
import {RouterLink} from '@angular/router';
import {ProfileShow} from '../../data/types';
import {ShowRankingData} from '../../data/lists/show-ranking-data';

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
  @Input({required : true}) shows: ProfileShow[];
  @Input({transform: booleanAttribute}) ranked = false;

  isShowRankingData(show: ProfileShow): show is ShowRankingData {
    return 'rankNum' in show;
  }
}
