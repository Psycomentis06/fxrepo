import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subject, takeUntil} from "rxjs";
import {HeaderLinks, HeaderService} from "../../../../service/header.service";

@Component({
  selector: 'app-mobile',
  templateUrl: './mobile.component.html',
  styleUrls: ['./mobile.component.scss']
})
export class MobileComponent implements OnDestroy, OnInit {

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
