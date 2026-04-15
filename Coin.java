import java.util.ArrayList;
import processing.core.*;

public class Coin extends Entity {
  protected PApplet app;
  protected int            mida = 25;
  protected float          animacio;
  protected float          yBase;
  protected PVector        posicio;
  protected boolean        collected;
  protected ParticleSystem particles = new ParticleSystem();
  protected ScorePopup     popup;

  public Coin(PApplet p, float x, float y) {
    this.app   = p;
    posicio    = new PVector(x, y);
    this.yBase = y;
  }

  public void display() {
    particles.display(this.app);
    
    if (popup != null)
      popup.display(app);
    
    if (collected) 
      return;

    float amplada = PApplet.abs(PApplet.sin(animacio))*16+2;
    app.fill(255, 220, 0);
    app.stroke(200, 180, 10);
    app.strokeWeight(2);
    app.ellipse(posicio.x, posicio.y, amplada, mida);
    app.ellipse(posicio.x, posicio.y, amplada/1.5f, mida/1.5f);
    app.strokeWeight(1);  // Posar on toca
    
  }
  
  public void update(TileMap tileMap) {
    particles.update();

    if (popup != null)
      popup.update();

    if (collected)
      return;

    animacio += 0.15f;
    posicio.y = yBase + PApplet.sin(animacio * 0.5f) * 3;
  }

  public void collect () {
    for (int i=0; i < 12; i++) {
      float vx = app.random(-3,3);
      float vy = app.random(-4,0);      
      particles.add(new Particle(posicio.x, posicio.y, vx, vy, 30));
    }
    this.collected = true;
    popup = new ScorePopup(this.app, this.posicio.x, this.posicio.y, 100);
  }
  public boolean isCollected() {
    return collected;
  }
  
  public boolean isDead () {
    return collected && particles.isEmpty();
  }
  
  // -------------------------------------- GETS -----------------------------------------
  public float getWidth  () { return this.mida;      }
  public float getHeight () { return this.mida;      }
  public float getRadi   () { return this.mida/2;    }
  public float getX      () { return this.posicio.x; }
  public float getY      () { return this.posicio.y; }
  
  // -------------------------------------- SETS -----------------------------------------
  public void setPosicio (PVector v) { this.posicio = v; } 
}
