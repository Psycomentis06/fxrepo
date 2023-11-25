import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";
import {CategoryService} from "../../../../../services/category.service";
import {debounceTime, distinctUntilChanged, map, Observable} from "rxjs";

type PostImageForm = {
  title: FormControl,
  description: FormControl,
  category: FormControl,
  visibility: FormControl,
  tags: FormControl,
  nsfw: FormControl
}

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
  filteredCategories: Observable<string[]>

  constructor(private categoryService: CategoryService) {
    this.dataForm = new FormGroup({})
    this.categories = new Observable<string[]>()
    this.filteredCategories = new Observable<string[]>()
  }

  ngOnInit() {
    this._filter("")

    this.dataForm = new FormGroup<PostImageForm>({
      title: new FormControl(''),
      description: new FormControl(''),
      category: new FormControl(),
      visibility: new FormControl('true'),
      tags: new FormControl(''),
      nsfw: new FormControl(false),
    })

    this.dataForm.get("category")?.valueChanges
      .pipe(
        debounceTime(1000),
        distinctUntilChanged(),
      )
      .subscribe(val => {
        this._filter(val)
      })
  }

  private _filter(value: string) {
    const filterValue = value.toLowerCase()
    this.categories = this.categoryService.getCategories("image", filterValue, 0, 5)
      .pipe(
        map((res) => res.data.content.map(c => c.name.length === 0 ? "default" : c.name))
      )
  }

  getCategories(query: string) {
    return this.categoryService.getCategories("image", query, 0, 5)
  }

  inputChangeEvent(event: Event) {
    console.log(event)
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
