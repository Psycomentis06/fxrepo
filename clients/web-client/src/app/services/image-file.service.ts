import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment'
import {HttpClient} from "@angular/common/http";
import {ResponseModel} from "../core/models/response";
import {ImageFileModel} from "../models/image-file";

@Injectable({
  providedIn: 'root'
})
export class ImageFileService {

  readonly UPLOAD_IMAGE_FILE_ENDPOINT = environment.api.fx_main.v1 + '/file/image/new'

  constructor(private http: HttpClient) { }

  uploadImage(file: File) {
    const fd = new FormData()
    fd.set('file', file)
    return this.http.post<any>(this.UPLOAD_IMAGE_FILE_ENDPOINT, fd, {
      reportProgress: true,
      observe: 'events'
    })
  }
}
