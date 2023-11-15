import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Page} from "../../../../models/page";
import {Router} from "@angular/router";
import {ImagePostService} from "../../../../services/image-post.service";

@Component({
    selector: 'app-pagination',
    templateUrl: './pagination.component.html',
    styleUrls: ['./pagination.component.scss']
})
export class PaginationComponent {
    @Input() page?: Page<any>
    @Output() pageChange = new EventEmitter<number>();

    pageEditMode = false
    pageJumpInput = "0"

    constructor(private router: Router, private imagePostService: ImagePostService) {
    }

    handlePageJumpInputChange(e: KeyboardEvent) {
        console.log(e)
    }

    cancelEdit() {
        this.pageEditMode = false
        this.pageJumpInput = "" + this.page?.number || "0"
    }

    jumpToPage() {
        this.pageEditMode = false
        this.goToPage(Number(this.pageJumpInput) || 0)
    }

    goToPage(page: number) {
        if (this.page && page >= 0 && page <= this.page?.totalPages - 1) {
            // this.router.navigate([], {queryParams: {page: page}})
            //   .then(() => {
            //     this.pageChange.emit(page)
            //     this.pageJumpInput = page + ""
            //   })
            this.imagePostService.updateImagePostPage = {page: page}
            this.pageJumpInput = page + ""
        }
    }
}
