import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-create-image-form',
  templateUrl: './create-image-form.component.html',
  styleUrls: ['./create-image-form.component.scss']
})
export class CreateImageFormComponent {
  @Input('disabled')
  disabled = true
  submit(e: Event) {
    e.preventDefault()
    if (this.disabled) {
      alert('Upload is disabled, maybe you need to upload an image 1st')
      return
    }
  }
}
