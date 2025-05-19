export class CollectionData {
  readonly collectionId: number;
  collectionName: string;
  isPrivate: boolean;

  constructor(jsonObject: { [key: string]: any }) {
    this.collectionId = jsonObject['collectionId'];
    this.collectionName = jsonObject['collectionName'];
    this.isPrivate = jsonObject['private'];
  }
}
