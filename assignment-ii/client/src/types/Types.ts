export interface Dungeon {
  dungeonName: string;
  configName: string;
  entities: EntityResponse[];
  inventory: ItemResponse[];
  buildables: string[];
  goals: string;
  animations: Animation[];
}

export interface ItemResponse {
  id: string;
  type: string;
  icon: Icon;
}

export interface Icon {
  name: string;
  width: number;
  height: number;
}

export interface Position {
  x: number;
  y: number;
  layer: number;
}

export interface EntityResponse {
  id: string;
  type: string;
  icon: Icon;
  position: Position;
  isInteractable: boolean;
}

export interface GameResponse {
  dungeonId: string;
  name: string;
  lastSaved: string;
}

export interface Animation {
  when: string;
  entityId: string;
  queue: string[];
  loop: boolean;
  duration: number;
}
