import {Component, OnInit} from '@angular/core';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-page-not-found',
  templateUrl: './page-not-found.component.html',
  styleUrl: './page-not-found.component.css',
  imports: [RouterLink],
  standalone: true,
})
export class PageNotFoundComponent implements OnInit {
  ngOnInit() {}
}
