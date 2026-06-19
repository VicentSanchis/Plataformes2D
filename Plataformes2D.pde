import java.util.ArrayList;

// Variables globals del projecte
int         score   = 0;
float       cameraX = 0;
float       cameraY = 0;

// Objectes globals
GameManager game;

void setup() {
  size(800, 600);
  Assets.load (this);
  game = new GameManager(this);
  PFont fontGeneral = createFont("./assets/fonts/SuperMario64.ttf",24);
  textSize(32);
  textFont(fontGeneral);
}

void draw() {
  background (200, 230, 255);  
  game.update      ();
  dibuixaEscena    ();
  actualitzaCamera ();
}

void dibuixaEscena () { 
  pushMatrix();
    translate    (round(cameraX), round(cameraY));
    game.display (cameraX, cameraY);
  popMatrix();
}

void actualitzaCamera () {

  Nivell n = game.getNivellActual();
  TileMap map = n.getTileMap();

  float cameraObjectiuX = width/2  - n.getPlayerPositionX();
  float cameraObjectiuY = height/2 - n.getPlayerPositionY();
  float ampladaNivell   = n.getAmpladaNivell();
  float limitEsquerre   = 0;
  float limitDret       = -(ampladaNivell - width);
  float limitSuperior   = map.getCameraMinY();
  float limitInferior   = -(map.getCameraMaxY() - height);

  cameraObjectiuX = constrain(cameraObjectiuX, limitDret, limitEsquerre);
  cameraObjectiuY = constrain(cameraObjectiuY, limitInferior, limitSuperior);

  cameraX = lerp(cameraX, cameraObjectiuX, 0.05f);
  cameraY = lerp(cameraY, cameraObjectiuY, 0.05f);
}

void incrementaMarcador (int value) { this.score += value; }

// ------------------------ ESDEVENIMENTS ---------------------------
void keyPressed  () { game.getNivellActual().gestionarTeclat (key, keyCode, true ); }
void keyReleased () { game.getNivellActual().gestionarTeclat (key, keyCode, false); }
