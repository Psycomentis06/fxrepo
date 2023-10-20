import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ImagePostListModel, ImagePostModel} from "../models/image-post";
import {environment} from '../../environments/environment'
import {Page} from "../models/page";
import {tap} from "rxjs";
import {Router} from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class ImagePostService {

  readonly CREATE_NEW_IMAGE_POST_ENDPOINT = environment.api.fx_main.v1 + '/post/image/new';
  readonly GET_IMAGE_POST_PAGE_ENDPOINT = environment.api.fx_main.v1 + '/post/image/list';

  constructor(private http: HttpClient, private router: Router) {
  }

  createImagePost(d: ImagePostModel) {
    return this.http.post<ImagePostModel>(this.CREATE_NEW_IMAGE_POST_ENDPOINT, d)
  }

  getImagePostPage(page: number, size: number, search: string, ts: boolean) {
    if (!page) page = 0
    if (!size) size = 10
    if (!search) search = ""
    const params = {
      page,
      limit: size,
      search,
      // Enable typsense for results
      ts
    }
    return this.http.get<Page<ImagePostListModel[]>>(this.GET_IMAGE_POST_PAGE_ENDPOINT, {
      params
    }).pipe(
      tap(() => {
        let queryParams: { page?: number, search?: string } = {
          page,
          search
        }
        if (page === 0) delete queryParams.page;
        if (search.trim().length === 0) delete queryParams.search;
        this.router.navigate([], {queryParams})
      })
    )
  }
}
