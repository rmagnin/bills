import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BillsSharedModule } from 'app/shared/shared.module';
import { ChampionshipComponent } from './championship.component';
import { ChampionshipDetailComponent } from './championship-detail.component';
import { ChampionshipUpdateComponent } from './championship-update.component';
import { ChampionshipDeletePopupComponent, ChampionshipDeleteDialogComponent } from './championship-delete-dialog.component';
import { championshipRoute, championshipPopupRoute } from './championship.route';

const ENTITY_STATES = [...championshipRoute, ...championshipPopupRoute];

@NgModule({
  imports: [BillsSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ChampionshipComponent,
    ChampionshipDetailComponent,
    ChampionshipUpdateComponent,
    ChampionshipDeleteDialogComponent,
    ChampionshipDeletePopupComponent
  ],
  entryComponents: [ChampionshipDeleteDialogComponent]
})
export class BillsChampionshipModule {}
