export interface PageData<T> {
  readonly content: T[];
  page: PageInfo;
}

interface PageInfo {
  readonly size: number;
  readonly totalElements: number;
  readonly totalPages: number;
  readonly number: number;
}
