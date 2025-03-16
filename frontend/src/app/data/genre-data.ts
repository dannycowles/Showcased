export class GenreData {
  id: number;
  name: string;

  constructor(jsonObject: { [key: string]: any }) {
    this.id = jsonObject['id'];
    this.name = jsonObject['name'];
  }
}
