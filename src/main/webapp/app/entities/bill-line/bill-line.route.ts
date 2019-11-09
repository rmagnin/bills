import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { BillLine } from 'app/shared/model/bill-line.model';
import { BillLineService } from './bill-line.service';
import { BillLineComponent } from './bill-line.component';
import { BillLineDetailComponent } from './bill-line-detail.component';
import { BillLineUpdateComponent } from './bill-line-update.component';
import { BillLineDeletePopupComponent } from './bill-line-delete-dialog.component';
import { IBillLine } from 'app/shared/model/bill-line.model';

@Injectable({ providedIn: 'root' })
export class BillLineResolve implements Resolve<IBillLine> {
  constructor(private service: BillLineService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IBillLine> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<BillLine>) => response.ok),
        map((billLine: HttpResponse<BillLine>) => billLine.body)
      );
    }
    return of(new BillLine());
  }
}

export const billLineRoute: Routes = [
  {
    path: '',
    component: BillLineComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'BillLines'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: BillLineDetailComponent,
    resolve: {
      billLine: BillLineResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'BillLines'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: BillLineUpdateComponent,
    resolve: {
      billLine: BillLineResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'BillLines'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: BillLineUpdateComponent,
    resolve: {
      billLine: BillLineResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'BillLines'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const billLinePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: BillLineDeletePopupComponent,
    resolve: {
      billLine: BillLineResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'BillLines'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
