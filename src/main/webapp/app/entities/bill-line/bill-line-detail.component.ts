import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBillLine } from 'app/shared/model/bill-line.model';

@Component({
  selector: 'jhi-bill-line-detail',
  templateUrl: './bill-line-detail.component.html'
})
export class BillLineDetailComponent implements OnInit {
  billLine: IBillLine;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ billLine }) => {
      this.billLine = billLine;
    });
  }

  previousState() {
    window.history.back();
  }
}
