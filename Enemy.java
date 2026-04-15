import processing.core.*;

public class Enemy extends Entity{
  protected PApplet        app;
  protected int            deathTimer   = 0;
  protected int            deathMaxTime = 20;
  protected float          speed        =  1.5f;
  protected float          radius       = 15;
  protected float          walkAnim     =  0;
  protected float          scaleX       =  1;
  protected float          scaleY       =  1;
  protected float          angle        =  0;
  protected float          lastVelX     =  0;
  protected boolean        alive        = true;
  protected boolean        dying        = false;
  protected PVector        vel;
  protected ParticleSystem particles    = new ParticleSystem();
  protected ScorePopup     popup;
  
  public Enemy (PApplet app, float x, float y) {
    this.app = app;
    this.posicio = new PVector(x, y);
    this.vel = new PVector(speed, 0);
  }

public void update(TileMap tileMap) {

  particles.update();
  
  if (popup != null)
      popup.update();

  if (!alive && !dying) return;

  if (dying) {
    deathTimer++;
    posicio.y += vel.y;
    vel.y += 0.05f;

    if (deathTimer > deathMaxTime) {
      alive = false;
    }
    return;
  }

  // física normal
  posicio.x += vel.x;
  vel.y += 0.4f;
  posicio.y += vel.y;

  resolveY(tileMap);
  resolveX(tileMap);

  walkAnim += Math.abs(vel.x) * 0.2f;

  boolean changeDirection =
    (vel.x > 0 && lastVelX < 0) ||
    (vel.x < 0 && lastVelX > 0);

  lastVelX = vel.x;

  if (changeDirection) {
    scaleX = 1.3f;
    scaleY = 0.7f;
  }

  scaleX = PApplet.lerp(scaleX, 1, 0.1f);
  scaleY = PApplet.lerp(scaleY, 1, 0.1f);

  float target = vel.x * 0.15f;
  angle = PApplet.lerp(angle, target, 0.1f);
}
  
  public void display() {
    if (popup != null)
      popup.display(app);
    
    if (!alive) 
      return;
      
    float bounce = PApplet.sin(walkAnim) * 1.5f;
      
    app.pushMatrix();
    app.pushStyle();
      app.translate(posicio.x, posicio.y);
      app.scale(scaleX, scaleY);
      app.rotate(angle);
    
      if (dying) {
        app.noStroke();
        app.fill(255,150,150,255-deathTimer*5);
        app.circle(0, 0, radius*2+deathTimer*-0.5f);
      }
      else {
        app.fill(200, 50, 50);
        app.noStroke();
    
        // cos principal
        app.ellipse(0, 0 + bounce, radius * 2, radius * 2);
    
        // direcció (mirada)
        float dir = (vel.x == 0) ? 1 : PApplet.abs(vel.x);
        float eyeOffsetX = radius * 0.4f * dir;
    
        // ulls
        app.fill(255);
        app.ellipse(-radius * 0.3f, -radius * 0.2f, radius * 0.6f, radius * 0.6f);
        app.ellipse(radius * 0.3f, -radius * 0.2f, radius * 0.6f, radius * 0.6f);
    
        // pupil·les
        app.fill(0);
        app.ellipse(-radius * 0.3f + eyeOffsetX * 0.3f, -radius * 0.2f, radius * 0.25f, radius * 0.25f);
        app.ellipse(radius * 0.3f + eyeOffsetX * 0.3f, -radius * 0.2f, radius * 0.25f, radius * 0.25f);
    
        // boca agressiva
        app.stroke(0);
        app.strokeWeight(2);
        app.line(-radius/2 * 0.3f, radius * 0.3f, radius * 0.3f, radius * 0.3f);
      }
    app.popStyle();
    app.popMatrix();
    particles.display(app);
  }
 
  private void resolveY(TileMap tileMap) {
    float left     = posicio.x - radius;
    float right    = posicio.x + radius;
    float top      = posicio.y - radius;
    float bottom   = posicio.y + radius;
    int leftTile   = tileMap.worldToTile(left);
    int rightTile  = tileMap.worldToTile(right);
    int bottomTile = tileMap.worldToTile(bottom);

    for (int tx = leftTile; tx <= rightTile; tx++) {
      if (tileMap.esSolid(tx, bottomTile)) {

        posicio.y = bottomTile * tileMap.tileSize - radius;
        vel.y = 0;
        return;
      }
    }
  }

  private void resolveX(TileMap tileMap) {
    float left     = posicio.x - radius;
    float right    = posicio.x + radius;
    float top      = posicio.y - radius;
    float bottom   = posicio.y + radius;
    int topTile    = tileMap.worldToTile(top);
    int bottomTile = tileMap.worldToTile(bottom);

    if (vel.x > 0) {
      int tileX = tileMap.worldToTile(right + vel.x);

      for (int y = topTile; y < bottomTile; y++) {
        if (tileMap.esSolidLateral(tileX, y)) {
          posicio.x = tileX * tileMap.tileSize - radius - 0.1f;
          vel.x *= -1; 
          return;
        }
      }
    } 
    else if (vel.x < 0) {
      int tileX = tileMap.worldToTile(left + vel.x);

      for (int y = topTile; y < bottomTile; y++) {
        if (tileMap.esSolidLateral(tileX, y)) {
          posicio.x = (tileX + 1) * tileMap.tileSize + radius + 0.1f;
          vel.x *= -1; 
          return;
        }
      }
    }
  }
  
  public boolean estaViu () {
    return this.alive == true;
  }
  
  public boolean estaMorint () {
    return this.dying;
  }
  
  public void kill () {
    dying = true;
    vel.set(0,0);
    
    for (int i=0; i < 12; i++) {
      float vx = app.random(-3,3);
      float vy = app.random(-4,0);      
      particles.add(new Particle(posicio.x, posicio.y, vx, vy, 30)); 
    }
    
    popup = new ScorePopup(this.app, this.posicio.x, this.posicio.y, 50);
  }

  // -------------------------------------- GETS -----------------------------------------
  public float getRadi   () { return this.radius;        }
  public float getHeight () { return this.radius*2;      }
  public float getWidth  () { return this.radius*2;      }
  public float getTop    () { return posicio.y - radius; }
  public float getBottom () { return posicio.y + radius; }
  public float getX      () { return this.posicio.x;     }
  public float getY      () { return this.posicio.y;     }
}
