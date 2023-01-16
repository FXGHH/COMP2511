import { Game, GameObjects, Scene, Tweens } from "phaser";

import { TextButton } from "../utils/TextButton";

import axios from "axios";
import Swal from "sweetalert2";
import { ErrorAlert, ListAlert } from "../utils/Alerts";
import { Animation, Dungeon } from "../types/Types";
import { API, detectWebGL } from "../utils/Api";
import { Clickable } from "../utils/Clickable";
import { getFromTileSet, TileSet } from "../utils/Tileset";
import {
  playBackgroundMusic,
  setupBackgroundMusic,
} from "../utils/MusicManager";
import { generateSceneWideSymbols } from "../utils/SceneWideSymbols";
import GlowFilterPostFx from "phaser3-rex-plugins/plugins/glowfilterpipeline.js";

class MainScene extends Scene {
  private pool: Phaser.GameObjects.Group;
  private healthbars: Phaser.GameObjects.Group;
  private movementCap = 500;
  private lastMovement: number = 0;
  private animations: Tweens.Tween[] = [];
  private postFxEntities: any[] = [];
  private lastInteraction: number = 0;
  private cursors: {
    up: Phaser.Input.Keyboard.Key;
    down: Phaser.Input.Keyboard.Key;
    left: Phaser.Input.Keyboard.Key;
    right: Phaser.Input.Keyboard.Key;
  };

  constructor() {
    super("scene-game");
  }

  preload() {
    //  enable if local
    this.load.baseURL = "/";
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
    this.scene.launch("scene-ui");

    playBackgroundMusic(
      "game-",
      this,
      window.Config.skinFile.game.background_music
    );

    // then to create the background we just generate per tile
    const widthGenerate = this.renderer.width;
    const widthOffset = 80;
    const heightGenerate = this.renderer.height;
    const heightOffset = 75;

    if (!window.Dungeon) {
      ErrorAlert(
        "Invalid Dungeon",
        "Dungeon not loaded... going back to main menu"
      );
      this.scene.start("scene-menu");
    }
    this.pool = this.add.group();
    this.healthbars = this.add.group();

    for (let x = -50; x < 50; x++) {
      for (let y = -50; y < 50; y++) {
        const tex = this.add.image(
          x * window.Config.skinFile.game.resolution_px,
          y * window.Config.skinFile.game.resolution_px,
          "background-" +
            getFromTileSet(window.Config.skinFile.game.background_image)
        );
        let entityResolution = [
          window.Config.skinFile.game.resolution_px,
          window.Config.skinFile.game.resolution_px,
        ];
        if (
          (window.Config.skinFile.game.background_image as TileSet) &&
          (window.Config.skinFile.game.background_image as TileSet).$resolution
        ) {
          entityResolution = (
            window.Config.skinFile.game.background_image as TileSet
          ).$resolution;
        }

        tex.setDisplaySize(entityResolution[0], entityResolution[1]);
        tex.texture.setFilter(Phaser.Textures.FilterMode.NEAREST);
      }
    }

    // next we need to actually generate the initial scene
    this.updateScene();

    const keys = new Set();
    this.input.keyboard.on("keydown", (event) => {
      if (!keys.has(event.code)) {
        keys.add(event.code);
        this.input.keyboard.emit(`keypress_${event.code}`);
      }
    });

    this.input.keyboard.on("keyup", (event) => {
      keys.delete(event.code);
      this.lastMovement = this.movementCap;
      this.input.keyboard.emit(`keyrelease_${event.code}`);
    });

    this.cursors = this.input.keyboard.addKeys(
      {
        up: Phaser.Input.Keyboard.KeyCodes.W,
        down: Phaser.Input.Keyboard.KeyCodes.S,
        left: Phaser.Input.Keyboard.KeyCodes.A,
        right: Phaser.Input.Keyboard.KeyCodes.D,
      },
      true,
      false
    ) as any;
  }

  updateScene() {
    if ((window as any).MainScene) {
      (window as any).MainScene.updateScene();
      (window as any).MainScene.menuScene = this;
    }

    const allIds = window.Dungeon.entities.map((x) => x.id);
    const uniqueIds = new Set(allIds);
    let entities = {};

    if (allIds.length != uniqueIds.size) {
      // find conflicting ids
      const incorrectIds = allIds
        .filter((x, i) => allIds.indexOf(x) != i)
        .map((x, i) => window.Dungeon.entities[i]);

      ListAlert(
        "Conflicting Ids",
        "We have ${incorrectIds.length} many conflicting ids since they are duplicates.\nThe (latter) IDs belong to the entities",
        incorrectIds.map(
          (x) =>
            `${x.id}: { (${x.position.x}, ${x.position.y}, ${x.position.layer}), ${x.type} }`
        )
      );
    }

    this.pool.maxSize = -1;
    this.pool.getChildren().forEach((x) => {
      x.setActive(false);
      (x as GameObjects.Image).setVisible(false);
      this.pool.killAndHide(x);
    });

    this.animations.forEach((x) => {
      x.stop();
      x.remove();
    });
    this.animations = [];

    const resolution_px = window.Config.skinFile.game.resolution_px;
    const offset = 75;

    var postFxPlugin =
      detectWebGL() && (this.plugins.get("rexGlowFilterPipeline") as any);
    this.postFxEntities.forEach((x) => postFxPlugin.remove(x));
    this.postFxEntities = [];

    for (var entity of window.Dungeon.entities) {
      const key =
        entity.type +
        "-" +
        getFromTileSet(window.Config.skinFile.entities[entity.type]);
      const entityImg = this.pool.get(
        entity.position.x * resolution_px,
        entity.position.y * resolution_px + offset,
        key
      ) as GameObjects.Image;
      entities[entity.id] = entityImg;
      entityImg.setTexture(key);
      entityImg.setActive(true);
      entityImg.alpha = 1;
      entityImg.setVisible(true);
      entityImg.setDepth(entity.position.layer);
      let entityResolution = [resolution_px, resolution_px];
      if (
        (window.Config.skinFile.entities[entity.type] as TileSet) &&
        (window.Config.skinFile.entities[entity.type] as TileSet).$resolution
      ) {
        entityResolution = (
          window.Config.skinFile.entities[entity.type] as TileSet
        ).$resolution;
      }

      entityImg.setDisplaySize(entityResolution[0], entityResolution[1]);
      entityImg.texture.setFilter(Phaser.Textures.FilterMode.NEAREST);

      const entityId = entity.id;

      if (entity.isInteractable) {
        // create a copy
        entityImg.setInteractive().off("pointerup"); // reset interactive before binding it to prevent multiple triggering
        entityImg.setInteractive().on("pointerup", async () => {
          if (Date.now() - this.lastInteraction >= 1000) {
            this.lastInteraction = Date.now(); // log it before the request to prevent triggering too fast
            window.Dungeon = await API.interact(entityId);
            this.updateScene();
          }
        });

        if (postFxPlugin) {
          var pipeline = postFxPlugin.add(entityImg);
          this.postFxEntities.push(entityImg);
          this.animations.push(
            this.tweens.add({
              targets: pipeline,
              intensity: 0.06,
              ease: "Linear",
              repeat: -1,
              yoyo: true,
            })
          );
        } else {
          this.animations.push(
            this.tweens.add({
              targets: [entityImg],
              scale: "+=0.1",
              scaleX: "+=0.1",
              scaleY: "+=0.1",
              ease: "Linear",
              repeat: -1,
              yoyo: true,
            })
          );
        }
      } else {
        entityImg.removeInteractive();
      }

      if (entity.type.toLowerCase() == "player") {
        this.cameras.main.setScroll(
          entity.position.x * resolution_px - this.renderer.width / 2,
          entity.position.y * resolution_px + offset - this.renderer.height / 2
        );
      }
    }

    this.parseAnimations(window.Dungeon.animations, entities);
  }

  move(direction: "Down" | "Left" | "Right" | "Up") {
    if (this.lastMovement >= this.movementCap) {
      this.lastMovement = 0;
    } else {
      return;
    }

    API.tickByMovement(direction).then((resp) => {
      window.Dungeon = resp;
      this.updateScene();
    });
  }

  addAnim(
    anim: Animation,
    entities: { [entityId: string]: GameObjects.Image },
    healthBars: any
  ) {
    let animations = [];

    if (entities && entities[anim.entityId]) {
      const entityImg = entities[anim.entityId];
      // add a series of tweens
      let addedNewOne = false;

      for (const frame of anim.queue) {
        const actions = frame.split(",").map((x) => x.trim());
        const type = actions[0];
        // in the type there are multiple subtypes
        let innerTypes = type.split(" ");
        if (innerTypes[0] == "healthbar") {
          innerTypes.shift();
          innerTypes = innerTypes; // for stupid typescript

          let rectImg: GameObjects.Rectangle;
          let outerImg: GameObjects.Rectangle;

          if (!healthBars[anim.entityId]) {
            rectImg = this.add.rectangle(
              entityImg.x,
              entityImg.y - entityImg.displayHeight / 2 - 2.5,
              entityImg.displayWidth,
              2.5,
              0,
              1
            );
            outerImg = this.add.rectangle(
              entityImg.x,
              entityImg.y - entityImg.displayHeight / 2 - 2.5,
              entityImg.displayWidth,
              2.5,
              0,
              1
            );
            this.healthbars.add(entityImg);
            rectImg.setOrigin(0, 0.5);
            rectImg.x -= rectImg.displayWidth / 2;
            outerImg.setOrigin(0, 0.5);
            outerImg.x -= outerImg.displayWidth / 2;

            this.healthbars.add(outerImg);
            outerImg.setDepth(9999);
            rectImg.setDepth(10000);
            healthBars[anim.entityId] = rectImg;
            healthBars[anim.entityId + "$$$$$--OUTER--$$$$$"] = outerImg;
          } else {
            rectImg = healthBars[anim.entityId];
            outerImg = healthBars[anim.entityId + "$$$$$--OUTER--$$$$$"];
          }

          // anim props
          let over = -1;
          let ease = "Linear";
          let wait = -1;

          for (let i = 1; i < actions.length; i++) {
            const actionTypes = actions[i].split(" ");
            // trim out the 's'
            if (actionTypes[0] == "over") {
              over = Number(
                actionTypes[1].substring(0, actionTypes[1].length - 1)
              );
            } else if (actionTypes[0] == "wait") {
              wait = Number(
                actionTypes[1].substring(0, actionTypes[1].length - 1)
              );
            } else if (actionTypes[0] == "ease") {
              ease = actionTypes[1];
            } else {
              // ignore
            }
          }

          switch (innerTypes[0]) {
            case "set": {
              animations.push(
                this.tweens.add({
                  targets: [rectImg],
                  width:
                    "+=" +
                    (Number(innerTypes[1]) * entityImg.width - entityImg.width),
                  ease,
                  duration: (over >= 0 ? over : 0) * 1000,
                  delay: (wait >= 0 ? wait : 0) * 1000,
                  repeat: 0,
                  yoyo: false,
                })
              );
              addedNewOne = true;
              break;
            }
            case "tint": {
              if (over == -1 && wait == -1) {
                rectImg.fillColor = Number(innerTypes[1]);
              } else {
                var oldColor = rectImg.fillColor;
                var newColor = Number(innerTypes[1]);

                animations.push(
                  this.tweens.addCounter({
                    ease,
                    duration: (over >= 0 ? over : 0) * 1000,
                    delay: (wait >= 0 ? wait : 0) * 1000,
                    repeat: 0,
                    yoyo: false,
                    from: 0,
                    to: 100,
                    onUpdate: (tween) => {
                      const color =
                        Phaser.Display.Color.Interpolate.ColorWithColor(
                          Phaser.Display.Color.IntegerToColor(oldColor),
                          Phaser.Display.Color.IntegerToColor(newColor),
                          100,
                          tween.getValue()
                        );
                      rectImg.fillColor =
                        ((color.r & 0x0ff) << 16) |
                        ((color.g & 0x0ff) << 8) |
                        (color.b & 0x0ff);
                    },
                  })
                );
              }
              addedNewOne = true;
              break;
            }
            case "scale": {
              animations.push(
                this.tweens.add({
                  targets: [rectImg, outerImg],
                  ease,
                  scale: "+=" + (Number(innerTypes[1]) - rectImg.scale),
                  duration: (over >= 0 ? over : 0) * 1000,
                  delay: (wait >= 0 ? wait : 0) * 1000,
                  repeat: 0,
                  yoyo: true,
                })
              );
              addedNewOne = true;
              break;
            }
            case "shake": {
              animations.push(
                this.tweens.add({
                  targets: [rectImg, outerImg],
                  ease,
                  rotation: "+=" + Math.PI / 128,
                  scale: "+=0.5",
                  duration: (over >= 0 ? over : 0) * 1000,
                  delay: (wait >= 0 ? wait : 0) * 1000,
                  repeat: 0,
                  yoyo: true,
                })
              );
              addedNewOne = true;
              break;
            }
          }
        } else {
          // anim props
          let over = -1;
          let ease = "Linear";
          let wait = -1;

          for (let i = 1; i < actions.length; i++) {
            const actionTypes = actions[i].split(" ");
            // trim out the 's'
            if (actionTypes[0] == "over") {
              over = Number(
                actionTypes[1].substring(0, actionTypes[1].length - 1)
              );
            } else if (actionTypes[0] == "wait") {
              wait = Number(
                actionTypes[1].substring(0, actionTypes[1].length - 1)
              );
            } else if (actionTypes[0] == "ease") {
              ease = actionTypes[1];
            } else {
              // ignore
            }
          }

          switch (innerTypes[0]) {
            case "sprite": {
              setTimeout(
                () => {
                  const key =
                    innerTypes[1] +
                    "-" +
                    getFromTileSet(
                      window.Config.skinFile.entities[innerTypes[1]]
                    );
                  entityImg.setTexture(key);
                  entityImg.setDisplaySize(window.Config.skinFile.game.resolution_px, window.Config.skinFile.game.resolution_px);
                },
                wait >= 0 ? wait : 0
              );
              break;
            }
            case "tint": {
              if (over == -1 && wait == -1) {
                entityImg.tint = Number(innerTypes[1]);
              } else {
                var oldColor = entityImg.tint;
                var newColor = Number(innerTypes[1]);

                animations.push(
                  this.tweens.addCounter({
                    ease,
                    duration: (over >= 0 ? over : 0) * 1000,
                    delay: (wait >= 0 ? wait : 0) * 1000,
                    repeat: 0,
                    yoyo: false,
                    from: 0,
                    to: 100,
                    onUpdate: (tween) => {
                      const color =
                        Phaser.Display.Color.Interpolate.ColorWithColor(
                          Phaser.Display.Color.IntegerToColor(oldColor),
                          Phaser.Display.Color.IntegerToColor(newColor),
                          100,
                          tween.getValue()
                        );
                      entityImg.tint =
                        ((color.r & 0x0ff) << 16) |
                        ((color.g & 0x0ff) << 8) |
                        (color.b & 0x0ff);
                    },
                  })
                );
              }
              addedNewOne = true;
              break;
            }
            case "scale-x": {
              animations.push(
                this.tweens.add({
                  targets: [entityImg],
                  scaleX: "+=" + (Number(innerTypes[1]) - entityImg.scaleX),
                  ease,
                  duration: (over >= 0 ? over : 0) * 1000,
                  delay: (wait >= 0 ? wait : 0) * 1000,
                  repeat: 0,
                  yoyo: false,
                })
              );
              addedNewOne = true;
              break;
            }
            case "scale-y": {
              animations.push(
                this.tweens.add({
                  targets: [entityImg],
                  scaleY: "+=" + (Number(innerTypes[1]) - entityImg.scaleY),
                  ease,
                  duration: (over >= 0 ? over : 0) * 1000,
                  delay: (wait >= 0 ? wait : 0) * 1000,
                  repeat: 0,
                  yoyo: false,
                })
              );
              addedNewOne = true;
              break;
            }
            case "scale-y": {
              animations.push(
                this.tweens.add({
                  targets: [entityImg],
                  scale: "+=" + (Number(innerTypes[1]) - entityImg.scale),
                  ease,
                  duration: (over >= 0 ? over : 0) * 1000,
                  delay: (wait >= 0 ? wait : 0) * 1000,
                  repeat: 0,
                  yoyo: false,
                })
              );
              addedNewOne = true;
              break;
            }
            case "translate-x": {
              const targets = [entityImg];
              const rectImg = healthBars[anim.entityId];
              const outerImg = healthBars[anim.entityId + "$$$$$--OUTER--$$$$$"];
              if (rectImg) targets.push(rectImg, outerImg);

              animations.push(
                this.tweens.add({
                  targets: targets,
                  x: "+=" + Number(innerTypes[1]) * entityImg.displayWidth,
                  ease,
                  duration: (over >= 0 ? over : 0) * 1000,
                  delay: (wait >= 0 ? wait : 0) * 1000,
                  repeat: 0,
                  yoyo: false,
                })
              );
              addedNewOne = true;
              break;
            }
            case "translate-y": {
              const targets = [entityImg];
              const rectImg = healthBars[anim.entityId];
              const outerImg = healthBars[anim.entityId + "$$$$$--OUTER--$$$$$"];
              if (rectImg) targets.push(rectImg, outerImg);

              animations.push(
                this.tweens.add({
                  targets: targets,
                  y: "+=" + Number(innerTypes[1]) * entityImg.displayHeight,
                  ease,
                  duration: (over >= 0 ? over : 0) * 1000,
                  delay: (wait >= 0 ? wait : 0) * 1000,
                  repeat: 0,
                  yoyo: false,
                })
              );
              addedNewOne = true;
              break;
            }
            case "set-layer": {
              animations.push(
                this.tweens.add({
                  targets: [entityImg],
                  depth: "+=" + (Number(innerTypes[1]) - entityImg.depth),
                  ease,
                  duration: (over >= 0 ? over : 0) * 1000,
                  delay: (wait >= 0 ? wait : 0) * 1000,
                  repeat: 0,
                  yoyo: false,
                })
              );
              addedNewOne = true;
              break;
            }
          }
        }
      }

      this.animations.push(...animations);

      setTimeout(() => {
        if (anim.queue.length > 0 && addedNewOne && anim.loop) {
          const time = animations.reduce(
            (acc, cur) => cur.totalDuration + acc,
            0
          );
          const action = () => {
            animations.forEach((x) => x.restart());
            setTimeout(action, time);
          };

          setTimeout(action, time);
        }

        if (anim.queue.length > 0 && addedNewOne && anim.duration >= 0) {
          setTimeout(() => {
            animations.forEach((x) => {
              x.seek(1);
            });
            animations = [];
          }, anim.duration * 1000);
        }
      }, 100);
    }
  }

  parseAnimations(
    anims: Animation[],
    entities: { [entityId: string]: GameObjects.Image }
  ) {
    this.healthbars.clear(true, true);
    const healthBars: any = {};

    if (!anims) return;

    for (const anim of anims) {
      this.addAnim(anim, entities, healthBars);
    }
  }

  update(time: number, delta: number) {
    this.lastMovement += delta;

    if (this.cursors.down.isDown) {
      this.move("Down");
    } else if (this.cursors.left.isDown) {
      this.move("Left");
    } else if (this.cursors.up.isDown) {
      this.move("Up");
    } else if (this.cursors.right.isDown) {
      this.move("Right");
    }
  }
}

export default MainScene;
