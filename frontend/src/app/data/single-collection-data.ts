import {CollectionShowData} from './collection-show-data';

export class SingleCollectionData {
  collectionName: string;
  shows: CollectionShowData[];
  isPrivate: boolean;

  constructor(jsonObject: { [key: string]: any }) {
    this.collectionName = jsonObject['collectionName'];
    this.shows = jsonObject['shows'].map((show: {} ) => {
      return new CollectionShowData(show);
    });
    this.isPrivate = jsonObject['private'];
  }
}
