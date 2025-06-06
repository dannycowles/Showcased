import {CharacterRankingData} from './lists/character-ranking-data';

export class CharacterRankingsData {
  protagonists: CharacterRankingData[];
  deuteragonists: CharacterRankingData[];
  antagonists: CharacterRankingData[];
  tritagonists: CharacterRankingData[];
  side: CharacterRankingData[];
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
    });

    this.tritagonists = jsonObject['tritagonists'].map((tritagonist: {} ) => {
      return new CharacterRankingData(tritagonist);
    });

    this.side = jsonObject['side'].map((side: {} ) => {
      return new CharacterRankingData(side);
    });
  }
}
