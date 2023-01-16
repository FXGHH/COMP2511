import { TileSet } from "../utils/Tileset"
import { Dungeon } from "./Types"

export type SkinFile = {
  main_menu: {
    background_image: string;
    text_color: string;
    background_music: TileSet | string;
  };
  game: {
    background_image: TileSet | string;
    resolution_px: number;
    item_box: string;
    text_color: string;
    background_music: TileSet | string;
  },
  entities: { [type: string]: TileSet | string },
}

export type LocalisationFile = {
  main_menu: {
    font: string;
    title: string;
    subtitle_1: string;
    subtitle_2: string;
    buttons: {
      new_game: string;
      generate_game: string;
      load_game: string;
      credits: string;
      quit_game: string;
    };
    game_mode: string;
    on_exit: {
      title: string;
      message: string;
    }
  };
  game: {
    font: string;
    build: string;
    inventory: string;
    save: string;
    main_menu: string;
  }
}

export interface Config {
  skinFileName: string;
  localisationLanguage: string;
  skinFile: SkinFile;
  localisation: LocalisationFile;
}

declare global {
  interface Window { Config: Config; Dungeon: Dungeon }
}