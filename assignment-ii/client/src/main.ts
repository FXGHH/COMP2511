import "./main.css";
import { Game } from "phaser";
import BootScene from "./scenes/BootScene";
import MainScene from "./scenes/MainScene";
import { API, detectWebGL } from "./utils/Api";
import { Config } from "./types/Module";
import MenuScene from "./scenes/MenuScene";
import UIScene from "./scenes/UIScene";
import GlowFilterPipelinePlugin from "phaser3-rex-plugins/plugins/glowfilterpipeline-plugin.js";

const plugins = detectWebGL() ? {
  global: [
    {
      key: "rexGlowFilterPipeline",
      plugin: GlowFilterPipelinePlugin,
      start: true,
    },
  ],
} : {};

const config = {
  type: Phaser.AUTO,
  width: 640,
  backgroundColor: "#1D1919",
  height: 515,
  plugins: plugins,
  scale: {
    mode: Phaser.Scale.FIT,
    autoCenter: Phaser.Scale.CENTER_HORIZONTALLY,
  },
  scene: [BootScene, MainScene, MenuScene, UIScene],
};

// before we construct the game we should load in configuration globally
(async () => {
  window.Config = {} as Config;

  window.Config.localisationLanguage = await API.getLocalisation();
  window.Config.skinFileName = await API.getSkin();
  window.Config.skinFile = await API.loadSkin(window.Config.skinFileName);
  window.Config.localisation = await API.loadLocalisation(
    window.Config.localisationLanguage
  );
  document.fonts.add(
    await new FontFace(
      "main_menu-font",
      "url(/fonts/" + window.Config.localisation.main_menu.font + ")"
    ).load()
  );
  document.fonts.add(
    await new FontFace(
      "game-font",
      "url(/fonts/" + window.Config.localisation.game.font + ")"
    ).load()
  );
})().then(() => {
  const game = new Game(config);
});
