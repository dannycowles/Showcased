import {Component, Input} from '@angular/core';
import {CharacterRankingData} from '../../data/lists/character-ranking-data';

@Component({
  selector: 'app-character-list-full',
  imports: [],
  templateUrl: './character-list-full.component.html',
  styleUrl: './character-list-full.component.css',
  standalone: true
})
export class CharacterListFullComponent {
  @Input({required: true}) characters: CharacterRankingData[];
}
