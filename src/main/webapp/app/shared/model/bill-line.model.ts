export interface IBillLine {
  id?: number;
  amount?: number;
  participationId?: number;
  billId?: number;
}

export class BillLine implements IBillLine {
  constructor(public id?: number, public amount?: number, public participationId?: number, public billId?: number) {}
}
