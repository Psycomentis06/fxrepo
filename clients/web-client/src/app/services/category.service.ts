import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from '../../environments/environment'
import {Page} from "../models/page";
import {Category} from "../models/category";
import {ResponseModel} from "../core/models/response"

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  readonly GET_CATEGORIES_ENDPOINT = environment.api.fx_main.v1 + '/category/{postType}/list'

  constructor(private http: HttpClient) {
  }

  getCategories(postType: string, name: string, page: number, size: number) {
    const queryParams = {
      q: name,
      p: page,
      l: size
    }
    return this.http.get<ResponseModel<Page<Category[]>>>(this.GET_CATEGORIES_ENDPOINT.replace('{postType}', postType), {
      params: queryParams
    })
  }
}
