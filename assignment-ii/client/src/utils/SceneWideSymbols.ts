import { Scene } from "phaser";
import { TextButton } from "./TextButton";

let muted = false;

export function generateSceneWideSymbols(scene: Scene) {
  const version = scene.add.text(scene.renderer.width - 5, scene.renderer.height - 15, "Version: 2022.09.20-1");
  version.x -= version.width;
  version.y -= version.height;
  version.z = 9999;
  version.setDepth(9999);

  scene.sound.mute = muted;

  const music = new TextButton(scene, 9, scene.renderer.height - 15 - version.height, muted ? "🔇" : "🔉", {}, () => {
    muted = !muted;
    scene.sound.mute = muted;
    if (music.text == "🔉") {
      music.text = "🔇";
    } else {
      music.text = "🔉";
    }
  });
  music.y -= music.height;
  scene.add.existing(music);
}