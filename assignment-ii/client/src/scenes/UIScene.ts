import { Game, Scene } from "phaser";

import { TextButton } from "../utils/TextButton";

import axios from "axios";
import Swal from "sweetalert2";
import { ErrorAlert, InfoAlert, ListAlert } from "../utils/Alerts";
import { Dungeon, ItemResponse } from "../types/Types";
import { API } from "../utils/Api";
import { Clickable } from "../utils/Clickable";
import { getFromTileSet } from "../utils/Tileset";
import {
  playBackgroundMusic,
  setupBackgroundMusic,
} from "../utils/MusicManager";
import { generateSceneWideSymbols } from "../utils/SceneWideSymbols";

class UIScene extends Scene {
  private pool: Phaser.GameObjects.Group;
  private exitButton: TextButton;
  private customPipeline: any;
  private hasSeenGoal: boolean = false;
  private hasSeenPlayer: boolean = false;

  constructor() {
    super("scene-ui");
  }

  preload() {
    (window as any).MainScene = this;

    //  enable if local
    this.load.baseURL = "/";
    // this.load.baseURL =
    //   window.location.protocol + "//" + window.location.host + "/";
    if (typeof window.Config.skinFile.game.background_image == "string") {
      this.load.image(
        "background-",
        window.Config.skinFile.game.background_image
      );
    } else {
      let count = 0;
      for (var img in window.Config.skinFile.game.background_image) {
        // it's actually pretty cheap to cache the images here because of how tiny they are
        // (this may change in the future if we have perf issues).
        this.load.image("background-" + img, img);
        count += window.Config.skinFile.game.background_image[img];
      }
    }

    this.load.image("item", window.Config.skinFile.game.item_box);

    // preload all entity types
    for (var entity in window.Config.skinFile.entities) {
      let entityImg = window.Config.skinFile.entities[entity];

      if (typeof entityImg == "string") {
        this.load.image(entity + "-", entityImg);
      } else {
        for (var img in entityImg) {
          this.load.image(entity + "-" + img, img);
        }
      }
    }

    setupBackgroundMusic(
      this,
      "game-",
      window.Config.skinFile.game.background_music
    );
  }

  create() {
    (window as any).MainScene = this;
    generateSceneWideSymbols(this);
    this.hasSeenGoal = false;
    this.hasSeenPlayer = false;

    this.exitButton = new TextButton(
      this,
      10,
      0,
      window.Config.localisation.game.main_menu,
      {
        fontSize: "25px",
        fontFamily: "game-font",
      },
      () => {
        (window as any).MainScene = null;
        this.scene.stop("scene-game");
        this.scene.start("scene-menu");
      }
    );
    this.add.existing(this.exitButton);

    let save = new TextButton(
      this,
      10,
      this.exitButton.height + 10,
      window.Config.localisation.game.save,
      {
        fontSize: "25px",
        fontFamily: "game-font",
      },
      () => {
        API.saveGame(window.Dungeon.dungeonName + "-" + new Date().getTime());
      }
    );
    this.add.existing(save);

    if (!window.Dungeon) {
      this.scene.stop("scene-ui");
    }
    this.pool = this.add.group();

    // next we need to actually generate the initial scene
    this.updateScene();
  }

  public updateScene() {
    this.pool.clear(true, true);
    const resolution_px = window.Config.skinFile.game.resolution_px;
    const offset = 75;

    if (window.Dungeon.entities.filter(x => x.type.toLowerCase() == "player").length > 0) {
      this.hasSeenPlayer = true;
    } else if (this.hasSeenPlayer) {
      InfoAlert("Oh no!", "You were defeated :(", () => {
        (window as any).MainScene = null;
        this.scene.stop("scene-game");
        this.scene.start("scene-menu");
      });
    }

    if (window.Dungeon.goals) {
      this.hasSeenGoal = true;
      let text = this.add.text(this.renderer.width * 0.5, 0, "Goals: ", {
        fontFamily: "game-font",
        fontSize: "15px",
        color: window.Config.skinFile.game.text_color,
      });
      this.pool.add(text);

      const sections = window.Dungeon.goals.split(/(:[\w]+)/g).filter((x) => x);
      let currentWidth = text.width;
      let resolution = 15;
      sections.forEach((section) => {
        if (section.startsWith(":")) {
          // is image lookup
          const img = this.add.image(
            this.renderer.width * 0.5 + currentWidth + resolution / 2,
            resolution / 2,
            section.substring(1) +
              "-" +
              getFromTileSet(
                window.Config.skinFile.entities[section.substring(1)]
              )
          );
          img.setDisplaySize(resolution, resolution);
          currentWidth += img.displayWidth + resolution / 8;
          this.pool.add(img);
        } else {
          let text = this.add.text(
            this.renderer.width * 0.5 + currentWidth,
            0,
            section.trim(),
            {
              fontFamily: "game-font",
              fontSize: resolution + "px",
              color: window.Config.skinFile.game.text_color,
            }
          );
          currentWidth += text.width + resolution / 8;
          this.pool.add(text);
        }
      });
    } else if (this.hasSeenGoal) {
      // we've won the game!
      InfoAlert("Congratulations!", "You've won the dungeon!", () => {
        (window as any).MainScene = null;
        this.scene.stop("scene-game");
        this.scene.start("scene-menu");
      });
    }


    if (window.Dungeon.inventory.filter(x => x.type.toLowerCase() == "time_turner").length > 0) {
      let rewind1TicksFrame = new TextButton(
          this,
          10,
          this.renderer.height - 100,
          "Rewind 1 Tick",
          {
            fontSize: "15px",
            fontFamily: "game-font",
          },
          async () => {
            window.Dungeon = await API.rewind(1);
            this.updateScene();
            (window as any).MainScene.menuScene && (window as any).MainScene.menuScene.updateScene();
          }
      );
      this.pool.add(rewind1TicksFrame);
      let rewind5TicksFrame = new TextButton(
          this,
          10,
          this.renderer.height - 75,
          "Rewind 5 Ticks",
          {
            fontSize: "15px",
            fontFamily: "game-font",
          },
          async () => {
            window.Dungeon = await API.rewind(5);
            this.updateScene();
            (window as any).MainScene.menuScene && (window as any).MainScene.menuScene.updateScene();
          }
      );
      this.pool.add(rewind5TicksFrame);
      this.add.existing(rewind1TicksFrame);
      this.add.existing(rewind5TicksFrame);
      this.add.existing(rewind5TicksFrame);
    }

    if (window.Dungeon.inventory.length > 0) {
      let text = this.add.text(
        this.renderer.width * 0.1 + this.exitButton.width,
        0,
        window.Config.localisation.game.inventory,
        {
          fontFamily: "game-font",
          fontSize: "25px",
          color: window.Config.skinFile.game.text_color,
        }
      );
      text.x -= text.width / 4;
      this.pool.add(text);
    }

    let currentWidth = this.renderer.width * 0.1 + this.exitButton.width;
    // grab count of each inventory item
    var inventoryDeduplicated = window.Dungeon.inventory.reduce(
      (xs, x) => ((xs[x.type] ? xs[x.type].push(x) : xs[x.type] = [x]), xs),
      {} as { [key: string]: ItemResponse[] });

    for (var items in inventoryDeduplicated) {
      const inventoryItems = inventoryDeduplicated[items];
      const id = inventoryItems[0].id;

      const inventoryFrame = new Clickable(
        this,
        currentWidth,
        45,
        "item",
        async () => {
          window.Dungeon = await API.tickByItem(id);
          this.updateScene();
          (window as any).MainScene.menuScene && (window as any).MainScene.menuScene.updateScene();
        }
      );

      this.add.existing(inventoryFrame);
      const item = this.add.image(
        currentWidth,
        45,
        inventoryItems[0].type +
          "-" +
          getFromTileSet(window.Config.skinFile.entities[inventoryItems[0].type])
      );
      item.setDisplaySize(16 * 1.5, 16 * 1.5);
      item.texture.setFilter(Phaser.Textures.FilterMode.NEAREST);

      inventoryFrame.setDisplaySize(16 * 4, 16 * 4);
      item.texture.setFilter(Phaser.Textures.FilterMode.NEAREST);

      const count = this.add.text(currentWidth + 16 + 5, 45 + 16, String(inventoryItems.length),
        {
          fontFamily: "game-font",
          fontSize: "15px",
          color: "white",
        });
      count.depth = 99;
      this.pool.add(count);

      this.pool.add(inventoryFrame);
      this.pool.add(item);

      currentWidth += 16 * 3;
    }

    if (window.Dungeon.buildables.length > 0) {
      let text = this.add.text(
        this.renderer.width - 15,
        10,
        window.Config.localisation.game.build,
        {
          fontFamily: "game-font",
          fontSize: "25px",
          color: window.Config.skinFile.game.text_color,
        }
      );
      text.x -= text.width;
      this.pool.add(text);
    }

    let currentHeight = this.renderer.height * 0.1;
    for (var buildableItem of window.Dungeon.buildables) {
      const itemSaved = buildableItem;

      const buildableFrame = new Clickable(
        this,
        this.renderer.width - 40,
        currentHeight,
        "item",
        async () => {
          window.Dungeon = await API.build(itemSaved);
          this.updateScene();
          (window as any).MainScene.menuScene && (window as any).MainScene.menuScene.updateScene();
        }
      );
      this.add.existing(buildableFrame);
      const item = this.add.image(
        this.renderer.width - 40,
        currentHeight,
        buildableItem +
          "-" +
          getFromTileSet(window.Config.skinFile.entities[buildableItem])
      );
      item.setDisplaySize(16 * 1.5, 16 * 1.5);
      item.texture.setFilter(Phaser.Textures.FilterMode.NEAREST);

      buildableFrame.setDisplaySize(16 * 4, 16 * 4);
      item.texture.setFilter(Phaser.Textures.FilterMode.NEAREST);

      this.pool.add(item);
      this.pool.add(buildableFrame);

      currentHeight += 16 * 4.5;
    }
  }
}

export default UIScene;
