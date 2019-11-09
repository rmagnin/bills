import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BillsSharedModule } from 'app/shared/shared.module';
import { ParticipationComponent } from './participation.component';
import { ParticipationDetailComponent } from './participation-detail.component';
import { ParticipationUpdateComponent } from './participation-update.component';
import { ParticipationDeletePopupComponent, ParticipationDeleteDialogComponent } from './participation-delete-dialog.component';
import { participationRoute, participationPopupRoute } from './participation.route';

const ENTITY_STATES = [...participationRoute, ...participationPopupRoute];

@NgModule({
  imports: [BillsSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ParticipationComponent,
    ParticipationDetailComponent,
    ParticipationUpdateComponent,
    ParticipationDeleteDialogComponent,
    ParticipationDeletePopupComponent
  ],
  entryComponents: [ParticipationDeleteDialogComponent]
})
export class BillsParticipationModule {}
