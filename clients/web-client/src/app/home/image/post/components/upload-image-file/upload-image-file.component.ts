import {Component, EventEmitter, Output} from '@angular/core';
import {fadeInOnEnterAnimation, fadeOutAnimation} from "angular-animations";

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

  removePreviewImage() {
    this.file = undefined
    this.imageLocalUrl = undefined
  }

  uploadImage() {
    if (!this.file)  {
      alert("Image empty")
      return
    }
    this.progressInc()
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
