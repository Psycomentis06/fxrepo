import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";

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

  constructor() {
    this.dataForm = new FormGroup({})
  }

  ngOnInit() {
    this.dataForm = new FormGroup<any>({
      title: new FormControl(''),
      description: new FormControl(''),
      category: new FormControl(0),
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
