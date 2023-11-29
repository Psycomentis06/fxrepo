import {Component} from '@angular/core';
import {Location} from '@angular/common'
import {FormGroup} from "@angular/forms";
import {ImagePostModel} from "../../../models/image-post";
import {ImagePostService} from "../../../services/image-post.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.scss']
})
export class PostComponent {

  imageId?: string

  constructor(private location: Location, private imagePostService: ImagePostService, private router: Router) {
  }

  goBack() {
    this.location.back()
  }

  imageUploadEvent(e: string) {
    this.imageId = e
  }

  createPostFormSubmitEvent(e: FormGroup) {
    if (!this.imageId) {
      alert("Image Not Set, Upload image first");
      return;
    }
    const {title, description, category, tags, nsfw} = e.value
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
      .subscribe({
        next: r => {
          if (r.code === 201) {
            alert("Image Post Created Successfully")
            this.router.navigate(['/images', r.data.slug])
          } else {
            alert('Image Post Not Created: ' + r.message)
          }
        },
        error: err => {
          alert("Failed To Create Image Post: " + err)
        }
      })
  }
}
