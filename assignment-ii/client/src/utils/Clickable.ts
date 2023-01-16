export class Clickable extends Phaser.GameObjects.Image {
  private originalHeight: number = 1;
  private originalWidth: number = 1;

  constructor(scene: Phaser.Scene, x: number, y: number, imgKey: string, callback: () => void) {
    super(scene, x, y, imgKey);

    this
      .setInteractive({ cursor: "pointer" })
      .on('pointerover', () => this.enterButtonHoverState() )
      .on('pointerout', () => this.enterButtonRestState() )
      .on('pointerdown', () => this.enterButtonActiveState() )
      .on('pointerup', (_, _a, _b, ev: Event) => {
        ev.stopPropagation();
        this.enterButtonHoverState();
        callback();
      }, this);

      this.originalWidth = this.displayWidth;
      this.originalHeight = this.displayHeight;
  }

  enterButtonHoverState() {
    if (this.scene.tweens.getAllTweens()
      .filter(x => x.targets.includes(this))
      .length > 0) {
      return;
    }

    this.scene.tweens.add({
      targets: [this],
      ease: "Linear",
      displayWidth: this.originalWidth * 1.25,
      displayHeight: this.originalWidth * 1.25,
      duration: 250,
    })
  }

  enterButtonRestState() {
    this.scene.tweens.getAllTweens()
      .filter(x => x.targets.includes(this))
      .forEach(x => this.scene.tweens.remove(x));

    this.scene.tweens.add({
      targets: [this],
      ease: "Linear",
      displayWidth: this.originalWidth,
      displayHeight: this.originalHeight,
      duration: 250,
    })
  }

  enterButtonActiveState() {
  }
}