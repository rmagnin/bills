import { IParticipation } from 'app/shared/model/participation.model';

export interface IChampionship {
  id?: number;
  name?: string;
  year?: number;
  oneEventPrice?: number;
  twoEventsPrice?: number;
  threeEventsPrice?: number;
  clubId?: number;
  participations?: IParticipation[];
}

export class Championship implements IChampionship {
  constructor(
    public id?: number,
    public name?: string,
    public year?: number,
    public oneEventPrice?: number,
    public twoEventsPrice?: number,
    public threeEventsPrice?: number,
    public clubId?: number,
    public participations?: IParticipation[]
  ) {}
}
