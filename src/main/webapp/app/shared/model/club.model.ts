export interface IClub {
  id?: number;
  name?: string;
  defaultChampionshipName?: string;
  defaultOneEventPrice?: number;
  defaultTwoEventsPrice?: number;
  defaultThreeEventsPrice?: number;
}

export class Club implements IClub {
  constructor(
    public id?: number,
    public name?: string,
    public defaultChampionshipName?: string,
    public defaultOneEventPrice?: number,
    public defaultTwoEventsPrice?: number,
    public defaultThreeEventsPrice?: number
  ) {}
}
