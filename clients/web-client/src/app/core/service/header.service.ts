import {Injectable, Type} from '@angular/core';
import {BehaviorSubject, Observable, Observer, Subject} from "rxjs";

export interface Header {
  pre?: Type<any>
  post?: Type<any>
  logo?: string;
  links: HeaderLinks;
  actions?: string[];
}
export interface HeaderLink {
  name: string;
  path: string;
  position: number;
  indicator?: HeaderIndicator
}
export interface HeaderIndicator {
  color: string;
  content: string;
}

export interface HeaderLinks {
  links: HeaderLink[];
  other?: HeaderLink[];
}
@Injectable({
  providedIn: 'root'
})
export class HeaderService {

  private $header: Observable<Header>
  private _header: BehaviorSubject<Header>
  constructor() {
    this._header = new BehaviorSubject<Header>({
      links: {
        links: [],
        other: []
      }
    })
    this.$header = this._header.asObservable()
  }

  addLink(link: HeaderLink): void {
    const header = this._header.getValue();
    header.links.links.push(link)
    this._header.next(header)
  }

  addOtherLink(link: HeaderLink): void {
    const header = this._header.getValue();
    if (header.links.other)
      header.links.other.push(link)
    else
      header.links.other = [link]
    this._header.next(header)
  }

  get  header(): Observable<Header>{
    return this.$header
  }
}
