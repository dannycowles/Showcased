import { Component } from '@angular/core';
import {NgOptimizedImage} from '@angular/common';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrl: './home-page.component.css',
  imports: [NgOptimizedImage, RouterLink],
  standalone: true,
})
export class HomePageComponent {
  constructor() {}
}
