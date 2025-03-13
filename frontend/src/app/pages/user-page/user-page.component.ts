import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-user-page',
  templateUrl: './user-page.component.html',
  styleUrl: './user-page.component.css',
  standalone: false
})
export class UserPageComponent implements OnInit {
  userId: number;

  constructor(private route: ActivatedRoute) {}

  ngOnInit() {

    this.userId = this.route.snapshot.params['id'];

  }

}
