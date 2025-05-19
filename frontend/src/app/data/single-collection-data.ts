import {CollectionShowData} from './collection-show-data';

export class SingleCollectionData {
  name: string;
  shows: CollectionShowData[];
  isPrivate: boolean;

  constructor(jsonObject: { [key: string]: any }) {
    this.name = jsonObject['collectionName'];
    this.shows = jsonObject['shows'].map((show: {} ) => {
      return new CollectionShowData(show);
    });
    this.isPrivate = jsonObject['private'];
  }
}
