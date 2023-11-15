import {Component} from '@angular/core';
import {ImagePostService} from "../../../../services/image-post.service";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {

  constructor(private imagePostService: ImagePostService) {
  }

  searchImages(search: string) {
    this.imagePostService.updateImagePostPage = {search}
  }

  clearSearch() {
    this.imagePostService.updateImagePostPage = {search: " "}
  }
}
