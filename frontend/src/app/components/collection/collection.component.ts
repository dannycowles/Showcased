import {Component, Input} from '@angular/core';
import {CollectionData} from '../../data/collection-data';
import {NgOptimizedImage} from '@angular/common';
import {RouterLink} from '@angular/router';
import {ButtonHeartComponent} from '../button-heart.component';

@Component({
  selector: 'app-collection',
  imports: [NgOptimizedImage, RouterLink, ButtonHeartComponent],
  templateUrl: './collection.component.html',
  styleUrl: './collection.component.css',
  standalone: true,
})
export class CollectionComponent {
  @Input({ required: true }) collection: CollectionData;
  readonly numPosters: number = 5;
}
