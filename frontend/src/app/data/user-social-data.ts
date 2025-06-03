export class UserSocialData {
  readonly socialId: number;
  readonly socialName: string;
  readonly handle: string;
  readonly url: string;
  readonly icon: string;

  constructor(jsonObject: {[key: string]: any}) {
    this.socialId = jsonObject['id'];
    this.socialName = jsonObject['socialName'];
    this.handle = jsonObject['handle'];
    this.url = jsonObject['url'];
    this.icon = jsonObject['icon'];
  }
}
