import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subject, takeUntil} from "rxjs";
import {HeaderLinks, HeaderService} from "../../../../service/header.service";

@Component({
  selector: 'app-desktop',
  templateUrl: './desktop.component.html',
  styleUrls: ['./desktop.component.scss']
})
export class DesktopComponent implements OnDestroy, OnInit {

  _destroyed: Subject<void>
  links: HeaderLinks

  constructor(private headerService: HeaderService) {
    this._destroyed = new Subject<void>();
    this.links = {
      links: [],
      more: []
    }
  }

  ngOnInit() {
    this.headerService.header
      .pipe(takeUntil(this._destroyed))
      .subscribe(h => {
        this.links = h.links
      })
  }

  ngOnDestroy() {
    this._destroyed.next();
    this._destroyed.complete()
  }

}
