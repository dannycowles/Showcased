import {Component, OnInit} from '@angular/core';
import {ProfileService} from '../../../services/profile.service';

@Component({
  selector: 'app-profile-collections-page',
  templateUrl: './profile-collections-page.component.html',
  styleUrl: './profile-collections-page.component.css',
  standalone: false
})
export class ProfileCollectionsPageComponent implements OnInit {

  constructor(private profileService: ProfileService) { };

  ngOnInit() {};

}
