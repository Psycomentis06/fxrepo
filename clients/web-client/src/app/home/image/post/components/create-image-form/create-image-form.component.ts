import { Component } from '@angular/core';

@Component({
  selector: 'app-create-image-form',
  templateUrl: './create-image-form.component.html',
  styleUrls: ['./create-image-form.component.scss']
})
export class CreateImageFormComponent {
  submit(e: Event) {
    e.preventDefault()
  }
}
