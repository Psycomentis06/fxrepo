import { Injectable } from '@angular/core';
import {BehaviorSubject, Observable} from "rxjs";

export enum NotificationType {
  success = 'alert-success',
  error = 'alert-error',
  info = 'alert-info',
  warning = 'alert-warning'
}
export interface Notification {
  id: number
  title: string,
  content?: string
  thumbnail?: string,
  uri?: string
  type?: NotificationType
}


@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  private _notifications: BehaviorSubject<Notification[]>
  private $notifications: Observable<Notification[]>
  constructor() {
    this._notifications = new BehaviorSubject<Notification[]>([])
    this.$notifications = this._notifications.asObservable();
  }

  addNotification(n: Notification) {
    const tmpNotif = this._notifications.getValue()
    tmpNotif.push(n)
    this._notifications.next(tmpNotif);
  }

  removeNotification(n: Notification) {
    let tmpNotif = this._notifications.getValue()
    tmpNotif = tmpNotif.filter(x => x.id !== n.id)
    this._notifications.next(tmpNotif);
  }

  get notifications() {
    return this.$notifications
  }
}
