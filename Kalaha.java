import processing.core.PApplet;

/**
 * Created by anton on 2017-03-02.
 */

public class Kalaha extends PApplet {

    int paddingX = 100;
    int paddingY = 200;

    public void settings() {
        size(700, 700);
    }

    public void setup() {

    }

    public void draw() {
        background(0);
        translate(paddingX, paddingY);
    }

    public static void main(String[] args) {
        PApplet.main("Kalaha");
    }
}