import {
  AfterViewInit,
  Component,
  ComponentRef,
  computed, ElementRef,
  OnDestroy,
  OnInit,
  Type,
  ViewContainerRef
} from '@angular/core';
import {Header, HeaderLink, HeaderService} from "../service/header.service";
import {BreakpointObserver, Breakpoints} from "@angular/cdk/layout";
import {filter, Subject, takeUntil} from "rxjs";
import {ActivatedRoute, ActivationStart, Router, RoutesRecognized} from "@angular/router";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit, OnDestroy {

  headerPosition: string
  header: Header | undefined
  isScreenLarge: boolean
  _destoryed: Subject<void>
  constructor(
    private headerService: HeaderService,
    private breakpointObserver: BreakpointObserver,
    private router: Router,
  ) {
    this.headerPosition = 'sticky'
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
        if (d && d['header-position'])
          this.headerPosition = d['header-position']
        else
          this.headerPosition = 'sticky'
    })
  }

  ngOnDestroy() {
    this._destoryed.next()
    this._destoryed.complete()
  }

}
