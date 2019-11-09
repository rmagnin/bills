import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IChampionship } from 'app/shared/model/championship.model';
import { ChampionshipService } from './championship.service';

@Component({
  selector: 'jhi-championship-delete-dialog',
  templateUrl: './championship-delete-dialog.component.html'
})
export class ChampionshipDeleteDialogComponent {
  championship: IChampionship;

  constructor(
    protected championshipService: ChampionshipService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.championshipService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'championshipListModification',
        content: 'Deleted an championship'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-championship-delete-popup',
  template: ''
})
export class ChampionshipDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ championship }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ChampionshipDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.championship = championship;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/championship', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/championship', { outlets: { popup: null } }]);
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
