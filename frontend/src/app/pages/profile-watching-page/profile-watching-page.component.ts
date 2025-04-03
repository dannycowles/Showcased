import {Component, OnInit} from '@angular/core';
import {ProfileService} from '../../services/profile.service';

@Component({
  selector: 'app-profile-watching-page',
  templateUrl: './profile-watching-page.component.html',
  styleUrl: './profile-watching-page.component.css',
  standalone: false
})
export class ProfileWatchingPageComponent implements OnInit {

  constructor(private profileService: ProfileService) { }

  ngOnInit() {}

}
