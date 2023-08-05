import {Component, OnInit} from '@angular/core';
import {Location} from '@angular/common'
import {FormControl, FormGroup} from "@angular/forms";
import {ImagePostModel} from "../../../models/image-post";
import {ImagePostService} from "../../../services/image-post.service";

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.scss']
})
export class PostComponent {

  imageId?: string
  constructor(private location: Location, private imagePostService: ImagePostService) {
  }

  goBack() {
    this.location.back()
  }

  imageUploadEvent(e: string) {
    this.imageId = e
  }

  createPostFormSubmitEvent(e: FormGroup) {
    const { title, description, category, tags, nsfw } = e.value
    const data: ImagePostModel = {
      title,
      category,
      tags: tags.split(','),
      nsfw,
      content: description,
      public: e.value?.public || true,
      image: this.imageId || ''
    }
    this.imagePostService.createImagePost(data)
      .subscribe(r => {
        console.log(r)
      })
  }
}
