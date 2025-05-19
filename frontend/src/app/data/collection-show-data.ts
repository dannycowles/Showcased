export class CollectionShowData {
  showId: number;
  title: string;
  posterPath: string;

  constructor(jsonObject: {[key: string]: any}) {
    this.showId = jsonObject["showId"];
    this.title = jsonObject["title"];
    this.posterPath = jsonObject["posterPath"];
  }
}
