export interface CollectionData {
  readonly id: number;
  readonly name: string;
  readonly isPrivate: boolean;
  readonly showPosters: string[];
  readonly showCount: number;
  readonly likeCount: number;
}
