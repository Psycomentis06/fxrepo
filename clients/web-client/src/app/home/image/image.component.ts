import {AfterViewInit, Component, OnDestroy} from '@angular/core';
import {HeaderService} from "../../core/service/header.service";
import {HeaderComponent} from "./components/header/header.component";

@Component({
  selector: 'app-image',
  templateUrl: './image.component.html',
  styleUrls: ['./image.component.scss']
})
export class ImageComponent implements AfterViewInit, OnDestroy {
  constructor(private headerService: HeaderService) {}

  ngAfterViewInit(): void {
    this.headerService.setPre(HeaderComponent)
  }

  ngOnDestroy() {
    this.headerService.setPre()
  }
}
