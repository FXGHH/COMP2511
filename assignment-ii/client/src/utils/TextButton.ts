export class TextButton extends Phaser.GameObjects.Text {
  constructor(scene: Phaser.Scene, x: number, y: number, text: string | string[], style: any, callback: () => void) {
    super(scene, x, y, text, style);
    this.depth = 10;
    this.setFill("#3895D3");

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
  }

  enterButtonHoverState() {
    this.setStyle({ fill: '#ff0'});

    if (this.scene.tweens.getAllTweens()
      .filter(x => x.targets.includes(this))
      .length > 0) {
      return;
    }

    this.scene.tweens.add({
      targets: [this],
      ease: "Linear",
      x: 3,
      duration: 250,
    })
  }

  enterButtonRestState() {
    this.setStyle({ fill: '#3895D3'});
    this.scene.tweens.getAllTweens()
      .filter(x => x.targets.includes(this))
      .forEach(x => this.scene.tweens.remove(x));

    this.scene.tweens.add({
      targets: [this],
      ease: "Linear",
      x: 9,
      duration: 250,
    })
  }

  enterButtonActiveState() {
    this.setStyle({ fill: '#0ff' });
  }
}