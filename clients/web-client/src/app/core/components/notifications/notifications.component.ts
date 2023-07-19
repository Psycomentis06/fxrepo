import {Component, OnDestroy, OnInit} from '@angular/core';
import {Notification, NotificationService, NotificationType} from "../../service/notification.service";
import {Subject, takeUntil} from "rxjs";
import {Router} from "@angular/router";

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.scss']
})
export class NotificationsComponent implements OnInit, OnDestroy {
  protected readonly NotificationType = NotificationType;
  notifications: Notification[]
  private _destroyed: Subject<void>
  constructor(private notificationService: NotificationService, private router: Router) {
    this.notifications = []
    this._destroyed = new Subject<void>();
  }

  ngOnInit() {
    this.notificationService.notifications
      .pipe(takeUntil(this._destroyed))
      .subscribe(n => {
        this.notifications = n
      })
  }

  ngOnDestroy() {
    this._destroyed.next()
    this._destroyed.complete()
  }

  remove(n: Notification) {
    this.notificationService.removeNotification(n)
  }

  notificationClick(n: Notification) {
    if (n.uri) {
      this.router.navigate([n.uri])
    }
  }
}
