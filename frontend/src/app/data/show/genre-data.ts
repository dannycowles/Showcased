export class GenreData {
  readonly id: number;
  readonly name: string;

  constructor(jsonObject: { [key: string]: any }) {
    this.id = jsonObject['id'];
    this.name = jsonObject['name'];
  }
}
