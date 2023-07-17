import {Component, OnInit} from '@angular/core';
import {Location} from '@angular/common'

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.scss']
})
export class PostComponent {

  imageId?: string
  constructor(private location: Location) {
  }

  goBack() {
    this.location.back()
  }

  imageUploadEvent(e: string) {
    this.imageId = e
  }
}
