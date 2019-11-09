import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { BillsTestModule } from '../../../test.module';
import { BillLineDeleteDialogComponent } from 'app/entities/bill-line/bill-line-delete-dialog.component';
import { BillLineService } from 'app/entities/bill-line/bill-line.service';

describe('Component Tests', () => {
  describe('BillLine Management Delete Component', () => {
    let comp: BillLineDeleteDialogComponent;
    let fixture: ComponentFixture<BillLineDeleteDialogComponent>;
    let service: BillLineService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BillsTestModule],
        declarations: [BillLineDeleteDialogComponent]
      })
        .overrideTemplate(BillLineDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BillLineDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(BillLineService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
