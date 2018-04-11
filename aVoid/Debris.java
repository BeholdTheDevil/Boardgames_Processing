package aVoid;

import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;

import static aVoid.aVoid.first;
import static processing.core.PApplet.*;

/**
 * Created by anton on 2017-03-14.
 */
public class Debris {

    PApplet parent;
    PVector position;
    PShape shape;
    float velocity;
    float size;
    Debris next;
    Debris prev = null;
    boolean breakpoint = false;
    float rotation = 0;
    float direction;
    float speedfactor = 0.5f;

    Debris(Debris prev, PApplet parent, float yoff) {
        direction = parent.random(0, 1f) < 0.5f ? -1 : 1;
        this.size = parent.random(7, 15);
        this.parent = parent;
        this.prev = prev;
        if(prev != null) prev.setNext(this);
        this.position = new PVector(parent.random(parent.width, parent.width*2), parent.random(size, parent.height-30));
        velocity = (float)Math.pow(parent.random(15, 25), 2)*speedfactor;

        this.shape = makeShape(yoff);
    }

    PShape makeShape(float yOff) {
        float smallestR = size;
        float r, x, y, offset;
        float xOff = 0;

        parent.pushMatrix();
        parent.translate(getX(), getY());
        PShape s = parent.createShape();
        s.beginShape();
        for(float i = 0; i < parent.TWO_PI; i += 0.1) {
            offset = parent.map(parent.noise(xOff + yOff), 0, 1, -size*0.35f, size*0.35f);
            r = this.size + offset;
            if(r < smallestR) smallestR = r;
            x = r * cos(i);
            y = r * sin(i);
            s.vertex(x, y);
            xOff += 0.1f;
        }
        if(breakpoint) s.fill(255, 0, 0);
        s.endShape();
        parent.popMatrix();
        size = smallestR;

        return s;
    }

    void drawDebris() {
        parent.pushMatrix();
        parent.translate(getX(), getY());
        parent.rotate(rotation);
        parent.shape(this.shape);
        parent.popMatrix();
    }

    boolean update(float dt, Ship player, boolean collisioncheck) {
        this.rotation += (this.direction*(velocity*0.01f))*dt;
        this.position.x -= this.velocity*dt;
        if(this.position.x < -size) {
            this.position.x = parent.random(parent.width, parent.width*2);
            this.position.y = parent.random(size, parent.height-size);
            this.velocity = (float)Math.pow(parent.random(15, 25), 2)*speedfactor;
        }
        if(collisioncheck) {
            float mag = PVector.dist(position, player.position);
            if(mag < player.size + this.size - 5) {
                return true;
            }
        }
        if(next != null) {
            if(this.breakpoint || !collisioncheck) {
                if(next.update(dt, player, false));
            } else if(next.update(dt, player, collisioncheck)) {
                return true;
            }
        }
        return false;
    }

    void sort() {
        Debris nextInLine = next;
        Debris compare = prev;
        while(compare != null && this.position.x < prev.getX()) {
            compare = compare.prev;
        }
        if(compare != null) {
            Debris temp = this.getNext();
            this.setNext(compare);
            prev.setNext(temp);
            if(temp != null) temp.setPrev(compare);

            temp = compare.getPrev();
            compare.setPrev(this);
            this.setPrev(temp);

            if(temp != null) temp.setNext(this);
            if(prev == null) first = this;
        }
        if(nextInLine != null) nextInLine.sort();
    }

    public float getX() { return this.position.x; }

    public float getY() { return this.position.y; }

    public void setPrev(Debris d) {
        this.prev = d;
    }

    public Debris getPrev() {
        return this.prev;
    }

    public void setNext(Debris d) {
        this.next = d;
    }

    public Debris getNext() {
        return this.next;
    }
}
