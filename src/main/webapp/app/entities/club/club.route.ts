import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Club } from 'app/shared/model/club.model';
import { ClubService } from './club.service';
import { ClubComponent } from './club.component';
import { ClubDetailComponent } from './club-detail.component';
import { ClubUpdateComponent } from './club-update.component';
import { ClubDeletePopupComponent } from './club-delete-dialog.component';
import { IClub } from 'app/shared/model/club.model';

@Injectable({ providedIn: 'root' })
export class ClubResolve implements Resolve<IClub> {
  constructor(private service: ClubService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IClub> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Club>) => response.ok),
        map((club: HttpResponse<Club>) => club.body)
      );
    }
    return of(new Club());
  }
}

export const clubRoute: Routes = [
  {
    path: '',
    component: ClubComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'Clubs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ClubDetailComponent,
    resolve: {
      club: ClubResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Clubs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ClubUpdateComponent,
    resolve: {
      club: ClubResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Clubs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ClubUpdateComponent,
    resolve: {
      club: ClubResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Clubs'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const clubPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ClubDeletePopupComponent,
    resolve: {
      club: ClubResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Clubs'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
