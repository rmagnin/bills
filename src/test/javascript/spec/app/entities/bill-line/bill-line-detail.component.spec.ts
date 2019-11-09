import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BillsTestModule } from '../../../test.module';
import { BillLineDetailComponent } from 'app/entities/bill-line/bill-line-detail.component';
import { BillLine } from 'app/shared/model/bill-line.model';

describe('Component Tests', () => {
  describe('BillLine Management Detail Component', () => {
    let comp: BillLineDetailComponent;
    let fixture: ComponentFixture<BillLineDetailComponent>;
    const route = ({ data: of({ billLine: new BillLine(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BillsTestModule],
        declarations: [BillLineDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(BillLineDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BillLineDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.billLine).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
