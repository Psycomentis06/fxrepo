import {Component, OnDestroy, OnInit} from '@angular/core';
import {Header, HeaderService} from "../../service/header.service";
import {BreakpointObserver, Breakpoints} from "@angular/cdk/layout";
import {filter, Subject, takeUntil} from "rxjs";
import {ActivationStart, Router} from "@angular/router";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit, OnDestroy {

  headerHidden: boolean
  headerPosition: string
  headerBgColor: string
  header: Header | undefined
  isScreenLarge: boolean
  _destoryed: Subject<void>

  constructor(
    private headerService: HeaderService,
    private breakpointObserver: BreakpointObserver,
    private router: Router,
  ) {
    this.headerHidden = false
    this.headerPosition = 'sticky'
    this.headerBgColor = 'bg-base-300'
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

    this.router.events
      .pipe(
        takeUntil(this._destoryed),
        filter(e => e instanceof ActivationStart)
      )
      .subscribe(e => {
        const event = e as ActivationStart
        const d = event.snapshot.data
        if (d) {
          if (d['header-position']) {
            this.headerPosition = d['header-position']
          } else {
            this.headerPosition = 'sticky'
          }

          if (d['header-bg']) {
            this.headerBgColor = d['header-bg']
          } else {
            this.headerBgColor = 'bg-base-300'
          }

          if (d['header-hidden']) {
            this.headerHidden = d['header-hidden']
          } else {
            this.headerHidden = false
          }
        }
      })
  }

  ngOnDestroy() {
    this._destoryed.next()
    this._destoryed.complete()
  }

}
