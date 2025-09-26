import {Component, Input} from '@angular/core';
import {NgOptimizedImage} from '@angular/common';
import {RouterLink} from '@angular/router';
import {SeasonRankingData} from '../../data/lists/season-ranking-data';

@Component({
  selector: 'app-season-list-full',
  imports: [NgOptimizedImage, RouterLink],
  templateUrl: './season-list-full.component.html',
  styleUrl: './season-list-full.component.css',
  standalone: true,
})
export class SeasonListFullComponent {
  @Input({required: true}) seasons: SeasonRankingData[];
}
