import java.util.ArrayList;
import processing.core.*;

public class ParticleSystem {
  protected ArrayList<Particle> particles;

  public ParticleSystem() {
    particles = new ArrayList<>();
  }

  public void add(Particle p) {
    particles.add(p);
  }

  public void update() {
    for (int i = particles.size()-1; i >= 0; i--) {
      Particle p = particles.get(i);
      p.update();

      if (p.isDead()) 
        particles.remove(i);
    }
  }

  public void display(PApplet app) {
    for (Particle p : particles) 
      p.display(app);
  }
  
  public boolean isEmpty () {
    return this.particles.size() == 0;
  }
}
