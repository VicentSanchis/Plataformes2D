import java.util.ArrayList;
import processing.core.PVector;
import processing.core.PApplet;

public class GestorCollisions {

  public void resolColisionsX (Boleta b, TileMap mapa) {
    PVector pos        = b.getPosicio();
    float   radi       = b.getRadi();
    float   nextX      = pos.x + b.getVelocitatX();
    float   top        = pos.y - radi + 1;
    float   bottom     = pos.y + radi - 1;
    int     topTile    = mapa.worldToTile(top);
    int     bottomTile = mapa.worldToTile(bottom);

    // ================= DRETA =================
    if (b.getVelocitatX() > 0) {
      int tileX = mapa.worldToTile(nextX + radi);

      for (int y = topTile; y <= bottomTile; y++) {
        if (mapa.esSolidLateral(tileX, y)) {
          pos.x = tileX * mapa.tileSize - radi -0.01f;
          b.setVelocitatX(0);
          return;
        }
      }
    }
    // ================= ESQUERRA =================
    else if (b.getVelocitatX() < 0) {
      int tileX = mapa.worldToTile(nextX - radi);
      for (int y = topTile; y <= bottomTile; y++) {
        if (mapa.esSolidLateral(tileX, y)) {
          pos.x = (tileX + 1) * mapa.tileSize + radi + 0.01f;
          b.setVelocitatX(0);
          return;
        }
      }
    }
  }

  public void resolColisionsY (Boleta b, TileMap mapa) {
    PVector pos         = b.getPosicio();
    float   radi        = b.getRadi();
    float   esquerra    = pos.x - radi;
    float   dreta       = pos.x + radi;
    float   top         = pos.y - radi;
    float   bottom      = pos.y + radi;
    int     leftTile    = mapa.worldToTile(esquerra);
    int     rightTile   = mapa.worldToTile(dreta);
    boolean tocantTerra = false;

    // ===== CAIENT =====
    if (b.getVelocitatY() >= 0) {
      int bottomTile = mapa.worldToTile(bottom); 
      for (int tx = leftTile; tx <= rightTile; tx++) {

        boolean esSolid       = mapa.esSolid(tx, bottomTile);
        boolean oneWay        = mapa.esOneWay(tx, bottomTile);
        boolean questionBlock = mapa.esQuestionBlock(tx, bottomTile);

        if (!esSolid && !oneWay && !questionBlock)
          continue;

        if (oneWay) {
          if (b.getPosicioAnteriorY() + radi > bottomTile * mapa.tileSize)
            continue;
        }
        
        pos.y = bottomTile * mapa.tileSize - radi;
        b.setVelocitatY(0);
        b.setEnTerra(true);
        return;
        
      }
    }
    // ===== PUJANT =====
    else if (b.getVelocitatY() < 0) { //<>//
      int topTile = mapa.worldToTile(top);
      for (int tx = leftTile; tx <= rightTile; tx++) {
        
        if (mapa.esOneWay(tx, topTile)) {
          continue;
        } 
          
        if (!mapa.esSolid(tx, topTile)) {
          continue;
        }
          
        pos.y = (topTile + 1) * mapa.tileSize + radi;
        b.setVelocitatY(0);
        break;
      }
    }
    b.setEnTerra(tocantTerra);
  }
  
  public int resolColisionsJugadorEnemics (Boleta player, ArrayList<Enemy> enemics) {
    if (player.estaMort()) 
      return 0;
      
    int score = 0;
    
    for (Enemy e : enemics) {
      if (e.estaMorint())
        continue;

      float px = player.getPosicio().x;
      float py = player.getPosicio().y;
      float ex = e.getX();
      float ey = e.getY();
      float pr = player.getRadi();
      float er = e.getRadi();

      boolean overlapX = PApplet.abs(px - ex) < pr + er;
      boolean overlapY = PApplet.abs(py - ey) < pr + er;

      if (!overlapX || !overlapY)
        continue;

      boolean falling = player.getVelocitatY() > 0;
      boolean above = player.getPosicioAnteriorY() + pr <= e.getTop();

      if (falling && above) {
        e.kill();
        player.setVelocitatY(-6);
        score = 50;
      } 
      else {
        player.damage();
        
        if (player.estaMort()) {
          player.morir();
        }
        player.setVelocitatX((player.getX() < e.getX()) ? -6 : 6);
        player.setVelocitatY(-5);
      }
    }
    return score;
  }

  public int resolColisionsJugadorMonedes(Boleta player,  ArrayList<Coin> coins) {
    int score = 0;
    for (int i = coins.size()-1; i >= 0; i--) {
      Coin c = coins.get(i);
      
      if (c.isCollected())
        continue;

      float dx = player.getPosicio().x - c.getX();
      float dy = player.getPosicio().y - c.getY();
      float radiTotal = player.getRadi() + c.getRadi();

      if (dx*dx + dy*dy < radiTotal*radiTotal) {
        c.collect();
        score += 100;    // Açò deuria anar a coin ja que és el seu valor
      }
    }
    return score;
  }
  
  public int resolColisionsJugadorQuestionBlocks (Boleta player, ArrayList<QuestionBlock> blocs) {
    int score = 0;
    
    for (int i = blocs.size()-1; i >= 0; i--) {
  
      QuestionBlock bloc = blocs.get(i);
      
      // Calculem els límits del questionBlock
      float left   = bloc.getX();
      float right  = bloc.getX() + bloc.getWidth();
      float top    = bloc.getY();
      float bottom = bloc.getY() + bloc.getHeight();
      
      // Calculem els límits del jugador
      float pLeft   = player.getX() - player.getRadi();
      float pRight  = player.getX() + player.getRadi();
      float pTop    = player.getY() - player.getRadi();
      float pBottom = player.getY() + player.getRadi();
      
      float playerPrevBottom = player.getPosicioAnteriorY() + player.getRadi();
      float playerNowTop     = player.getY() - player.getRadi();
      
      boolean overlap = pRight > left && pLeft < right && pBottom > top && pTop < bottom;
      
      boolean hittingFromBelow = player.getVelocitatY() < 0 && playerPrevBottom >= bottom;
        
      // Per colpejar per baix
      if (overlap && hittingFromBelow) {
        bloc.hit();
        player.setVelocitatY(2);
        player.getPosicio().y = bottom + player.getRadi();
        score += 30;
      }
      
      boolean landingOnTop =  player.getVelocitatY() >= 0 &&  playerPrevBottom <= top;
      // Per colpejar per dalt
      if (overlap && landingOnTop) {
        player.getPosicio().y = top - player.getRadi();
        player.setVelocitatY(0);
        player.setEnTerra(true);
      }
    }
    return score;
  }
}
