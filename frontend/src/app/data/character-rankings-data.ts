import {CharacterRankingData} from './lists/character-ranking-data';

export interface CharacterRankingsData {
  protagonists: CharacterRankingData[];
  deuteragonists: CharacterRankingData[];
  antagonists: CharacterRankingData[];
  tritagonists: CharacterRankingData[];
  side: CharacterRankingData[];
  [key: string]: CharacterRankingData[];
}
