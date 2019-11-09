import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IClub, Club } from 'app/shared/model/club.model';
import { ClubService } from './club.service';

@Component({
  selector: 'jhi-club-update',
  templateUrl: './club-update.component.html'
})
export class ClubUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    defaultChampionshipName: [],
    defaultOneEventPrice: [],
    defaultTwoEventsPrice: [],
    defaultThreeEventsPrice: []
  });

  constructor(protected clubService: ClubService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ club }) => {
      this.updateForm(club);
    });
  }

  updateForm(club: IClub) {
    this.editForm.patchValue({
      id: club.id,
      name: club.name,
      defaultChampionshipName: club.defaultChampionshipName,
      defaultOneEventPrice: club.defaultOneEventPrice,
      defaultTwoEventsPrice: club.defaultTwoEventsPrice,
      defaultThreeEventsPrice: club.defaultThreeEventsPrice
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const club = this.createFromForm();
    if (club.id !== undefined) {
      this.subscribeToSaveResponse(this.clubService.update(club));
    } else {
      this.subscribeToSaveResponse(this.clubService.create(club));
    }
  }

  private createFromForm(): IClub {
    return {
      ...new Club(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      defaultChampionshipName: this.editForm.get(['defaultChampionshipName']).value,
      defaultOneEventPrice: this.editForm.get(['defaultOneEventPrice']).value,
      defaultTwoEventsPrice: this.editForm.get(['defaultTwoEventsPrice']).value,
      defaultThreeEventsPrice: this.editForm.get(['defaultThreeEventsPrice']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClub>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
