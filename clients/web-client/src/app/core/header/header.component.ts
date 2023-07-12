import {Component, OnDestroy, OnInit} from '@angular/core';
import {Header, HeaderLink, HeaderService} from "../service/header.service";
import {BreakpointObserver, Breakpoints} from "@angular/cdk/layout";
import {Subject, takeUntil} from "rxjs";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit, OnDestroy {

  header: Header | undefined
  isScreenLarge: boolean
  _destoryed: Subject<void>
  constructor(public headerService: HeaderService, breakpointObserver: BreakpointObserver) {
    this.isScreenLarge = false
    this._destoryed = new Subject<void>()
    breakpointObserver
      .observe([Breakpoints.Small, Breakpoints.XSmall])
      .pipe(takeUntil(this._destoryed))
      .subscribe(result => {
        this.isScreenLarge = !result.matches;
      })
  }

  ngOnInit() {
    this.headerService.header
      .pipe(takeUntil(this._destoryed))
      .subscribe(o => {
      this.header = o
    })
  }

  ngOnDestroy() {
    this._destoryed.next()
    this._destoryed.complete()
  }
}
