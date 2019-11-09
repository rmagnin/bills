export interface IParticipation {
  id?: number;
  singleEvent?: boolean;
  doubleEvent?: boolean;
  mixedEvent?: boolean;
  championshipId?: number;
  playerId?: number;
}

export class Participation implements IParticipation {
  constructor(
    public id?: number,
    public singleEvent?: boolean,
    public doubleEvent?: boolean,
    public mixedEvent?: boolean,
    public championshipId?: number,
    public playerId?: number
  ) {
    this.singleEvent = this.singleEvent || false;
    this.doubleEvent = this.doubleEvent || false;
    this.mixedEvent = this.mixedEvent || false;
  }
}
