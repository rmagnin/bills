import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'player',
        loadChildren: () => import('./player/player.module').then(m => m.BillsPlayerModule)
      },
      {
        path: 'club',
        loadChildren: () => import('./club/club.module').then(m => m.BillsClubModule)
      },
      {
        path: 'championship',
        loadChildren: () => import('./championship/championship.module').then(m => m.BillsChampionshipModule)
      },
      {
        path: 'participation',
        loadChildren: () => import('./participation/participation.module').then(m => m.BillsParticipationModule)
      },
      {
        path: 'bill-line',
        loadChildren: () => import('./bill-line/bill-line.module').then(m => m.BillsBillLineModule)
      },
      {
        path: 'bill',
        loadChildren: () => import('./bill/bill.module').then(m => m.BillsBillModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class BillsEntityModule {}
