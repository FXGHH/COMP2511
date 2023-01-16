import { Scene } from "phaser";
import { getFromTileSet, TileSet } from "./Tileset";

export function setupBackgroundMusic(
  scene: Scene,
  prefix: string,
  music: TileSet | string
) {
  if (typeof music == "string") {
    scene.load.audio(prefix, music);
  } else {
    for (var audio in music) {
      // it's actually pretty cheap to cache the images here because of how tiny they are
      // (this may change in the future if we have perf issues).
      scene.load.audio(prefix + audio, [audio]);
    }
  }
}

let backgroundMusic: Phaser.Sound.BaseSound;

function playSound(
  scene: Scene,
  prefix,
  lastSong = null,
  audio: TileSet | string
) {
  if (!audio) return;
  lastSong = getFromTileSet(audio, lastSong);

  backgroundMusic = scene.sound.add(prefix + lastSong);
  backgroundMusic.once("complete", () => {
    backgroundMusic.destroy();
    playSound(scene, prefix, lastSong, audio);
  });

  scene.events.once("shutdown", () => {
    backgroundMusic.destroy();
  });
}

export function playBackgroundMusic(prefix: string, scene: Scene, music: TileSet | string) {
  playSound(scene, prefix, null, music);
  if (!backgroundMusic) return;

  if (!scene.sound.locked) {
    // already unlocked so play
    backgroundMusic.play();
  } else {
    // wait for 'unlocked' to fire and then play
    scene.sound.once(Phaser.Sound.Events.UNLOCKED, () => {
      backgroundMusic.play();
    });
  }
}
