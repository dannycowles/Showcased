export class RoleData {
  readonly id: string;
  readonly characterName: string;

  constructor(jsonObject: {[key: string]: any}) {
    this.id = jsonObject['credit_id'];
    this.characterName = jsonObject['character'];
  }
}
