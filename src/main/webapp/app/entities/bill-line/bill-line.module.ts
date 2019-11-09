import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BillsSharedModule } from 'app/shared/shared.module';
import { BillLineComponent } from './bill-line.component';
import { BillLineDetailComponent } from './bill-line-detail.component';
import { BillLineUpdateComponent } from './bill-line-update.component';
import { BillLineDeletePopupComponent, BillLineDeleteDialogComponent } from './bill-line-delete-dialog.component';
import { billLineRoute, billLinePopupRoute } from './bill-line.route';

const ENTITY_STATES = [...billLineRoute, ...billLinePopupRoute];

@NgModule({
  imports: [BillsSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    BillLineComponent,
    BillLineDetailComponent,
    BillLineUpdateComponent,
    BillLineDeleteDialogComponent,
    BillLineDeletePopupComponent
  ],
  entryComponents: [BillLineDeleteDialogComponent]
})
export class BillsBillLineModule {}
