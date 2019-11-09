import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBillLine } from 'app/shared/model/bill-line.model';
import { BillLineService } from './bill-line.service';

@Component({
  selector: 'jhi-bill-line-delete-dialog',
  templateUrl: './bill-line-delete-dialog.component.html'
})
export class BillLineDeleteDialogComponent {
  billLine: IBillLine;

  constructor(protected billLineService: BillLineService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.billLineService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'billLineListModification',
        content: 'Deleted an billLine'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-bill-line-delete-popup',
  template: ''
})
export class BillLineDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ billLine }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(BillLineDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.billLine = billLine;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/bill-line', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/bill-line', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
