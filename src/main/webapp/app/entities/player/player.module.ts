import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BillsSharedModule } from 'app/shared/shared.module';
import { PlayerComponent } from './player.component';
import { PlayerDetailComponent } from './player-detail.component';
import { PlayerUpdateComponent } from './player-update.component';
import { PlayerDeletePopupComponent, PlayerDeleteDialogComponent } from './player-delete-dialog.component';
import { playerRoute, playerPopupRoute } from './player.route';

const ENTITY_STATES = [...playerRoute, ...playerPopupRoute];

@NgModule({
  imports: [BillsSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [PlayerComponent, PlayerDetailComponent, PlayerUpdateComponent, PlayerDeleteDialogComponent, PlayerDeletePopupComponent],
  entryComponents: [PlayerDeleteDialogComponent]
})
export class BillsPlayerModule {}
