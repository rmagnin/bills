export interface IChampionship {
  id?: number;
  name?: string;
  year?: number;
  oneEventPrice?: number;
  twoEventsPrice?: number;
  threeEventsPrice?: number;
  clubId?: number;
}

export class Championship implements IChampionship {
  constructor(
    public id?: number,
    public name?: string,
    public year?: number,
    public oneEventPrice?: number,
    public twoEventsPrice?: number,
    public threeEventsPrice?: number,
    public clubId?: number
  ) {}
}
