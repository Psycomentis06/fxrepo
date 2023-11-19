import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";
import {CategoryService} from "../../../../../services/category.service";
import {Category} from "../../../../../models/category";
import {map, Observable, tap} from "rxjs";

@Component({
  selector: 'app-create-image-form',
  templateUrl: './create-image-form.component.html',
  styleUrls: ['./create-image-form.component.scss']
})
export class CreateImageFormComponent implements OnInit {
  @Input('disabled')
  disabled = true

  @Output('form-submit')
  submitEvent = new EventEmitter<FormGroup>()

  dataForm: FormGroup
  categories: Observable<string[]>
  category: string = ""

  constructor(private categoryService: CategoryService) {
    this.dataForm = new FormGroup({})
    this.categories = new Observable<string[]>()
  }

  ngOnInit() {
    this.categories = this.categoryService.getCategories("image", "", 0, 5)
      .pipe(
        map((res) => res.data.content.map(c => c.name.length === 0 ? "default" : c.name)
        )
      )

    this.dataForm = new FormGroup<any>({
      title: new FormControl(''),
      description: new FormControl(''),
      category: new FormControl(this.category),
      visibility: new FormControl('true'),
      tags: new FormControl(''),
      nsfw: new FormControl(false),
    })
  }

  submit(e: Event) {
    e.preventDefault()
    if (this.disabled) {
      alert('Upload is disabled, maybe you need to upload an image 1st')
      return
    }

    this.submitEvent.emit(this.dataForm)
  }
}
