export class CollectionData {
  readonly id: number;
  name: string;
  isPrivate: boolean;

  constructor(jsonObject: { [key: string]: any }) {
    this.id = jsonObject['collectionId'];
    this.name = jsonObject['collectionName'];
    this.isPrivate = jsonObject['private'];
  }
}
