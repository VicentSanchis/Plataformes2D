import processing.core.*;

public class ParallaxLayer {

  protected PApplet app;
  protected float cameraX;
  protected float cameraY;

  public ParallaxLayer(PApplet app) {
    this.app = app;
  }

  public void setCamera(float camX, float camY) {
    this.cameraX = camX;
    this.cameraY = camY;
  }

  public void display() {

    app.pushMatrix();
    app.resetMatrix();

    app.noStroke();

    // ===== Capa 1: cel =====
    app.background(135, 206, 235);

    // ===== Capa 2: muntanyes (molt lluny) =====
    //drawMountains(cameraX * 0.2f);

    // ===== Capa 3: núvols =====
    drawClouds(cameraX * 0.5f, cameraY * 0.2f);

    // ===== Capa 4: arbres (més propers) =====
    //drawTrees(cameraX * 0.8f);

    app.popMatrix();
  }

  void drawMountains(float offset) {
    app.fill(80, 120, 80);

    float baseY = app.height * 0.7f;

    for (int i = -2; i < 10; i++) {
      float parallaxX = cameraX * 0.5f;

      float x = i * 250 + parallaxX;

      app.triangle(
        x, baseY,
        x + 100, baseY - 150,
        x + 200, baseY
      );
    }
  }

  void drawClouds(float offsetX, float offsetY) {
    app.fill(255, 255, 255, 200);

    for (int i = -2; i < 10; i++) {
      //float x = i * 250 - (offsetX % 250);
      float x = i*250 + offsetX*0.15f;
      float y = 80 + (i % 3) * 40 + offsetY;

      app.ellipse(x, y, 60, 40);
      app.ellipse(x + 20, y + 10, 50, 30);
      app.ellipse(x - 20, y + 10, 50, 30);
    }
  }

  void drawTrees(float offset) {
    app.fill(101, 67, 33);

    float groundY = app.height * 0.85f;

    for (int i = -2; i < 20; i++) {
      float x = i * 120 - (offset % 120);

      // tronc
      app.rect(x, groundY - 40, 10, 40);

      // copa
      app.fill(30, 120, 50);
      app.triangle(
        x - 20, groundY - 40,
        x + 5,  groundY - 90,
        x + 30, groundY - 40
      );
    }
  }
}
