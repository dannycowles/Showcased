export const sortReviewOptions = [
  { value: 'reviewDate,desc', label: 'Most Recent' },
  { value: 'numLikes,desc', label: 'Most Likes' },
  { value: 'rating,desc', label: 'Positive Reviews' },
  { value: 'rating,asc', label: 'Negative Reviews' },
] as const;

export type SortReviewOption = typeof sortReviewOptions[number];
