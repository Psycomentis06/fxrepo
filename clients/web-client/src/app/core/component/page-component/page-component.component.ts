import {Component, OnInit} from '@angular/core';
import {HeaderIndicator, HeaderLink, HeaderService} from "../../service/header.service";

@Component({
  selector: 'app-page-component',
  templateUrl: './page-component.component.html',
  styleUrls: ['./page-component.component.scss']
})
export class PageComponentComponent implements OnInit {
  name: string;
  path: string;
  position: number;
  indicator: HeaderIndicator | null;
  ngOnInit(): void {
    this.init()
    const link: HeaderLink = {
      name: this.name,
      path: this.path,
      position: this.position,
      indicator: this.indicator !== null? this.indicator : undefined
    }
    this.headerService.addLink(link)
  }

  constructor(private headerService: HeaderService) {
    this.name = 'Component'
    this.path = ''
    this.position = 0;
    this.indicator = null;
  }

  init() {}

}
