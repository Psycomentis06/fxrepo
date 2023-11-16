import {AfterViewInit, Component, ElementRef, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {ImagePostService} from "../../../services/image-post.service";
import {ImagePostListModel} from "../../../models/image-post";
import {Page} from "../../../models/page";
import {map} from "rxjs";

@Component({
    selector: 'app-images',
    templateUrl: './images.component.html',
    styleUrls: ['./images.component.scss']
})
export class ImagesComponent implements AfterViewInit {
    @ViewChild('imageDetailsDialog') imageDetailsDialog?: ElementRef<HTMLDialogElement>;
    images: ImagePostListModel[] = []
    imagePostsPage?: Page<ImagePostListModel[]>
    selectedImageSlug?: string

    constructor(private router: Router,
                private activatedRoute: ActivatedRoute,
                private imagePostService: ImagePostService) {
    }

    ngAfterViewInit() {
        // this.getImagePostsPage()

        this.imagePostService.imagePostPageObservable.subscribe(data => {
            this.imagePostsPage = data
            this.images = data.content
        })

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
        const queryParamMap = this.activatedRoute.snapshot.queryParamMap
        const page = Number(queryParamMap.get("page")) || undefined
        const search = queryParamMap.get("search") || ""
        const category = Number(queryParamMap.get("category")) || undefined
        const tag = queryParamMap.get("tag") || undefined
        const nsfw = Boolean(queryParamMap.get("nsfw"))
        this.imagePostService.getImagePostPage(page, 10, search, false, category, tag, nsfw)
            .subscribe(d => {
                this.imagePostsPage = d
                this.images = d.content
                console.log(d.content)
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
        this.selectedImageSlug = undefined
        window.history.replaceState({event: 'close'}, '', '/images')
    }
}
