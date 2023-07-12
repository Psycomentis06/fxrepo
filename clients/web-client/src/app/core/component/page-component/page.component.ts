import {Component, OnInit} from '@angular/core';
import {HeaderIndicator, HeaderLink, HeaderService} from "../../service/header.service";

@Component({
  selector: 'app-page-component',
  templateUrl: './page.component.html',
  styleUrls: ['./page.component.scss']
})
export class PageComponent implements OnInit {
  name: string;
  path: string;
  position: number;
  indicator: HeaderIndicator | null;
  type: 'link' | 'option'
  ngOnInit(): void {
    this.init()
    const link: HeaderLink = {
      name: this.name,
      path: this.path,
      position: this.position,
      indicator: this.indicator !== null? this.indicator : undefined,
      disabled: true
    }
    if (this.type === 'link') this.headerService.addLink(link)
    else this.headerService.addOtherLink(link)

  }

  constructor(private headerService: HeaderService) {
    this.name = 'Component'
    this.path = ''
    this.position = 0;
    this.indicator = null;
    this.type = 'option'
  }

  init() {}

}
