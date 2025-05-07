import {CharacterRankingData} from './lists/character-ranking-data';

export class CharacterRankingsData {
  protagonists: CharacterRankingData[];
  deuteragonists: CharacterRankingData[];
  antagonists: CharacterRankingData[];
  [key: string]: CharacterRankingData[];

  constructor(jsonObject: {[key: string]: any}) {
    this.protagonists = jsonObject['protagonists'].map((protagonist: {} ) => {
      return new CharacterRankingData(protagonist);
    });

    this.deuteragonists = jsonObject['deuteragonists'].map((deuteragonist: {} ) => {
      return new CharacterRankingData(deuteragonist);
    });

    this.antagonists = jsonObject['antagonists'].map((antagonist: {} ) => {
      return new CharacterRankingData(antagonist);
    })
  }
}
