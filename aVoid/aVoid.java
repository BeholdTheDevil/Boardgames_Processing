package aVoid;

import processing.core.PApplet;
import aVoid.Debris;
import processing.core.PGraphics;
import processing.core.PShape;

/**
 * Created by anton on 2017-03-13.
 */

public class aVoid extends PApplet {

    public Ship player;
    public static Debris first;
    Updater updater;
    public Debris breakpoint;
    public int debrisAmount = 75;
    public Debris[] debris;
    float t0 = 0;
    float t1;

    boolean gameover = false;

    public void settings() {
        size(1000, 700);
    }

    public void setup() {
        frameRate = 40;
        noCursor();

        float yoff = 0.01f;
        player = new Ship(this);
        first = new Debris(null, this, yoff);
        debris = new Debris[debrisAmount];
        debris[0] = first;

        for(int i = 1; i < debrisAmount; i++) {
            yoff += 0.2f;
            debris[i] = new Debris(debris[i-1], this, yoff+random(0, 0.1f));
        }

        breakpoint = new Debris(debris[debrisAmount-1], this, 0);
        breakpoint.breakpoint = true;
        breakpoint.velocity = 0;
        breakpoint.position.x = width*0.333f;

        first.position.x = width;
        updater = new Updater();
        updater.start();
    }

    public void startGame() {

        float yoff = 0.01f;
        player = new Ship(this);
        first = new Debris(null, this, yoff);
        debris = new Debris[debrisAmount];
        debris[0] = first;

        for(int i = 1; i < debrisAmount; i++) {
            yoff += 0.2f;
            debris[i] = new Debris(debris[i-1], this, yoff+random(0, 0.1f));
        }

        breakpoint = new Debris(debris[debrisAmount-1], this, 0);
        breakpoint.breakpoint = true;
        breakpoint.velocity = 0;
        breakpoint.position.x = width*0.333f;

        first.position.x = width;
        gameover = false;
    }

    public void keyPressed() {
        if(gameover && keyCode == ENTER) {
            startGame();
        }
    }

    public void draw() {
        background(0);
        System.out.print("\rFramerate: " + frameRate);
        player.drawShip();
        if(!gameover) {
            for(Debris d : debris) {
                d.drawDebris();
            }
        }
    }

    class Updater extends Thread {

        float t0 = 0;
        float t1;

        public void run() {
            while(true) {
                t1 = millis();
                if(!gameover) {
                    gameover = first.update((t1 - t0)*0.001f, player, true);
                    player.update((t1 - t0)*0.001f);
                    first.sort();
                }
                t0 = t1;
            }
        }
    }

    public static void main(String[] args) {
        PApplet.main("aVoid.aVoid");
    }
}