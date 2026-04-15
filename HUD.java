import processing.core.PApplet;

public class HUD {

  protected PApplet app;

  public HUD(PApplet app) {
    this.app = app;
  }
 
  public void display(Nivell nivell, Boleta player, int score, int nivellActual) {

    app.pushMatrix();
    app.resetMatrix();
    
    float progress = player.getX() / nivell.getAmpladaNivell();
    
    app.noFill();
    app.stroke(90);
    app.rectMode(PApplet.CORNER);
    app.rect(app.width/2-125, 20, 250, 15);

    app.noStroke();
    app.fill(0,150,0);
    app.rect(app.width/2-124, 20, 250 * progress, 15);

    app.fill(255,100,50,200);
    app.textSize(24);
    app.textAlign(PApplet.LEFT);

    app.text("Punts: " + score, 20, 30);
    
    app.noStroke();
    for (int i = 0; i < player.getVides(); i++) {
      float x = 35 + i * 30;
      float y = 45;

      app.fill(255,0,0);
      app.circle(x, y, 12);
      app.circle(x+10, y, 12);
      app.triangle(x-6, y+2, x+16, y+2, x+5, y+18);
    }
    
    app.fill(255,100,50,200);
    app.text("Nivell " + nivellActual, app.width - 120, 30);
    
    app.popMatrix();
  }
}
