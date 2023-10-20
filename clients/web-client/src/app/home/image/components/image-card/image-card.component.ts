import {Component, Input} from '@angular/core';
import {ImagePostListModel} from "../../../../models/image-post";

@Component({
  selector: 'app-image-card',
  templateUrl: './image-card.component.html',
  styleUrls: ['./image-card.component.scss']
})
export class ImageCardComponent {
  @Input() imagePost?: ImagePostListModel

}
