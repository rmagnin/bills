export interface IParticipation {
  id?: number;
  singleEvent?: boolean;
  doubleEvent?: boolean;
  mixedEvent?: boolean;
  championshipName?: string;
  championshipId?: number;
  playerLastName?: string;
  playerId?: number;
}

export class Participation implements IParticipation {
  constructor(
    public id?: number,
    public singleEvent?: boolean,
    public doubleEvent?: boolean,
    public mixedEvent?: boolean,
    public championshipName?: string,
    public championshipId?: number,
    public playerLastName?: string,
    public playerId?: number
  ) {
    this.singleEvent = this.singleEvent || false;
    this.doubleEvent = this.doubleEvent || false;
    this.mixedEvent = this.mixedEvent || false;
  }
}
