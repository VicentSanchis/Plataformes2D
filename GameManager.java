import processing.core.PApplet;

public class GameManager {
  protected PApplet       app;
  protected Nivell        nivellActual;
  protected int           indexNivell = 1;
  protected ParallaxLayer bg;

  public GameManager(PApplet app) {
    this.app = app;
    carregarNivell(indexNivell);
    bg = new ParallaxLayer(app);
  }
  
  public void update() {
    nivellActual.update();

    if (nivellActual.estaCompletat()) {
      seguentNivell();
    }
  }

  public void display(float cX, float cY) {
    bg.setCamera(cX, cY);
    bg.display();
    nivellActual.display();
  }

  public void seguentNivell() {
    System.out.println("CAnviem de nivell");
    indexNivell++;
    carregarNivell(indexNivell);
  }

  private void carregarNivell(int i) {
    String file = "./assets/nivells/nivell" + i + ".json";
    nivellActual = new Nivell(app, file);
  }

  public Nivell getNivellActual() {
    return nivellActual;
  }
}
