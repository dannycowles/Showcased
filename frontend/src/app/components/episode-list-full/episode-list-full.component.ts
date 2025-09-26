import {Component, Input} from '@angular/core';
import {NgOptimizedImage} from '@angular/common';
import {EpisodeRankingData} from '../../data/lists/episode-ranking-data';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-episode-list-full',
  imports: [NgOptimizedImage, RouterLink],
  templateUrl: './episode-list-full.component.html',
  styleUrl: './episode-list-full.component.css',
  standalone: true,
})
export class EpisodeListFullComponent {
  @Input({ required: true }) episodes: EpisodeRankingData[];
}
