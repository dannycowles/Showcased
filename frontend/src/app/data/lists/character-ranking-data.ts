export class CharacterRankingData {
  name: string
  show: string
  rankNum: number

  constructor(jsonObject: {[key: string]: any}) {
    this.name = jsonObject['characterName']
    this.show = jsonObject['showName']
    this.rankNum = jsonObject['rankNum']
  }
}
