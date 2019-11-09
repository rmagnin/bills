import { Moment } from 'moment';
import { IBillLine } from 'app/shared/model/bill-line.model';
import { BillStatus } from 'app/shared/model/enumerations/bill-status.model';

export interface IBill {
  id?: number;
  creationDate?: Moment;
  status?: BillStatus;
  amount?: number;
  playerLastName?: string;
  playerId?: number;
  lines?: IBillLine[];
}

export class Bill implements IBill {
  constructor(
    public id?: number,
    public creationDate?: Moment,
    public status?: BillStatus,
    public amount?: number,
    public playerLastName?: string,
    public playerId?: number,
    public lines?: IBillLine[]
  ) {}
}
