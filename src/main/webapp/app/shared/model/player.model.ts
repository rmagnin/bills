import { IParticipation } from 'app/shared/model/participation.model';
import { IBill } from 'app/shared/model/bill.model';

export interface IPlayer {
  id?: number;
  firstName?: string;
  lastName?: string;
  participations?: IParticipation[];
  bills?: IBill[];
}

export class Player implements IPlayer {
  constructor(
    public id?: number,
    public firstName?: string,
    public lastName?: string,
    public participations?: IParticipation[],
    public bills?: IBill[]
  ) {}
}
