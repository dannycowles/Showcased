export interface ReviewCommentData {
  readonly id: number,
  readonly reviewId: number,
  readonly userId: number,
  readonly username: string,
  readonly profilePicture: string,
  readonly commentText: string,
  numLikes: number,
  readonly createdAt: string,
  isLikedByUser: boolean;
}
