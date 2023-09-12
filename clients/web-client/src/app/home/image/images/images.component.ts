import {AfterViewInit, Component, ElementRef, ViewChild} from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-images',
  templateUrl: './images.component.html',
  styleUrls: ['./images.component.scss']
})
export class ImagesComponent implements AfterViewInit {
  @ViewChild('imageDetailsDialog') imageDetailsDialog?: ElementRef<HTMLDialogElement>;
  images = [
    {},
    {},
    {},
    {},
    {},
    {},
    {},
  ]
  selectedImageSlug = ''

  constructor(private router: Router) {
  }

  ngAfterViewInit() {
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
