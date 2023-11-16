import {Component, Input, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {ImagePostService} from "../../../services/image-post.service";

@Component({
    selector: 'app-image-details',
    templateUrl: './image-details.component.html',
    styleUrls: ['./image-details.component.scss']
})
export class ImageDetailsComponent implements OnInit {
    @Input('slug') inputSlug?: string

    slug?: string

    constructor(private route: ActivatedRoute, private imagePostService: ImagePostService) {
    }

    ngOnInit() {
        if (this.inputSlug) {
            this.slug = this.inputSlug
        } else {
            this.slug = this.route.snapshot.params['slug']
        }
        if (this.slug) this.imagePostService.getImagePostBySlug(this.slug)
            .subscribe(d => {
                console.log(d)
            })
    }
}
