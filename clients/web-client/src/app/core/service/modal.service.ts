import {Injectable, Type} from '@angular/core';
import {BehaviorSubject, Observable} from "rxjs";


export enum ModalActionType {
  primary = 'btn-primary',
  secondary = 'btn-secondary',
  success = 'btn-success',
  error = 'btn-error',
  info = 'btn-info',
  warning = 'btn-warning'
}
export interface Modal {
  title?: string
  content?: string
  template?: Type<any>
  actions?: ModalAction[]
  cornerClose?: boolean
}

export interface ModalAction {
  title: string
  type?: ModalActionType
  close?: boolean
  onClick?: () => void
}

@Injectable({
  providedIn: 'root'
})
export class ModalService {

  private _modal: BehaviorSubject<Modal | null>
  private $modal: Observable<Modal | null>

  constructor() {
    this._modal = new BehaviorSubject<Modal | null>(null)
    this.$modal = this._modal.asObservable()
  }

  showModal(m: Modal): void {
    this._modal.next(m)
  }

  closeModal(): void {
    this._modal.next(null)
  }

  get modal() {
    return this.$modal
  }
}
