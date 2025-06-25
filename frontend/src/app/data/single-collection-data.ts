import {CollectionShowData} from './collection-show-data';

export interface SingleCollectionData {
  name: string;
  shows: CollectionShowData[];
  isPrivate: boolean;
  isRanked: boolean;
  description: string | null;
  numLikes: number;
  isLikedByUser: boolean;
}
