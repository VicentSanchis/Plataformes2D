import processing.core.PApplet;
import processing.core.PFont;
import processing.data.*;
import java.util.*;

public class TileMap {
  protected final static int AIR            = 0;
  protected final static int SOLID          = 1;
  protected final static int ONEWAY         = 2;
  protected final static int COIN           = 3; // (*) Tile extraible es converteix a AIR 
  protected final static int ENEMY          = 4; // (*) Tile extraible es converteix a AIR
  protected final static int GOAL           = 5; // (*) Tile extraible es converteix a AIR
  protected final static int QUESTION_BLOCK = 6; // (*) Tile extraible es converteix a AIR
  protected final static int CAVE           = 7;
  
  
  protected int      numero;
  protected int [][] tiles;
  protected int      tileSize = 50;
  protected int      cameraMinYTile;
  protected int      cameraMaxYTile;
  protected String   file;
  protected Nivell   nivell;
  protected Goal     goal;
  protected PApplet  app;

  public TileMap(Nivell n, PApplet app, String file) {
    this.nivell = n;
    this.app    = app;
    loadTiles (file);
  }
  
  public void loadTiles(String file) {
    this.file = file;
    JSONObject json     = app.loadJSONObject (file           );
    JSONObject bounds   = json.getJSONObject ("cameraBounds" );
    this.cameraMinYTile = bounds.getInt      ("minY"         );
    this.cameraMaxYTile = bounds.getInt      ("maxY"         );
    JSONArray rows      = json.getJSONArray  ("tiles"        );
    numero              = json.getInt        ("numero"       );

    int h = rows.size();
    int w = rows.getJSONArray(0).size();

    tiles = new int[h][w];

    for (int y = 0; y < h; y++) {
      JSONArray row = rows.getJSONArray(y);
      for (int x = 0; x < w; x++) {
        tiles[y][x] = row.getInt(x);
      }
    }
  }

  public void display() {
    boolean teAireDamunt, teAireEsquerra, teAireDreta, teAireBaix, teSolidDalt, teCovaDalt;
    for (int y = 0; y < tiles.length; y++) {
      for (int x = 0; x < tiles[y].length; x++) {
        
        int tileType   = tiles[y][x];
        teAireDamunt   = teAireDamunt   (x,y);
        teAireDreta    = teAireDreta    (x,y);
        teAireEsquerra = teAireEsquerra (x,y);
        teAireBaix     = teAireBaix     (x,y);
        teCovaDalt     = teCovaDalt     (x,y);
        teSolidDalt    = teSolidDalt    (x,y);
              
        switch (tileType) {
          case SOLID: //<>//
            if (teCovaDalt) {
              app.image(Assets.TILE_CAVE_FLOOR, x*tileSize, y*tileSize, tileSize, tileSize);
              continue;
            }
            
            if (teAireDamunt){
              if (teAireDreta && teAireEsquerra) { 
                app.image(Assets.TILE_GRASS_BOTH,  x*tileSize, y*tileSize, tileSize, tileSize);
                continue;
              }
              if (teAireDreta) { 
                app.image(Assets.TILE_GRASS_RIGHT, x*tileSize, y*tileSize, tileSize, tileSize);
                continue;
              }
              
              if (teAireEsquerra) { 
                app.image(Assets.TILE_GRASS_LEFT, x*tileSize, y*tileSize, tileSize, tileSize);
                continue;
              }
              
              app.image(Assets.TILE_GRASS_CENTER, x*tileSize, y*tileSize, tileSize, tileSize);
            }
            else
              app.image(Assets.TILE_GROUND, x*tileSize, y*tileSize, tileSize, tileSize);
          break;
          
          case CAVE:
            
            if (teSolidDalt)
              app.image(Assets.TILE_CAVE_ROOF,  x*tileSize, y*tileSize, tileSize, tileSize);
              
            else
              app.image(Assets.TILE_CAVE,  x*tileSize, y*tileSize, tileSize, tileSize);
          break;
          
          case ONEWAY:
            if (teAireDreta && teAireEsquerra)
              app.image(Assets.TILE_ONEWAY_BOTH, x*tileSize, y*tileSize, tileSize, tileSize/2);
              
            else if (teAireDreta) 
                app.image(Assets.TILE_ONEWAY_RIGHT, x*tileSize, y*tileSize, tileSize, tileSize);
              
            else if (teAireEsquerra) 
              app.image(Assets.TILE_ONEWAY_LEFT, x*tileSize, y*tileSize, tileSize, tileSize);
              
            else 
              app.image(Assets.TILE_ONEWAY_CENTER, x*tileSize, y*tileSize, tileSize, tileSize);
          break;
        }
        
        //app.strokeWeight(1);
        //app.stroke(255);
        //app.noFill();
        //app.rect(x*tileSize, y*tileSize, tileSize, tileSize);
        //app.noStroke();
      }
    }
  }
  
  public boolean esAire (int tx, int ty) {
    return tiles[ty][tx] == AIR;
  }
  
  public boolean esSolid(int tx, int ty) {
    if (tx < 0 || tx >= tiles[0].length)
        return true;

    if (ty < 0)
        return false;   // cel infinit

    if (ty >= tiles.length)
        return true;    // abisme inferior sòlid

    return (tiles[ty][tx] == SOLID);
}

public boolean esQuestionBlock(int tx, int ty) {
  if (tx < 0 || tx >= tiles[0].length)
        return true;

    if (ty < 0)
        return false;   // cel infinit

    if (ty >= tiles.length)
        return true;    // abisme inferior sòlid

    return (tiles[ty][tx] == QUESTION_BLOCK);
}
  
  public boolean esOneWay(int tx, int ty) {

    if (tx < 0 || tx >= tiles[0].length)
        return false;

    if (ty < 0 || ty >= tiles.length)
        return false;

    return tiles[ty][tx] == ONEWAY;
}
  
  public boolean esSolidLateral (int tx, int ty) {
    if (tx < 0 || tx >= tiles[0].length)
        return true;

    if (ty < 0 || ty >= tiles.length)
        return false;

    return (tiles[ty][tx] == SOLID);
  }
  
  public boolean teSolidDalt (int tx, int ty) {
    if (tx < 0 || tx >= tiles[0].length)
        return true;

    if (ty < 0 || ty >= tiles.length)
        return false;

    return esSolid(tx, ty-1);
  }
  
  public boolean teCovaDalt (int tx, int ty) {
      
    if (tx < 0 || tx >= tiles[0].length)
        return true;

    if (ty-1 < 0 || ty >= tiles.length)
        return false;

    return tiles[ty-1][tx] == CAVE;
  }
  
  private boolean teAireDamunt(int x, int y) {
    return !esSolid(x, y - 1);
  }
  
  private boolean teAireDreta(int x, int y) {
    return !esSolid(x+1, y);
  }
  
  private boolean teAireEsquerra(int x, int y) {
    return !esSolid(x-1, y);
  }
  
  private boolean teAireBaix(int x, int y) {
    return !esSolid(x, y + 1);
  }
  
  public void extreuMonedes (ArrayList<Coin> coins) {
    for (int y = 0; y < tiles.length; y++) {
      for (int x = 0; x < tiles[y].length; x++) {
        if (tiles[y][x] == COIN) {
          float worldX = x * tileSize + tileSize/2f;
          float worldY = y * tileSize + tileSize/2f;
          coins.add(new Coin(this.app, worldX, worldY));
          tiles[y][x] = 0;
        }
      }
    }
  }
  
  public void extreuEnemics (ArrayList<Enemy> enemics) {
    for (int y = 0; y < tiles.length; y++) {
      for (int x = 0; x < tiles[y].length; x++) {
        if (tiles[y][x] == ENEMY) {
          float worldX = x * tileSize + tileSize/2f;
          float worldY = y * tileSize;
          enemics.add(new Enemy(app,worldX, worldY));
          tiles[y][x] = 0;
        }
      }
    }
  }
  
  public void extreuMeta () {
    for (int y = 0; y < tiles.length; y++) {
      for (int x = 0; x < tiles[y].length; x++) {
        if (tiles[y][x] == GOAL) {
          goal = new Goal(app, x * tileSize + tileSize/2, y * tileSize + tileSize);
          tiles[y][x] = 0;
        }
      }
    }
  }
  
  public void extreuQuestionBlocks(ArrayList<QuestionBlock> blocks) {
    for (int y = 0; y < tiles.length; y++) {
      for (int x = 0; x < tiles[y].length; x++) {
        if (tiles[y][x] == QUESTION_BLOCK) {
          float worldX = x * tileSize;
          float worldY = y * tileSize;
          blocks.add(new QuestionBlock(app, worldX, worldY, this.nivell.getCoins()));
          tiles[y][x] = AIR;
          
        }
      }
    }
  }
  
  // --------------------------------------- GETS ------------------------------------------
  public int     getAmpladaNivell ()               { return this.tiles[0].length*tileSize; }
  public int     getAlsadaNivell  ()               { return this.tiles.length*tileSize;    }
  public int     worldToTile      (float valor)    { return PApplet.floor(valor/tileSize); }
  public Goal    getGoal          ()               { return goal;                          }
  public int     getNumero        ()               { return this.numero;                   }
  public float   getCameraMinY    ()               { return cameraMinYTile * tileSize;     }
  public float   getCameraMaxY    ()               { return cameraMaxYTile * tileSize;     }
  public String  getFilePath      ()               { return this.file;                     }
}
