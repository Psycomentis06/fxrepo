import {AfterViewInit, Component, ElementRef, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {ImagePostService} from "../../../services/image-post.service";
import {ImagePostListModel} from "../../../models/image-post";
import {Page} from "../../../models/page";

@Component({
  selector: 'app-images',
  templateUrl: './images.component.html',
  styleUrls: ['./images.component.scss']
})
export class ImagesComponent implements AfterViewInit {
  @ViewChild('imageDetailsDialog') imageDetailsDialog?: ElementRef<HTMLDialogElement>;
  images: ImagePostListModel[] = []
  imagePostsPage?: Page<ImagePostListModel[]>
  selectedImageSlug = ''

  constructor(private router: Router,
              private activatedRoute: ActivatedRoute,
              private imagePostService: ImagePostService) {
  }

  ngAfterViewInit() {
    this.getImagePostsPage()

    window.addEventListener('popstate', (e) => {
      console.log(e.state)
      if (e.state && e.state.event === 'open') {
        if (this.imageDetailsDialog?.nativeElement.open) {
          this.imageDetailsDialog?.nativeElement.close()
          window.history.pushState({event: 'close'}, '', '/images/')
        }
      } else if (e.state.event === 'close') {
        if (!this.imageDetailsDialog?.nativeElement.open) {
          this.imageDetailsDialog?.nativeElement.showModal()
          window.history.pushState({event: 'open'}, '', '/images/' + this.selectedImageSlug)
        }
      }
    })
  }

  getImagePostsPage() {
    const page = Number(this.activatedRoute.snapshot.queryParamMap.get("page")) || 0
    const search = this.activatedRoute.snapshot.queryParamMap.get("search") || ""
    this.imagePostService.getImagePostPage(page, 2, search, false)
      .subscribe(d => {
        this.imagePostsPage = d
        this.images = d.content
      })
  }

  showImageDetailsDialog(slug: string) {
    if (!this.imageDetailsDialog) return
    this.selectedImageSlug = slug
    this.imageDetailsDialog.nativeElement.showModal()
    window.history.pushState({event: 'open'}, '', '/images/' + slug)
  }

  closeModal() {
    if (!this.imageDetailsDialog) return
    this.imageDetailsDialog.nativeElement.close()
    window.history.replaceState({event: 'close'}, '', '/images')
  }
}
