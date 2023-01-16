import {Scene} from 'phaser';

class BootScene extends Scene {
  constructor() {
    super("scene-boot");
  }
  
  preload() {
    // Load any assets here from your assets directory
    // this.load.image('star', 'assets/star.png');
  }

  create() {
    this.scene.start('scene-menu');
  }
}

export default BootScene;