import {Component, EventEmitter, Output} from '@angular/core';
import {fadeInOnEnterAnimation, fadeOutAnimation} from "angular-animations";
import {ImageFileService} from "../../../../../services/image-file.service";
import {ToastService} from "../../../../../core/service/toast.service";
import {HttpEventType} from "@angular/common/http";
import {ResponseModel} from "../../../../../core/models/response";
import {ImageFileModel} from "../../../../../models/image-file";

@Component({
  selector: 'app-upload-image-file',
  templateUrl: './upload-image-file.component.html',
  styleUrls: ['./upload-image-file.component.scss'],
  animations: [
    fadeInOnEnterAnimation(),
    fadeOutAnimation()
  ]
})
export class UploadImageFileComponent {
  @Output('image')
  imageId: EventEmitter<string> = new EventEmitter<string>()
  file?: File
  imageLocalUrl?: string
  progress: number = 0
  progressText: string = 'Uploading...'

  constructor(private imageFileService: ImageFileService, private toastService: ToastService) {
  }

  removePreviewImage() {
    this.file = undefined
    this.imageLocalUrl = undefined
  }

  uploadImage() {
    if (!this.file)  {
      alert("Image empty")
      return
    }
    //this.progressInc()
    this.imageFileService.uploadImage(this.file).subscribe(res => {
      if (res.type === HttpEventType.UploadProgress) {
        this.progressText = 'Uploading...'
        this.progress = Math.round(100 * res.loaded / (res.total || 0))
      }

      if (res.type === HttpEventType.Response) {
        const d = res.body as ResponseModel<ImageFileModel>
        console.log(d.message)
      }
    })
  }

  progressInc() {
    const a = setInterval(() => {
      this.progress += 10
      if (this.progress >= 60) {
        this.progressText = 'Verifying...'
      }
      if (this.progress === 100) {
        this.progressText = 'finish'
        clearInterval(a)
        this.imageId.emit('imageId')
        this.toastService.add({
          id: Math.random(),
          title: 'Image upload success',
        })
      }
    }, 200)
  }

  previewImage() {
    if (this.file) this.imageLocalUrl = URL.createObjectURL(this.file)
  }

  fileInputHandler(el: HTMLInputElement) {
    const {files} = el
    if (files && files.length > 0) {
      console.log(files[0])
      this.file = files[0]
      this.previewImage()
    }
  }
}
