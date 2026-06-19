import java.util.ArrayList;
import processing.core.PApplet;

public class Nivell {
  protected PApplet                   app;
  protected EstatNivell               estat;
  protected int                       numero;
  protected int                       ampladaNivell;
  protected int                       alsadaNivell;
  protected int                       introTimer = 120;
  protected int                       completeTimer = 180;
  protected TileMap                   tileMap;
  protected Boleta                    personatge;
  protected GestorCollisions          gestor;
  protected Goal                      goal;
  protected HUD                       hud;
  protected ArrayList <Coin>          coins;
  protected ArrayList <Enemy>         enemics;
  protected ArrayList <QuestionBlock> questionBlocks;
  protected boolean                   completat;
  
  enum EstatNivell { MENU, INTRO, PLAYING, COMPLETE, GAME_OVER }

  public Nivell (PApplet p, String file) {
    this.app       = p;
    this.estat     = EstatNivell.INTRO;
    personatge     = new Boleta(this.app);
    coins          = new ArrayList <Coin>();
    enemics        = new ArrayList <Enemy>();
    questionBlocks = new ArrayList <QuestionBlock>();
    gestor         = new GestorCollisions();
    tileMap        = new TileMap(this, app, file);
    numero         = tileMap.getNumero();
    ampladaNivell  = tileMap.getAmpladaNivell ();
    alsadaNivell   = tileMap.getAlsadaNivell ();
    
    tileMap.extreuMonedes(coins);
    tileMap.extreuEnemics(enemics);
    tileMap.extreuMeta();
    tileMap.extreuQuestionBlocks(questionBlocks);
    
    goal  = tileMap.getGoal();
    hud   = new HUD(app);
  }

  public void update() {
   switch(estat) {
     case INTRO:
       introTimer --;
        
       if (introTimer <= 0)
         estat = EstatNivell.PLAYING;
     break;
      
     case PLAYING:
       actualitzaJugador();
     break;
      
     case COMPLETE:
       actualitzaJugador();
       completeTimer --;
       if (completeTimer <= 0)
         this.completat = true;
     break;
    }
    
    if (personatge.esGameOver()) {
      if (personatge.estaMort() && personatge.animacioMortAcabada())
        estat = EstatNivell.GAME_OVER;
    }
    
    actualitzaEnemics();
    
    int score = gestor.resolColisionsJugadorEnemics(personatge, enemics);
    
    actualitzarMonedes ();
    score += gestor.resolColisionsJugadorMonedes(personatge, coins);
    
    actualitzarQuestionBlocks();
    score += gestor.resolColisionsJugadorQuestionBlocks(personatge, questionBlocks);
    
    afegeixScore(score);
    gestionaMeta();
  }
  
  public void display() {
    tileMap.display();
    
    if(goal != null)
      goal.display();
    
    switch (estat) {
      case INTRO:
        app.pushMatrix();
        app.resetMatrix();
        app.fill(255,100,50,200);
        app.textSize(100);
        app.textAlign(PApplet.CENTER);
        app.text("NIVELL " + numero, app.width/2, app.height/2-100);
        app.popMatrix();
      break;
      
      case PLAYING:
        personatge.display();
      break;
      
      case COMPLETE:
        personatge.display();
        app.pushMatrix();
        app.resetMatrix(); //<>//
        app.fill(255,100,50,200);
        app.textSize(100);
        app.textAlign(PApplet.CENTER);
        app.text("COMPLETAT", app.width/2, app.height/2-100);
        app.popMatrix();
      break;
      
      case GAME_OVER:
        app.pushMatrix();
        app.resetMatrix();
        app.fill(255,100,50,200);
        app.textAlign(PApplet.CENTER);
        app.textSize(100);
        app.text("GAME OVER",  app.width/2,  app.height/2);
        app.textSize(50);
        app.text("Prem Espai",  app.width/2,  app.height/2 + 50);
        app.popMatrix();

      break;
    }
    
    for (Coin c : coins)
      c.display();

    for (Enemy e : enemics)
      e.display();
      
    for (QuestionBlock q : questionBlocks)
      q.display();
      
    hud.display(this, personatge, ((Plataformes2D)app).score, numero);
  }
  
  protected void actualitzaJugador () {
    personatge.update(tileMap);
    
    if (personatge.estaMort())
      return;
      
    personatge.mouX   ();
    gestor.resolColisionsX(personatge, tileMap);
    
    personatge.mouY ();
    gestor.resolColisionsY(personatge, tileMap, questionBlocks);
  }
  
  protected void actualitzaEnemics() {
    for (int i = enemics.size()-1; i >= 0; i--) {
      Enemy e = enemics.get(i);
      e.update(tileMap);
      if (!e.estaViu()) 
        enemics.remove(i);
    }
  }
  
  private void actualitzarMonedes() {
    for (int i = coins.size()-1; i >= 0; i--) {
      Coin c = coins.get(i);
      c.update(this.tileMap);
      
      if (c.isDead()) 
        coins.remove(i);
    }
  }
  
  private void actualitzarQuestionBlocks() {
    for (int i = questionBlocks.size()-1; i >= 0; i --) {
      QuestionBlock q = questionBlocks.get(i);
      q.update(this.tileMap);
    }
  }
  
  private void gestionaMeta() {
    if (goal == null)
        return;

    float dx = personatge.getX() - goal.getX();
    float dy = personatge.getY() - goal.getY();

    float radiTotal = personatge.getRadi() + goal.getWidth() * 0.5f;
    
    if (estat == EstatNivell.PLAYING && dx*dx + dy*dy < radiTotal * radiTotal) {
      this.estat = EstatNivell.COMPLETE;
      personatge.iniciarCelebracio(goal.getX(), goal.getY());
    }
  }
  
  private void afegeixScore(int score) {
    ((Plataformes2D)app).incrementaMarcador(score);
  }

  public void reiniciar() {
    tileMap       = new TileMap(this, app, tileMap.getFilePath()); // o guarda el path
    personatge    = new Boleta(app);
    coins.clear();
    enemics.clear();
  
    tileMap.extreuMonedes(coins);
    tileMap.extreuEnemics(enemics);
    tileMap.extreuMeta();
    goal = tileMap.getGoal();
  
    estat = EstatNivell.INTRO;
    introTimer = 120;
    completeTimer = 180;
  }

  public void gestionarTeclat  (int clau, int codi, boolean value) {
    if (estat == EstatNivell.PLAYING)
      personatge.gestionarTeclat (clau, codi, value);
      
    if (estat == EstatNivell.GAME_OVER) {
      if (value && clau == ' ') {
        reiniciar();
      }
    }
  }

  // ---------------------------------------- GETS --------------------------------------------
  public TileMap               getTileMap         () { return this.tileMap;                    }
  public float                 getAmpladaNivell   () { return this.ampladaNivell;              }
  public int                   getAlsadaNivell    () { return this.alsadaNivell;               }
  public ArrayList<Coin>       getCoins           () { return this.coins;                      }
  public float                 getPlayerPositionX () { return this.personatge.getPosicio().x;  }
  public float                 getPlayerPositionY () { return this.personatge.getPosicio().y;  } 
  public boolean               estaCompletat      () { return this.completat;                  }
}
