import processing.core.PApplet;
import processing.core.PVector;
import processing.core.PFont;

public class ScorePopup {
  protected PApplet app;
  protected PVector posicio;
  protected int     value;
  protected int     timer = 0;
  protected int     maxTime = 120;
  protected PFont   font;

  public ScorePopup(PApplet p, float x, float y, int value) {
    this.app     = p;
    this.posicio = new PVector(x,y);
    this.value   = value;
    this.font    = app.createFont("./assets/fonts/SuperMario64.ttf",24);
  }

  public void update() {
    this.posicio.y -= 1;
    this.timer ++;
  }

  public void display(PApplet app) {
    float alpha = PApplet.map(timer, 0, maxTime, 255, 0);
    app.textFont(font,50);
    app.textAlign(PApplet.CENTER);
    app.fill(210,100,25,alpha);
    app.textSize(40);
    app.text(value, posicio.x, posicio.y);
  }

  public boolean dead() {
    return timer >= maxTime;
  }
}
