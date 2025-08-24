import {Component, Input} from '@angular/core';
import {CollectionData} from '../../data/collection-data';
import {NgOptimizedImage} from '@angular/common';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-collection',
  imports: [NgOptimizedImage, RouterLink],
  templateUrl: './collection.component.html',
  styleUrl: './collection.component.css',
  standalone: true,
})
export class CollectionComponent {
  @Input({ required: true }) collection: CollectionData;
  readonly numPosters: number = 5;
}
