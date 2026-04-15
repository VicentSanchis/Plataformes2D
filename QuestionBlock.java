import java.util.ArrayList;
import processing.core.*;

public class QuestionBlock extends Entity {

  protected static final int BOUNCE_TIME = 10;

  protected enum Estat { IDLE, HIT, USED }
  protected Estat estat = Estat.IDLE;

  protected PApplet app;

  protected int bounceTimer = 0;
  protected float bounceOffsetY = 0;

  protected float amplada = 50;
  protected float altura  = 50;

  protected ArrayList<Coin> coins;

  public QuestionBlock(PApplet app, float x, float y, ArrayList<Coin> coins) {
    this.app = app;
    this.posicio = new PVector(x, y);
    this.coins = coins;
  }

  public void update(TileMap map) {

    if (estat == Estat.HIT) {

      if (bounceTimer == 0) {
        bounceTimer = BOUNCE_TIME;
      }

      bounceTimer--;

      float t = (BOUNCE_TIME - bounceTimer) / (float) BOUNCE_TIME;

      // moviment suau cap amunt i tornada
      bounceOffsetY = -PApplet.sin(t * PApplet.PI) * 12;

      if (bounceTimer <= 0) {
        bounceOffsetY = 0;
        estat = Estat.USED;
      }
    }
  }

  public void display() {

    app.pushMatrix();
    app.translate(posicio.x, posicio.y + bounceOffsetY);

    app.imageMode(PApplet.CORNER);

    if (estat == Estat.IDLE) {
      app.image(Assets.TILE_QUESTION, 0, 0, amplada, altura);
    } else {
      app.image(Assets.TILE_USED_BLOCK, 0, 0, amplada, altura);
    }

    app.popMatrix();
  }

  public void hit() {

    if (estat != Estat.IDLE)
      return;

    estat = Estat.HIT;

    spawnCoin();
  }

  private void spawnCoin() {
    Coin c = new Coin(app,
      this.posicio.x + this.amplada / 2,
      this.posicio.y - this.altura / 2
    );
    coins.add(c);
  }

  // ---------------- GETS ----------------

  public float getX() { return posicio.x; }
  public float getY() { return posicio.y; }
  public float getWidth() { return amplada; }
  public float getHeight() { return altura; }
  public float getRadi() { return amplada * 0.5f; }

  public boolean isSolid() {
    return estat != Estat.IDLE; // després del hit ja és sòlid
  }
}
