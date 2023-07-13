import {Component, OnInit} from '@angular/core';
import {Header, HeaderService} from "../core/service/header.service";

@Component({
  selector: 'app-home-page',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  constructor(private headerService: HeaderService) {}

  ngOnInit() {
    const h: Header = {
      logo: 'assets/logo.png',
      links: {
        links: [
          {
            name: 'Images',
            path: 'images',
            position: 0,
            indicator: {
              color: 'primary',
              content: 'new'
            },
            disabled: false
          },
          {
            name: 'Vectors',
            path: 'vectors',
            position: 1,
            indicator: {
              color: 'secondary',
              content: 'soon'
            },
            disabled: true
          },
          {
            name: 'Videos',
            path: 'videos',
            position: 2,
            indicator: {
              color: 'secondary',
              content: 'soon'
            },
            disabled: true
          },
          {
            name: 'Audio',
            path: 'audio',
            position: 3,
            indicator: {
              color: 'secondary',
              content: 'soon'
            },
            disabled: true
          }
        ],
        more: [
          {
            name: '3D Models',
            path: 'models',
            position: 0,
            indicator: {
              color: 'secondary',
              content: 'soon'
            },
            disabled: true
          },
          {
            name: 'Software Specific',
            path: 'software',
            position: 1,
            indicator: {
              color: 'secondary',
              content: 'soon'
            },
            disabled: true
          },
          {
            name: 'Templates',
            path: 'templates',
            position: 2,
            indicator: {
              color: 'secondary',
              content: 'soon'
            },
            disabled: true
          },
          {
            name: 'Plugins',
            path: 'plugins',
            position: 3,
            indicator: {
              color: 'secondary',
              content: 'soon'
            },
            disabled: true
          }
        ]
      }
    }
    this.headerService.setHeader(h)
  }
}
