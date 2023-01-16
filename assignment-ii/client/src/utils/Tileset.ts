export type TileSet = { [asset: string]: number, $resolution: any, };

export function getFromTileSet(tileset: TileSet | string, ignore: string = null): string {
  if (!tileset || typeof tileset == "string") return "";

  // first count tileset
  const count = Object.entries(tileset).filter(x => x[0] != "$resolution").map(x => x[1]).reduce((prev, cur) => prev + cur, 0);
  let randomChoice = Math.floor(Math.random() * count);
  let lastChoice = null;

  for (const tile in tileset) {
    if (tile == "$resolution") continue;

    lastChoice = tile;
    randomChoice -= tileset[tile];

    if (ignore == tile) continue;
    if (randomChoice <= 0) {
      return String(tile);
    }
  }

  return String(Object.keys(tileset).filter(x => x != "$resolution")[0]);
}
