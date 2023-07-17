export interface ResponseModel<T> {
  message: string
  code: number
  status: string
  data: T
}
