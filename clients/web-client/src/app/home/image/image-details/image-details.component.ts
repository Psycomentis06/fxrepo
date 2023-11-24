import {Component, Input, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {ImagePostService} from "../../../services/image-post.service";
import {ImagePostDetailsModel} from "../../../models/image-post";
import {FileVariantModel} from "../../../models/file-variant";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-image-details',
  templateUrl: './image-details.component.html',
  styleUrls: ['./image-details.component.scss']
})
export class ImageDetailsComponent implements OnInit {
  @Input('slug') inputSlug?: string

  slug?: string

  imagePost?: ImagePostDetailsModel
  variant?: FileVariantModel

  constructor(private route: ActivatedRoute, private imagePostService: ImagePostService, private httpClient: HttpClient) {
  }

  ngOnInit() {
    if (this.inputSlug) {
      this.slug = this.inputSlug
    } else {
      this.slug = this.route.snapshot.params['slug']
    }
    if (this.slug) this.imagePostService.getImagePostBySlug(this.slug)
      .subscribe(d => {
        this.imagePost = d.data
        this.variant = this.imagePost.image.variants.reduce((minVar, currVar) => {
          return (currVar.width < minVar.width && currVar.height < minVar.height) ? currVar : minVar
        }, this.imagePost.image.variants[0])
      })
  }


  downloadImage(url: string, name: string) {
    this.httpClient.get(url, {responseType: 'blob'})
      .subscribe((response: Blob) => {
        const url = URL.createObjectURL(response)
        const link = document.createElement('a')
        link.href = url
        link.download = name
        link.click()
      }, err => {
        console.log(err)
      })
  }
}
