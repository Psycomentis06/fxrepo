import {Component, OnInit} from '@angular/core';
import {HeaderLink, HeaderService} from "../service/header.service";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  links: HeaderLink[]
  otherLinks: HeaderLink[]
  constructor(public headerService: HeaderService) {
    this.links = []
    this.otherLinks = []
  }

  ngOnInit() {
    this.headerService.header.subscribe(o => {
      this.links = o.links.links
      this.otherLinks = o.links.other ? o.links.other : []
    })
  }
}
