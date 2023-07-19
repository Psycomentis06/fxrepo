import {AfterViewInit, Component, ElementRef, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {Modal, ModalActionType, ModalService} from "../../service/modal.service";
import {Subject, takeUntil} from "rxjs";
import {FooterComponent} from "../footer/footer.component";

@Component({
  selector: 'app-modal',
  templateUrl: './modal.component.html',
  styleUrls: ['./modal.component.scss']
})
export class ModalComponent implements OnInit, OnDestroy, AfterViewInit {
  @ViewChild('dialogElement') dialog?: ElementRef<HTMLDialogElement>
  _destroyed
  modal: Modal | null
  constructor(private modalService: ModalService) {
    this._destroyed = new Subject<void>()
    this.modal = null
  }

  ngOnInit() {
    this.modalService.modal
      .pipe(takeUntil(this._destroyed))
      .subscribe(m => {
        this.modal = m
      })
  }

  ngAfterViewInit() {
    if (this.dialog) {
      this.dialog.nativeElement.showModal()
    }
  }

  ngOnDestroy() {
    this._destroyed.next();
    this._destroyed.complete()
  }

  close() {
    if (this.dialog) {
      this.dialog.nativeElement.close()
      this.modalService.closeModal()
    }
  }
}
