import {Component, Input} from '@angular/core';
import {EpisodeRankingData} from '../../data/lists/episode-ranking-data';
import {RouterLink} from '@angular/router';
import {NgOptimizedImage} from '@angular/common';

@Component({
  selector: 'app-episode-list',
  imports: [
    RouterLink,
    NgOptimizedImage
  ],
  templateUrl: './episode-list.component.html',
  styleUrl: './episode-list.component.css',
  standalone: true
})
export class EpisodeListComponent {
  @Input({required: true}) episodes : EpisodeRankingData[];
}
