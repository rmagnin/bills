import { IBillLine } from 'app/shared/model/bill-line.model';
import { BillStatus } from 'app/shared/model/enumerations/bill-status.model';

export interface IBill {
  id?: number;
  status?: BillStatus;
  amount?: number;
  playerId?: number;
  lines?: IBillLine[];
}

export class Bill implements IBill {
  constructor(
    public id?: number,
    public status?: BillStatus,
    public amount?: number,
    public playerId?: number,
    public lines?: IBillLine[]
  ) {}
}
