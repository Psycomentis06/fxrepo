import { Component } from '@angular/core';

@Component({
  selector: 'app-upload-image-file',
  templateUrl: './upload-image-file.component.html',
  styleUrls: ['./upload-image-file.component.scss']
})
export class UploadImageFileComponent {

  uploadImage(el: HTMLInputElement) {
    const {files} = el
    if (files && files.length > 0) {
      console.log(files[0])
    }
  }
}
