import {Component, Input} from '@angular/core';
import {CharacterRankingData} from '../../data/lists/character-ranking-data';

@Component({
  selector: 'app-character-list',
  imports: [],
  templateUrl: './character-list.component.html',
  styleUrl: './character-list.component.css',
  standalone: true
})
export class CharacterListComponent {
  @Input({required : true}) title: string;
  @Input({required : true}) characters: CharacterRankingData[];
}
