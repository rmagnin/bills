import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BillsSharedModule } from 'app/shared/shared.module';
import { ClubComponent } from './club.component';
import { ClubDetailComponent } from './club-detail.component';
import { ClubUpdateComponent } from './club-update.component';
import { ClubDeletePopupComponent, ClubDeleteDialogComponent } from './club-delete-dialog.component';
import { clubRoute, clubPopupRoute } from './club.route';

const ENTITY_STATES = [...clubRoute, ...clubPopupRoute];

@NgModule({
  imports: [BillsSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [ClubComponent, ClubDetailComponent, ClubUpdateComponent, ClubDeleteDialogComponent, ClubDeletePopupComponent],
  entryComponents: [ClubDeleteDialogComponent]
})
export class BillsClubModule {}
