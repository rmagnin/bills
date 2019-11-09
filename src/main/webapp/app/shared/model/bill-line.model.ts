export interface IBillLine {
  id?: number;
  label?: string;
  amount?: number;
  participationId?: number;
  billId?: number;
}

export class BillLine implements IBillLine {
  constructor(public id?: number, public label?: string, public amount?: number, public participationId?: number, public billId?: number) {}
}
