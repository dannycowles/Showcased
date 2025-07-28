export interface PageData<T> {
  content: T[];
  page: PageInfo;
}

interface PageInfo {
  readonly size: number;
  readonly totalElements: number;
  readonly totalPages: number;
  readonly number: number;
}
