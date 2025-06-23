import {CollectionShowData} from './collection-show-data';

export interface SingleCollectionData {
  name: string;
  shows: CollectionShowData[];
  private: boolean;
  ranked: boolean;
  description: string | null;
  numLikes: number;
  likedByUser: boolean;
}
