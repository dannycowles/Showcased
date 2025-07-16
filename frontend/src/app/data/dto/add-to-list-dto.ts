interface BaseAddToListDto {
  showId: number,
  showTitle: string;
  posterPath: string;
}

export interface AddToWatchlistDto extends BaseAddToListDto {}

export interface AddToWatchingListDto extends BaseAddToListDto {}

export interface AddToShowRankingList extends BaseAddToListDto {}

export interface AddToSeasonRankingList extends BaseAddToListDto {
  season: number;
}

export interface AddToEpisodeRankingList extends AddToSeasonRankingList {
  episodeId: number;
  episodeTitle: string;
  episode: number;
}

export interface AddToCharacterRankingList extends BaseAddToListDto {
  characterId: string;
  characterName: string;
  characterType: string;
}

export interface AddToCollection extends BaseAddToListDto {}

export interface AddToDynamicRankingList extends BaseAddToListDto {
  character1Id: string;
  character1Name: string;
  character2Id: string;
  character2Name: string;
}
