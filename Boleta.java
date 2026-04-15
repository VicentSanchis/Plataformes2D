import processing.core.PVector;
import processing.core.PApplet;

public class Boleta extends Entity {
  protected PApplet        app;
  protected int            mida                =  30;
  protected int            vides               =   3;
  protected int            invulnerableTimer   =   0;
  protected int            coyoteFrames        =   0;
  protected int            coyoteMaxFrames     =   6;
  protected int            jumpBufferFrames    =   0;
  protected int            jumpBufferMaxFrames =   6;
  protected int            deathTimer          =   0;
  protected int            deathMaxTime        =  90;
  protected float          escalaX             =   1;
  protected float          escalaY             =   1;
  protected float          fallMultiplier      =   1.5f;
  protected float          velocitatEscalar    =   4;
  protected float          impulsBot           = -10;
  protected float          celebracioTimer;
  protected boolean        enTerra;
  protected boolean        mouEsquerra;
  protected boolean        mouDreta;
  protected boolean        saltMantingut;
  protected boolean        celebrant;
  protected boolean        mort;
  protected boolean        visible;
  protected PVector        spawnPoint;
  protected PVector        posicioAnterior;
  protected PVector        velocitat;
  protected PVector        gravetat;
  protected PVector        celebracioTarget;
  protected int            celebracioFase;
  protected ParticleSystem particles;
  
  enum EstatJugador {NORMAL, FERIT, MORT, CELEBRANT}
  
  public Boleta (PApplet p) {
    this.app              = p;
    this.posicio          = new PVector (50, app.height-20);
    this.spawnPoint       = new PVector (50, app.height-20);
    this.posicioAnterior  = this.posicio.copy();
    this.velocitat        = new PVector (0, 0);
    this.gravetat         = new PVector (0, 0.4f);
    this.visible          = true;
  }

  public void display () {
    float angle   = PApplet.map(velocitat.x, -velocitatEscalar,  velocitatEscalar,  -0.4f,  0.4f);
    float stretch = PApplet.map(PApplet.abs(velocitat.x), 0, velocitatEscalar, 1f, 1.1f);
    float ample   = mida * stretch * escalaX;
    float alt     = mida / stretch * escalaY;
    
    if (mort) {
      app.pushMatrix();
        app.translate(posicio.x, posicio.y);
        app.rotate(PApplet.PI);
        dibuixaCos(ample, alt, app.color(255,125,0));
      app.popMatrix();
    }
    else {
      app.pushMatrix();
        app.translate(posicio.x, posicio.y);
        app.rotate(angle);
        dibuixaCos(ample, alt, app.color(0,255,0));
      app.popMatrix();
    }
  }
  
  private void dibuixaCos(float amp, float alt, int colorCos) {
    app.strokeWeight(1);
      app.stroke(0, 150, 0);
      app.fill(colorCos);
      app.ellipse(0,0, amp, alt);
      
      // Ulls
      app.noStroke();
      app.fill(255);
      app.circle(-7,-2, 10);
      app.circle(7,-2,10);
      
      // Pupil·les
      app.fill(0);
      app.circle(-5,-2,4);
      app.circle( 8,-2,4);
      
      // Boca
      app.fill(255,0,0);
      app.ellipse(0,9,7,2);
  }
  
  public void update (TileMap tileMap) {
    this.visible = true;
    
    if (invulnerableTimer > 0) {
      invulnerableTimer --;
      if (invulnerableTimer % 2 == 0)
        this.visible = false;
    }
      
    if (celebrant) {
      updateCelebracio();
      return;
    }
    
    if (mort) {
      updateMort();
      return;
    }
    
    posicioAnterior.set(posicio);
    
    float direccioX = 0;
    if (mouEsquerra) 
      direccioX -= 1;
      
    if (mouDreta)    
      direccioX += 1;

    float accel = enTerra ? 0.8f : 0.2f;
    velocitat.x += direccioX * accel;

    if (direccioX == 0)
      velocitat.x *= enTerra ? 0.8f : 0.98f;

    velocitat.x = PApplet.constrain(velocitat.x, -velocitatEscalar, velocitatEscalar);

    if (!enTerra) {
      escalaX = PApplet.lerp(escalaX, 0.85f, 0.15f);
      escalaY = PApplet.lerp(escalaY, 1.15f, 0.15f);

      if (velocitat.y > 0)
        velocitat.y += gravetat.y + fallMultiplier;
      
      else 
        velocitat.y += gravetat.y;
    }
    else {
      escalaX = PApplet.lerp(escalaX, 1.0f, 0.2f);
      escalaY = PApplet.lerp(escalaY, 1.0f, 0.2f); 
    }
      
    if (enTerra)
      coyoteFrames = coyoteMaxFrames;
      
    else if (coyoteFrames > 0)
      coyoteFrames --;
      
    if (jumpBufferFrames > 0) 
      jumpBufferFrames --;
      
    if (jumpBufferFrames > 0 && (enTerra || teCoyoteTime())) {
      velocitat.y = impulsBot;
      enTerra = false;
      jumpBufferFrames = 0;
      coyoteFrames = 0;
    }
    
    if (!saltMantingut && velocitat.y < 0) {
      velocitat.y = Math.max(velocitat.y, -3);
    }
    
    velocitat.y = PApplet.constrain(velocitat.y, -20,  12);
  }
  
  public void iniciarCelebracio(float x, float y) {
    celebrant = true;
    celebracioTarget = new PVector(x, y);
    celebracioFase = 0;
  }
  private void updateCelebracio() {

    switch (celebracioFase) {

        case 0:

            posicio.x = PApplet.lerp(
                posicio.x,
                celebracioTarget.x,
                0.04f
            );

            if (PApplet.abs(posicio.x - celebracioTarget.x) < 3) {
                celebracioFase = 1;
            }

        break;

        case 1:

            if (enTerra) {
                velocitat.y = -7;
                enTerra = false;
            }

            velocitat.y += gravetat.y;

        break;
    }
}

  private void updateMort() {
    deathTimer ++;
    
    escalaX = PApplet.lerp(escalaX, 1, 0.05f);
    escalaY = PApplet.lerp(escalaY, 1, 0.05f);

    velocitat.y += gravetat.y;
    posicio.y   += velocitat.y;
    
    if (deathTimer > deathMaxTime) {
      if (vides > 0)
        respawn();
    }
  }

  public void mouX () { this.posicio.x += this.velocitat.x;  }
  public void mouY () { this.posicio.y += this.velocitat.y;  }

  public void gestionarTeclat(int clau, int codi, boolean value) {
    if (clau == PApplet.CODED) {
      switch (codi) {
        case PApplet.LEFT:  mouEsquerra = value;            break;
        case PApplet.RIGHT: mouDreta    = value;            break;
        case PApplet.DOWN:  if (value) velocitat.y += 2.5f; break;
        case PApplet.UP:
          if (value) {
            jumpBufferFrames = jumpBufferMaxFrames;
            saltMantingut = true;
          }
          else
            saltMantingut = false;
        break;
      }
    }
  }
  
  public void setEnTerra (boolean value) {
    if (!enTerra && value) {
      escalaX = 1.3f;
      escalaY = 0.7f;
    }
    this.enTerra = value;
  }
  
  public void damage() {
    if (invulnerableTimer > 0 || mort)
      return;

    vides --;
    morir();
  }
  
  public void morir() {
    mort = true;
    velocitat.x = 0;
    velocitat.y = -8;
    escalaX = 0.7f;
    escalaY = 1.4f;
  }
  
  public boolean animacioMortAcabada () {
    return deathTimer >= deathMaxTime;
  }
  
  public void respawn() {
    posicio.set(spawnPoint);
    velocitat.set(0,0);
    mort = false;
    deathTimer = 0;
    invulnerableTimer = 120;
}

  // --------------------------------- GETS -------------------------------------
  public boolean tocaTerra           () { return this.enTerra;           }
  public PVector getPosicio          () { return this.posicio;           }
  public float   getRadi             () { return this.mida / 2f;         }
  public float   getWidth            () { return this.mida;              }
  public float   getHeight           () { return this.mida;              }
  public float   getVelocitatX       () { return this.velocitat.x;       }
  public float   getVelocitatY       () { return this.velocitat.y;       }
  public float   getPosicioAnteriorX () { return this.posicioAnterior.x; }
  public float   getPosicioAnteriorY () { return this.posicioAnterior.y; }
  public boolean teCoyoteTime        () { return this.coyoteFrames > 0;  }
  public float   getX                () { return this.posicio.x;         }
  public float   getY                () { return this.posicio.y;         }
  public int     getVides            () { return this.vides;             }
  public boolean estaMort            () { return this.mort;              }
  public boolean esGameOver          () { return this.vides <= 0;        }

  // --------------------------------- SETS -------------------------------------
  public void setVelocitatX (float   value) { this.velocitat.x = value;  }
  public void setVelocitatY (float   value) { this.velocitat.y = value;  }
  public void setCelebrant  (boolean value) { this.celebrant   = value;  }
}
