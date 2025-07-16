export interface UpdateCollectionDetails {
  collectionName?: string;
  description?: string;
  isPrivate?: boolean;
  isRanked?: boolean;
  shows?: UpdateCollectionShow[]
}

interface UpdateCollectionShow {
  id: number;
}
