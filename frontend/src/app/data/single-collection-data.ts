import {CollectionShowData} from './collection-show-data';

export class SingleCollectionData {
  name: string;
  shows: CollectionShowData[];
  isPrivate: boolean;
  isRanked: boolean;
  description: string | null;
  numLikes: number;
  likedByUser: boolean;

  constructor(jsonObject: { [key: string]: any }) {
    this.name = jsonObject['collectionName'];
    this.shows = jsonObject['shows'].map((show: {} ) => {
      return new CollectionShowData(show);
    });
    this.isPrivate = jsonObject['private'];
    this.isRanked = jsonObject['ranked'];
    this.description = jsonObject['description'];
    this.numLikes = jsonObject['numLikes'];
    this.likedByUser = jsonObject['likedByUser'];
  }
}
