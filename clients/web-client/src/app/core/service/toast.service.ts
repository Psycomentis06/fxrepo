import { Injectable } from '@angular/core';
import {BehaviorSubject, Observable} from "rxjs";

export enum ToastType {
  success = 'alert-success',
  error = 'alert-error',
  info = 'alert-info',
  warning = 'alert-warning'
}

export enum ToastActionType {
  primary = 'btn-primary',
  secondary = 'btn-secondary',
  success = 'btn-success',
  error = 'btn-error',
  info = 'btn-info',
  warning = 'btn-warning'
}

export interface Toast {
  id: number
  title: string
  content?: string
  showIcon?: boolean
  type?: ToastType
  actions?: ToastAction[]
}

export interface ToastAction {
  title: string
  type?: ToastActionType
  close?: boolean
  onClick?: () => void
}

@Injectable({
  providedIn: 'root'
})
export class ToastService {
  private $toasts: Observable<Toast[]>
  private _toasts: BehaviorSubject<Toast[]>

  constructor() {
    this._toasts = new BehaviorSubject<Toast[]>([])
    this.$toasts = this._toasts.asObservable()
  }

  add(t: Toast, duration = 5000) {
    const tmpT = this._toasts.getValue()
    tmpT.push(t)
    this._toasts.next(tmpT)
    if (duration > 0) {
      setTimeout(() => {
        this.remove(t.id)
      }, duration)
    }
  }

  remove(id: number) {
    let tmpT = this._toasts.getValue()
    tmpT = tmpT.filter(t => t.id !== id)
    this._toasts.next(tmpT)
  }

  get toasts() {
    return this.$toasts
  }
}
