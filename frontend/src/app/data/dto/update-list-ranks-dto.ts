interface BaseUpdateListRankingDto {
  id: number;
  rankNum: number;
}

export interface UpdateShowRankingDto extends BaseUpdateListRankingDto {}

export interface UpdateEpisodeRankingDto extends BaseUpdateListRankingDto {}

export interface UpdateSeasonRankingDto extends BaseUpdateListRankingDto {}

export interface UpdateDynamicRankingDto extends BaseUpdateListRankingDto {}

interface UpdateCharacterRankingItem {
  id: string;
  rankNum: number;
}

export interface UpdateCharacterRankingDto {
  characterType: string;
  updates: UpdateCharacterRankingItem[]
}
