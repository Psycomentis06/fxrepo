import {Component, OnInit} from '@angular/core';
import {PageComponentComponent} from "../core/component/page-component/page-component.component";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent extends PageComponentComponent {
  override init() {
    super.name = 'Home'
    super.path = ''
    super.position = 0
  }
}
