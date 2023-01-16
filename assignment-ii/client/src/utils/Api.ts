import axios, { AxiosResponse } from "axios";
import { Dungeon, GameResponse } from "../types/Types";

import Swal from "sweetalert2";
import { ErrorAlert } from "./Alerts";
import { LocalisationFile, SkinFile } from "../types/Module";

const URL = ""

export function detectWebGL() {
  // Check for the WebGL rendering context
  if (!!window.WebGLRenderingContext) {
    var canvas = document.createElement("canvas"),
      names = ["webgl", "experimental-webgl", "moz-webgl", "webkit-3d"],
      context = false;

    for (var i in names) {
      try {
        const context = canvas.getContext(names[i]);
        if (
          context &&
          typeof (context as WebGL2RenderingContext).getParameter === "function"
        ) {
          // WebGL is enabled.
          return true;
        }
      } catch (e) {}
    }

    // WebGL is supported, but disabled.
    return false;
  }

  // WebGL not supported.
  return false;
}

let currentResponse: Promise<AxiosResponse> | null = null;

async function evaluateResponse<T>(
  promise: Promise<AxiosResponse>,
  title: string,
  otherwise: T
): Promise<T> {
  try {
    const resp = await promise;
    if (resp.data && resp.data.isError) {
      ErrorAlert(title, resp.data.errorTitle + ":\n" + resp.data.errorMessage);
      return otherwise;
    } else {
      return resp.data.result as T;
    }
  } catch (err) {
    ErrorAlert(
      "Unknown Error: " + (err && err.statusCode) || "",
      err.data || err
    );
    return otherwise;
  }
}

async function evaluateAsset<T>(
  promise: Promise<AxiosResponse>,
  title: string,
  otherwise: T
): Promise<T> {
  try {
    const resp = await promise;
    if (!resp.data) {
      ErrorAlert(title, resp.data.errorTitle + ":\n" + resp.data.errorMessage);
      return otherwise;
    } else {
      return resp.data as T;
    }
  } catch (err) {
    ErrorAlert(
      "Unknown Error: " + ((err && err.statusCode) || ""),
      err.data || err
    );
    return otherwise;
  }
}

export const API = {
  loadSkin: (skin: string): Promise<SkinFile> =>
    evaluateAsset(
      axios.get(URL + "/skins/" + skin + ".json"),
      "Error GET /skins/" +
        skin +
        ".json: Is most likely due to the fact the file didn't exist, have a look at the Java output window",
      {} as SkinFile
    ),
  loadLocalisation: (localisation: string): Promise<LocalisationFile> =>
    evaluateAsset(
      axios.get(URL + "/languages/" + localisation + ".json"),
      "Error GET /languages/" +
        localisation +
        ".json: Is most likely due to the fact the file didn't exist, have a look at the Java output window",
      {} as LocalisationFile
    ),
  getConfigs: (): Promise<string[]> =>
      evaluateResponse(
          axios.get(URL + "/api/configs/"),
          "Error GET /configs: DungeonManiaController::configs(...)",
          []
      ),
  getSkin: (): Promise<string> =>
    evaluateResponse(
      axios.get(URL + "/api/skin/current/"),
      "Error GET /skin/current: DungeonManiaController::getSkin(...)",
      "default"
    ),
  getLocalisation: (): Promise<string> =>
    evaluateResponse(
      axios.get(URL + "/api/localisation/current/"),
      "Error GET /skin/current: DungeonManiaController::getLocalisation(...)",
      "en_US"
    ),
  getDungeons: (): Promise<string[]> =>
    evaluateResponse(
      axios.get(URL + "/api/dungeons/"),
      "Error GET /dungeons: DungeonManiaController::dungeons(...)",
      []
    ),
  newGame: (dungeonName: string, configName: string): Promise<Dungeon | null> =>
    evaluateResponse(
      axios.post(
        URL + "/api/game/new/",
        {},
        {
          params: {
            dungeonName,
            configName
          },
        }
      ),
      "Error POST /game/new: DungeonManiaController::ctor(...)",
      null
    ),
  saveGame: async (name: string): Promise<void> => {
    if (currentResponse) await currentResponse;
    return currentResponse = evaluateResponse(
      axios.post(
        URL + "/api/game/save/",
        {},
        {
          params: {
            name,
          },
        }
      ),
      "Error POST /game/save: DungeonManiaController::saveGame(...)",
      null
    );
  },
  loadGame: (name: string): Promise<Dungeon | null> =>
    evaluateResponse(
      axios.post(
        URL + "/api/game/load/",
        {},
        {
          params: {
            name,
          },
        }
      ),
      "Error POST /game/load: DungeonManiaController::loadGame(...)",
      null
    ),
  allGames: (): Promise<string[]> =>
    evaluateResponse(
      axios.get(URL + "/api/games/all/", {}),
      "Error GET /games/all/: DungeonManiaController::allGames(...)",
      []
    ),
  interact: async (entityId: string): Promise<Dungeon | null> => {
    if (currentResponse) await currentResponse;
    return (currentResponse = evaluateResponse(
      axios.post("/api/game/interact/?entityId=" + entityId, {}),
      "Error POST /games/game/interact/: DungeonManiaController::interact(...)",
      null
    ));
  },
  dungeonResponseModel: async (): Promise<Dungeon | null> => {
    if (currentResponse) await currentResponse;
    return (currentResponse = evaluateResponse(
        axios.post("/api/game/dungeonResponseModel/", {}),
        "Error POST /games/game/dungeonResponseModel/: DungeonManiaController::dungeonResponseModel(...)",
        null
    ));
  },
  tickByItem: async (
    itemUsed: string | undefined,
  ): Promise<Dungeon | null> => {
    if (currentResponse) await currentResponse;
    return (currentResponse = evaluateResponse(
      axios.post(
        URL + "/api/game/tick/item/",
        {},
        {
          params: {
            itemUsed,
          },
        }
      ),
      "Error POST /game/tick: DungeonManiaController::tick(...)",
      null
    ));
  },
  tickByMovement: async (
      movementDirection: "Up" | "Down" | "Left" | "Right" | "None"
  ): Promise<Dungeon | null> => {
    if (currentResponse) await currentResponse;
    return (currentResponse = evaluateResponse(
        axios.post(
            "/api/game/tick/movement/",
            {},
            {
              params: {
                movementDirection,
              },
            }
        ),
        "Error POST /game/tick: DungeonManiaController::tick(...)",
        null
    ));
  },
  build: async (buildable: string): Promise<Dungeon | null> => {
    if (currentResponse) await currentResponse;
    return (currentResponse = evaluateResponse(
      axios.post(
        URL + "/api/game/build/",
        {},
        {
          params: {
            buildable,
          },
        }
      ),
      "Error POST /game/build: DungeonManiaController::build(...)",
      null
    ));
  },
  rewind: async (ticks: integer): Promise<Dungeon | null> => {
    if (currentResponse) await currentResponse;
    return currentResponse = evaluateResponse(
        axios.post(
            URL + "/api/game/rewind/",
            {},
            {
              params: {
                ticks,
              },
            }
        ),
        "Error POST /game/rewind: DungeonManiaController::rewindGame(...)",
        null
    );
  },
  generate: async (xStart: integer, yStart: integer, xEnd: integer, yEnd: integer, configName : String): Promise<Dungeon | null> => {
    if (currentResponse) await currentResponse;
    return currentResponse = evaluateResponse(
        axios.post(
            URL + "/api/game/new/generate/",
            {},
            {
              params: {
                xStart,
                yStart,
                xEnd,
                yEnd,
                configName
              },
            }
        ),
        "Error POST /game/new/generate: DungeonManiaController::generateDungeon(...)",
        null
    );
  },
};
