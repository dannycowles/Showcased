export class ShowListData {
  readonly showId: number;
  readonly title: string;
  readonly posterPath: string;

  constructor(jsonObject: { [key : string]: any }) {
    this.showId = jsonObject['showId'];
    this.title = jsonObject['title'];
    this.posterPath = jsonObject['posterPath'];
  }
}
