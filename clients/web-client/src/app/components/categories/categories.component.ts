import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {CategoryService} from "../../services/category.service";
import {Category} from "../../models/category";
import {ActivatedRoute, Router} from "@angular/router";
import {ImagePostService} from "../../services/image-post.service";

@Component({
    selector: 'app-categories',
    templateUrl: './categories.component.html',
    styleUrls: ['./categories.component.scss']
})
export class CategoriesComponent implements OnInit {
    @Output() categorySelected: EventEmitter<string>
    categories: Category[] = []
    selectedCategory?: Category

    constructor(private imagePostService: ImagePostService, private categoryService: CategoryService, private router: Router, private activatedRoute: ActivatedRoute) {
        this.categorySelected = new EventEmitter<string>()
    }

    ngOnInit() {
        this.getCategories()
    }

    getCategories(name: string = "") {
        this.categoryService.getCategories("image", name, 0, 20)
            .subscribe(cat => {
                this.categories = cat.data.content
            })
    }

    selectCategory(category: Category) {
        // this.router.navigate([], {queryParams: {category}})
        //   .then(() => this.categorySelected.emit(category))
        this.imagePostService.updateImagePostPage = {category: category.id}
        this.selectedCategory = category
    }

    clearCategoryFilter() {
        this.selectedCategory = undefined
        this.imagePostService.updateImagePostPage = {category: -1}
    }
}
