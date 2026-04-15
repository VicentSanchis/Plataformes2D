import processing.core.*;

public class Particle {
  protected PVector pos;
  protected PVector vel;
  protected float   life;
  protected float   maxLife;

  public Particle (float x, float y, float vx, float vy, float life) {
    pos = new PVector(x, y);
    vel = new PVector(vx, vy);
    this.life    = life;
    this.maxLife = life;
  }

  public void update() {
    pos.add(vel);
    vel.y += 0.15f; // gravetat suau
    life --;
  }

  public void display(PApplet app) {
    float alpha = PApplet.map(life, 0, maxLife, 0, 255);
    app.fill(255, 127, 0, alpha);
    app.noStroke();
    app.circle(pos.x, pos.y, 4);
  }

  public boolean isDead() {
    return life <= 0;
  }
}
