import {Component, OnInit} from '@angular/core';
import {HeaderLink, HeaderService} from "../service/header.service";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  links: HeaderLink[]
  constructor(public headerService: HeaderService) {
    this.links = []
  }

  ngOnInit() {
    this.headerService.header.subscribe(o => {
      this.links = o.links.links
      console.log(this.links)
    })
  }
}
