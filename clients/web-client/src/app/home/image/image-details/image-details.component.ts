import {Component, Input, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-image-details',
  templateUrl: './image-details.component.html',
  styleUrls: ['./image-details.component.scss']
})
export class ImageDetailsComponent implements OnInit {
  @Input('slug') inputSlug?: string

  slug?: string

  constructor(private route: ActivatedRoute) {
  }

  ngOnInit() {
    if (this.inputSlug) {
      this.slug = this.inputSlug
    } else {
      this.slug = this.route.snapshot.params['slug']
    }
  }
}
