import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IChampionship, Championship } from 'app/shared/model/championship.model';
import { ChampionshipService } from './championship.service';
import { IClub } from 'app/shared/model/club.model';
import { ClubService } from 'app/entities/club/club.service';

@Component({
  selector: 'jhi-championship-update',
  templateUrl: './championship-update.component.html'
})
export class ChampionshipUpdateComponent implements OnInit {
  isSaving: boolean;

  clubs: IClub[];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    year: [],
    oneEventPrice: [],
    twoEventsPrice: [],
    threeEventsPrice: [],
    clubId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected championshipService: ChampionshipService,
    protected clubService: ClubService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ championship }) => {
      this.updateForm(championship);
    });
    this.clubService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IClub[]>) => mayBeOk.ok),
        map((response: HttpResponse<IClub[]>) => response.body)
      )
      .subscribe((res: IClub[]) => (this.clubs = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(championship: IChampionship) {
    this.editForm.patchValue({
      id: championship.id,
      name: championship.name,
      year: championship.year,
      oneEventPrice: championship.oneEventPrice,
      twoEventsPrice: championship.twoEventsPrice,
      threeEventsPrice: championship.threeEventsPrice,
      clubId: championship.clubId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const championship = this.createFromForm();
    if (championship.id !== undefined) {
      this.subscribeToSaveResponse(this.championshipService.update(championship));
    } else {
      this.subscribeToSaveResponse(this.championshipService.create(championship));
    }
  }

  private createFromForm(): IChampionship {
    return {
      ...new Championship(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      year: this.editForm.get(['year']).value,
      oneEventPrice: this.editForm.get(['oneEventPrice']).value,
      twoEventsPrice: this.editForm.get(['twoEventsPrice']).value,
      threeEventsPrice: this.editForm.get(['threeEventsPrice']).value,
      clubId: this.editForm.get(['clubId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IChampionship>>) {
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

  trackClubById(index: number, item: IClub) {
    return item.id;
  }
}
