export class CollectionShowData {
  id: number;
  title: string;
  posterPath: string;
  rankNum: number | null;

  constructor(jsonObject: {[key: string]: any}) {
    this.id = jsonObject["showId"];
    this.title = jsonObject["title"];
    this.posterPath = jsonObject["posterPath"];
    this.rankNum = jsonObject["rankNum"];
  }
}
