export const sortReviewOptions = [
  { value: 'reviewDate,desc', label: 'Most Recent' },
  { value: 'numLikes,desc', label: 'Most Likes' },
  { value: 'rating,desc', label: 'Positive Reviews' },
  { value: 'rating,asc', label: 'Negative Reviews' },
] as const;

export const reviewTypeOptions = [
  { value: 'all', label: 'All' },
  { value: 'show', label: 'Show' },
  { value: 'episode', label: 'Episode'}
] as const;

export type ReviewTypeOption = typeof reviewTypeOptions[number];
export type SortReviewOption = typeof sortReviewOptions[number];
