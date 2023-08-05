import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ImagePostModel} from "../models/image-post";
import { environment } from '../../environments/environment'

@Injectable({
  providedIn: 'root'
})
export class ImagePostService {

  readonly CREATE_NEW_IMAGE_POST_ENDPOINT = environment.api.fx_main.v1 + '/post/image/new';
  constructor(private http: HttpClient) { }

  createImagePost(d: ImagePostModel) {
    return this.http.post<ImagePostModel>(this.CREATE_NEW_IMAGE_POST_ENDPOINT, d)
  }
}
