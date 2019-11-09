import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';
import { IBill, Bill } from 'app/shared/model/bill.model';
import { BillService } from './bill.service';
import { IPlayer } from 'app/shared/model/player.model';
import { PlayerService } from 'app/entities/player/player.service';

@Component({
  selector: 'jhi-bill-update',
  templateUrl: './bill-update.component.html'
})
export class BillUpdateComponent implements OnInit {
  isSaving: boolean;

  players: IPlayer[];
  creationDateDp: any;

  editForm = this.fb.group({
    id: [],
    creationDate: [null, [Validators.required]],
    status: [],
    amount: [],
    playerId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected billService: BillService,
    protected playerService: PlayerService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ bill }) => {
      this.updateForm(bill);
    });
    this.playerService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IPlayer[]>) => mayBeOk.ok),
        map((response: HttpResponse<IPlayer[]>) => response.body)
      )
      .subscribe((res: IPlayer[]) => (this.players = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(bill: IBill) {
    this.editForm.patchValue({
      id: bill.id,
      creationDate: bill.creationDate,
      status: bill.status,
      amount: bill.amount,
      playerId: bill.playerId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const bill = this.createFromForm();
    if (bill.id !== undefined) {
      this.subscribeToSaveResponse(this.billService.update(bill));
    } else {
      this.subscribeToSaveResponse(this.billService.create(bill));
    }
  }

  private createFromForm(): IBill {
    return {
      ...new Bill(),
      id: this.editForm.get(['id']).value,
      creationDate: this.editForm.get(['creationDate']).value,
      status: this.editForm.get(['status']).value,
      amount: this.editForm.get(['amount']).value,
      playerId: this.editForm.get(['playerId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBill>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackPlayerById(index: number, item: IPlayer) {
    return item.id;
  }
}
