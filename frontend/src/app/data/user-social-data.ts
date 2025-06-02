export class UserSocialData {
  readonly socialId: number;
  readonly socialName: string;
  readonly handle: string;
  readonly url: string;

  constructor(jsonObject: {[key: string]: any}) {
    this.socialId = jsonObject['id'];
    this.socialName = jsonObject['socialName'];
    this.handle = jsonObject['handle'];
    this.url = jsonObject['url'];
  }
}
