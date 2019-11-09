import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IParticipation, Participation } from 'app/shared/model/participation.model';
import { ParticipationService } from './participation.service';
import { IChampionship } from 'app/shared/model/championship.model';
import { ChampionshipService } from 'app/entities/championship/championship.service';
import { IPlayer } from 'app/shared/model/player.model';
import { PlayerService } from 'app/entities/player/player.service';

@Component({
  selector: 'jhi-participation-update',
  templateUrl: './participation-update.component.html'
})
export class ParticipationUpdateComponent implements OnInit {
  isSaving: boolean;

  championships: IChampionship[];

  players: IPlayer[];

  editForm = this.fb.group({
    id: [],
    singleEvent: [],
    doubleEvent: [],
    mixedEvent: [],
    championshipId: [],
    playerId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected participationService: ParticipationService,
    protected championshipService: ChampionshipService,
    protected playerService: PlayerService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ participation }) => {
      this.updateForm(participation);
    });
    this.championshipService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IChampionship[]>) => mayBeOk.ok),
        map((response: HttpResponse<IChampionship[]>) => response.body)
      )
      .subscribe((res: IChampionship[]) => (this.championships = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.playerService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IPlayer[]>) => mayBeOk.ok),
        map((response: HttpResponse<IPlayer[]>) => response.body)
      )
      .subscribe((res: IPlayer[]) => (this.players = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(participation: IParticipation) {
    this.editForm.patchValue({
      id: participation.id,
      singleEvent: participation.singleEvent,
      doubleEvent: participation.doubleEvent,
      mixedEvent: participation.mixedEvent,
      championshipId: participation.championshipId,
      playerId: participation.playerId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const participation = this.createFromForm();
    if (participation.id !== undefined) {
      this.subscribeToSaveResponse(this.participationService.update(participation));
    } else {
      this.subscribeToSaveResponse(this.participationService.create(participation));
    }
  }

  private createFromForm(): IParticipation {
    return {
      ...new Participation(),
      id: this.editForm.get(['id']).value,
      singleEvent: this.editForm.get(['singleEvent']).value,
      doubleEvent: this.editForm.get(['doubleEvent']).value,
      mixedEvent: this.editForm.get(['mixedEvent']).value,
      championshipId: this.editForm.get(['championshipId']).value,
      playerId: this.editForm.get(['playerId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IParticipation>>) {
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

  trackChampionshipById(index: number, item: IChampionship) {
    return item.id;
  }

  trackPlayerById(index: number, item: IPlayer) {
    return item.id;
  }
}
