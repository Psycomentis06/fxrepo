import {Injectable, Type} from '@angular/core';
import {BehaviorSubject, Observable} from "rxjs";

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
  disabled: boolean;
  indicator?: HeaderIndicator
}

export interface HeaderIndicator {
  color: string;
  content: string;
}

export interface HeaderLinks {
  links: HeaderLink[];
  more?: HeaderLink[];
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
        more: []
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
    if (header.links.more)
      header.links.more.push(link)
    else
      header.links.more = [link]
    this._header.next(header)
  }

  setPost(post?: Type<any>): void {
    const header = this._header.getValue();
    header.post = post
    this._header.next(header)
  }

  setPre(pre?: Type<any>): void {
    const header = this._header.getValue();
    header.pre = pre
    this._header.next(header)
  }

  setLinks(links: HeaderLinks): void {
    const header = this._header.getValue();
    header.links = links
    this._header.next(header)
  }

  setHeader(header: Header): void {
    this._header.next(header)
  }

  get header(): Observable<Header> {
    return this.$header
  }

}
