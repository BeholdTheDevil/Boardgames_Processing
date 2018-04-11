import processing.core.PApplet;
import processing.core.PShape;

/**
 * Created by anton on 2017-03-03.
 */

public class Risk extends PApplet {

    PShape map;

    public void settings() {
        size(1366, 768);
    }

    public void setup() {
        /*map = loadShape("/home/anton/private/programs/java/Boardgames - Processing/src/world.svg");
        map.enableStyle();*/
    }

    public void draw() {
        background(0);
        fill(255);
        //shape(map, 0, 0, 500, 500);
        stroke(255);
        line(0, height/2, width, height/2);
    }

    public static void main(String[] args) {
        PApplet.main("Risk");
    }
}