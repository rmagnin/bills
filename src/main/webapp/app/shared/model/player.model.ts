export interface IPlayer {
  id?: number;
  firstName?: string;
  lastName?: string;
}

export class Player implements IPlayer {
  constructor(public id?: number, public firstName?: string, public lastName?: string) {}
}
