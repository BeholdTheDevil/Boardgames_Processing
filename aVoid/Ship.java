package aVoid;

import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;

import static aVoid.aVoid.first;
import static processing.core.PApplet.max;
import static processing.core.PApplet.pow;
import static processing.core.PConstants.TWO_PI;

/**
 * Created by anton on 2017-03-14.
 */
public class Ship {

    PApplet parent;
    int size = 25;
    float targetAngle = 0;
    PVector position;
    PVector velocity;
    int maxSpeed = 5;
    PShape shape;

    Ship(PApplet parent) {
        this.parent = parent;
        this.velocity = new PVector(0, 0);
        this.velocity.limit(maxSpeed);
        this.position = new PVector(size*1.5f, parent.height*0.5f);
        this.shape = makeShape();
    }

    public void drawShip() {
        parent.pushMatrix();
        parent.translate(getX(), getY());
        parent.rotate(targetAngle);
        parent.shape(shape);
        parent.popMatrix();
    }

    private PShape makeShape() {
        parent.pushMatrix();
        parent.translate(getX(), getY());
        PShape s = parent.createShape();
        s.beginShape();
        s.fill(60, 128, 30);
        s.vertex(size, 0);
        s.vertex(size*parent.cos(3*parent.PI*0.25f), size*parent.sin(3*parent.PI*0.25f));
        s.vertex(size*-0.25f, 0);
        s.vertex(size*parent.cos(5*parent.PI*0.25f), size*parent.sin(5*parent.PI*0.25f));
        s.endShape();
        parent.popMatrix();
        return s;
    }

    public void update(float dt) {
        float dx = parent.mouseX - getX();
        float dy = parent.mouseY - getY();
        if(getX() + (dt*dx*maxSpeed) > parent.width*0.3f) {
            dx = 0;
        }
        if(getX() + (dt*dx*maxSpeed) < size*1.25f) {
            dx = 0;
        }
        float angle = parent.atan2(dy, dx);
        float dir = (angle - targetAngle)/TWO_PI;
        dir -= parent.round(dir);
        dir = dir*TWO_PI;
        targetAngle += dir*0.5f;

        addX(dt*dx*maxSpeed);
        addY(dt*dy*maxSpeed);
    }

    public float getX() {
        return this.position.x;
    }

    public float getY() {
        return this.position.y;
    }

    public void setX(float x) { this.position.x = x; }

    public void setY(float y) { this.position.y = y; }

    public void addX(float x) { this.position.x += x; }

    public void addY(float y) { this.position.y += y; }

}
