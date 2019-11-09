import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Championship } from 'app/shared/model/championship.model';
import { ChampionshipService } from './championship.service';
import { ChampionshipComponent } from './championship.component';
import { ChampionshipDetailComponent } from './championship-detail.component';
import { ChampionshipUpdateComponent } from './championship-update.component';
import { ChampionshipDeletePopupComponent } from './championship-delete-dialog.component';
import { IChampionship } from 'app/shared/model/championship.model';

@Injectable({ providedIn: 'root' })
export class ChampionshipResolve implements Resolve<IChampionship> {
  constructor(private service: ChampionshipService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IChampionship> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Championship>) => response.ok),
        map((championship: HttpResponse<Championship>) => championship.body)
      );
    }
    return of(new Championship());
  }
}

export const championshipRoute: Routes = [
  {
    path: '',
    component: ChampionshipComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'Championships'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ChampionshipDetailComponent,
    resolve: {
      championship: ChampionshipResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Championships'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ChampionshipUpdateComponent,
    resolve: {
      championship: ChampionshipResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Championships'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ChampionshipUpdateComponent,
    resolve: {
      championship: ChampionshipResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Championships'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const championshipPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ChampionshipDeletePopupComponent,
    resolve: {
      championship: ChampionshipResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Championships'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
