import processing.core.PVector;
import processing.core.PApplet;

public abstract class Entity {
  protected PVector posicio;
  protected PVector velocitat;

  protected abstract void  update    (TileMap map);
  protected abstract void  display   ();
  protected abstract float getX      ();
  protected abstract float getY      ();
  protected abstract float getRadi   ();  // Només per a entitats circulars. Ja veurem com ho faig
  protected abstract float getWidth  ();
  protected abstract float getHeight ();
}
