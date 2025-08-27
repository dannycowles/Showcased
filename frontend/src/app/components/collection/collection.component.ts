import {booleanAttribute, Component, Input} from '@angular/core';
import {CollectionData} from '../../data/collection-data';
import {NgOptimizedImage} from '@angular/common';
import {ActivatedRoute, Router} from '@angular/router';
import {ButtonHeartComponent} from '../button-heart.component';

@Component({
  selector: 'app-collection',
  imports: [NgOptimizedImage, ButtonHeartComponent],
  templateUrl: './collection.component.html',
  styleUrl: './collection.component.css',
  standalone: true,
})
export class CollectionComponent {
  @Input({ required: true }) collection: CollectionData;
  @Input({ transform: booleanAttribute }) onProfilePage: boolean = false;
  readonly numPosters: number = 5;

  constructor(private router: Router,
              private route: ActivatedRoute) {}

  navigateToCollection() {
    if (this.onProfilePage) {
      this.router.navigate(['collections', this.collection.id], {relativeTo: this.route});
    } else {
      this.router.navigate([this.collection.id], {relativeTo: this.route});
    }
  }
}
