import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { BillsTestModule } from '../../../test.module';
import { BillLineUpdateComponent } from 'app/entities/bill-line/bill-line-update.component';
import { BillLineService } from 'app/entities/bill-line/bill-line.service';
import { BillLine } from 'app/shared/model/bill-line.model';

describe('Component Tests', () => {
  describe('BillLine Management Update Component', () => {
    let comp: BillLineUpdateComponent;
    let fixture: ComponentFixture<BillLineUpdateComponent>;
    let service: BillLineService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BillsTestModule],
        declarations: [BillLineUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(BillLineUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BillLineUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(BillLineService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new BillLine(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new BillLine();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
