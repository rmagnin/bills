import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IBillLine, BillLine } from 'app/shared/model/bill-line.model';
import { BillLineService } from './bill-line.service';
import { IParticipation } from 'app/shared/model/participation.model';
import { ParticipationService } from 'app/entities/participation/participation.service';
import { IBill } from 'app/shared/model/bill.model';
import { BillService } from 'app/entities/bill/bill.service';

@Component({
  selector: 'jhi-bill-line-update',
  templateUrl: './bill-line-update.component.html'
})
export class BillLineUpdateComponent implements OnInit {
  isSaving: boolean;

  participations: IParticipation[];

  bills: IBill[];

  editForm = this.fb.group({
    id: [],
    label: [null, [Validators.required]],
    amount: [],
    participationId: [],
    billId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected billLineService: BillLineService,
    protected participationService: ParticipationService,
    protected billService: BillService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ billLine }) => {
      this.updateForm(billLine);
    });
    this.participationService
      .query({ filter: 'billline-is-null' })
      .pipe(
        filter((mayBeOk: HttpResponse<IParticipation[]>) => mayBeOk.ok),
        map((response: HttpResponse<IParticipation[]>) => response.body)
      )
      .subscribe(
        (res: IParticipation[]) => {
          if (!this.editForm.get('participationId').value) {
            this.participations = res;
          } else {
            this.participationService
              .find(this.editForm.get('participationId').value)
              .pipe(
                filter((subResMayBeOk: HttpResponse<IParticipation>) => subResMayBeOk.ok),
                map((subResponse: HttpResponse<IParticipation>) => subResponse.body)
              )
              .subscribe(
                (subRes: IParticipation) => (this.participations = [subRes].concat(res)),
                (subRes: HttpErrorResponse) => this.onError(subRes.message)
              );
          }
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
    this.billService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IBill[]>) => mayBeOk.ok),
        map((response: HttpResponse<IBill[]>) => response.body)
      )
      .subscribe((res: IBill[]) => (this.bills = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(billLine: IBillLine) {
    this.editForm.patchValue({
      id: billLine.id,
      label: billLine.label,
      amount: billLine.amount,
      participationId: billLine.participationId,
      billId: billLine.billId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const billLine = this.createFromForm();
    if (billLine.id !== undefined) {
      this.subscribeToSaveResponse(this.billLineService.update(billLine));
    } else {
      this.subscribeToSaveResponse(this.billLineService.create(billLine));
    }
  }

  private createFromForm(): IBillLine {
    return {
      ...new BillLine(),
      id: this.editForm.get(['id']).value,
      label: this.editForm.get(['label']).value,
      amount: this.editForm.get(['amount']).value,
      participationId: this.editForm.get(['participationId']).value,
      billId: this.editForm.get(['billId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBillLine>>) {
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

  trackParticipationById(index: number, item: IParticipation) {
    return item.id;
  }

  trackBillById(index: number, item: IBill) {
    return item.id;
  }
}
