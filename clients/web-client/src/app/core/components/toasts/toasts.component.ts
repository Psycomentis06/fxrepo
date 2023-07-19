import {Component, OnDestroy, OnInit} from '@angular/core';
import {Toast, ToastActionType, ToastService, ToastType} from "../../service/toast.service";
import {Subject, takeUntil} from "rxjs";

@Component({
  selector: 'app-toasts',
  templateUrl: './toasts.component.html',
  styleUrls: ['./toasts.component.scss']
})
export class ToastsComponent implements OnInit, OnDestroy {

  protected readonly ToastType = ToastType;
  toasts: Toast[] = []
  _destroyed: Subject<void>
  constructor(private toastService: ToastService) {
    this._destroyed = new Subject<void>()
  }

  ngOnInit() {
    this.toastService.toasts
      .pipe(takeUntil(this._destroyed))
      .subscribe(t => {
      this.toasts = t
    })
  }

  ngOnDestroy() {
    this._destroyed.next();
    this._destroyed.complete();
  }

  removeToast(t: Toast) {
    this.toastService.remove(t.id)
  }
}
