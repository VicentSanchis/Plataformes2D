import processing.core.PApplet;
import processing.core.PImage;

public class Assets {
  
  // Per l'autotiling
  public static PImage [] TILE_SOLID = new PImage [16];

  // Tiles
  public static PImage TILE_GRASS_LEFT;
  public static PImage TILE_GRASS_RIGHT;
  public static PImage TILE_GRASS_CENTER;
  public static PImage TILE_GRASS_BOTH;
  public static PImage TILE_GROUND;
  public static PImage TILE_ONEWAY_BOTH;
  public static PImage TILE_ONEWAY_CENTER;
  public static PImage TILE_ONEWAY_LEFT;
  public static PImage TILE_ONEWAY_RIGHT;
  public static PImage TILE_CAVE;
  public static PImage TILE_CAVE_ROOF;
  public static PImage TILE_CAVE_ROOF_GRASS;
  public static PImage TILE_CAVE_FLOOR;
  public static PImage TILE_QUESTION;
  public static PImage TILE_USED_BLOCK;

  // Entitats
  public static PImage COIN;
  public static PImage GOAL;

  public static void load(PApplet app) {

    // Tiles
    TILE_GRASS_CENTER    = app.loadImage("./assets/textures/grassCenter.png");
    TILE_GRASS_LEFT      = app.loadImage("./assets/textures/grassLeft.png");
    TILE_GRASS_RIGHT     = app.loadImage("./assets/textures/grassRight.png");
    TILE_GRASS_BOTH      = app.loadImage("./assets/textures/grassBoth.png");
    TILE_GROUND          = app.loadImage("./assets/textures/ground.png");
    TILE_ONEWAY_CENTER   = app.loadImage("./assets/textures/onewayCenter.png");
    TILE_ONEWAY_BOTH     = app.loadImage("./assets/textures/onewayBoth.png");
    TILE_ONEWAY_LEFT     = app.loadImage("./assets/textures/onewayLeft.png");
    TILE_ONEWAY_RIGHT    = app.loadImage("./assets/textures/onewayRight.png");
    TILE_CAVE            = app.loadImage("./assets/textures/cave.png");
    TILE_CAVE_ROOF       = app.loadImage("./assets/textures/caveRoof.png");
    TILE_CAVE_FLOOR      = app.loadImage("./assets/textures/caveFloor.png");
    TILE_CAVE_ROOF_GRASS = app.loadImage("./assets/textures/grassCaveRoof.png");
    TILE_QUESTION        = app.loadImage("./assets/textures/questionBlock.png");
    TILE_USED_BLOCK      = app.loadImage("./assets/textures/questionBlockUsed.png");

    // Entitats
    //COIN = app.loadImage("./assets/textures/coin.png");
    //GOAL = app.loadImage("./assets/textures/goal.png");
  }
}
