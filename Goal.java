import processing.core.PVector;
import processing.core.PApplet;

public class Goal extends Entity {
  protected PApplet app;
  protected float   amplada;
  protected float   altura;

  public Goal (PApplet app, float x, float y) {
    this.app = app;
    posicio  = new PVector(x, y);
    amplada  = 50;
    altura   = 50;
}

  public void update(TileMap map) {
    // de moment no fa res
  }
  public void display() {
    float t = app.frameCount * 0.15f;

    // ================= PAL =================
    app.stroke(90);
    app.rect(posicio.x, posicio.y-altura, 3, altura);

    // ================= BANDERA =================
    float wave = PApplet.sin(t) * 4;
    app.fill(255,0,0);
    app.quad(posicio.x, posicio.y-altura, posicio.x+30, posicio.y-altura+wave, posicio.x+30, posicio.y-altura+24+wave, posicio.x, posicio.y-altura+24);
  }
  
  public float getRadi   () { return this.amplada/2; }
  public float getX      () { return this.posicio.x; }
  public float getY      () { return this.posicio.y; }
  public float getWidth  () { return this.amplada;   }
  public float getHeight () { return this.altura;    }
}
