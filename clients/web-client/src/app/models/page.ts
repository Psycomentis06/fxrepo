export interface Page<T> {
  content: T
  empty: boolean
  first: boolean
  last: boolean
  number: number
  numberOfElements: number
  size: number
  totalElements: number
  totalPages: number
  pageable: {
    pageNumber: number
    pageSize: number
    offset: number
    paged: boolean
    unpaged: boolean
    sort: Sort
  }
  sort: Sort
}

export interface Sort {
  sorted: boolean
  unsorted: boolean
  empty: boolean
  sort: any[]
}
