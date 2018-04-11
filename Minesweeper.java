import processing.core.PApplet;

import java.util.Arrays;

/**
 * Created by anton on 2017-03-13.
 */

public class Minesweeper extends PApplet {

    Tile[] board;
    float padding = 50;
    int rowSize = 20;
    int boardSize;
    int[] neighbours = {-rowSize-1, -rowSize, -rowSize+1, -1, 1, rowSize-1, rowSize, rowSize+1};
    int windowSize = 700;
    int numberOfMines;
    float tileSize;

    public void settings() {
        size(windowSize, windowSize);
    }

    public void setup() {
        boardSize = (int)Math.pow(rowSize, 2);
        tileSize = (windowSize-2*padding)/rowSize;
        board = new Tile[boardSize];
        numberOfMines = floor(random(boardSize*0.175f, boardSize*0.25f));

        for(int i = 0; i < board.length; i++) {
            board[i] = new Tile(i, false);
        }
        for(int i = 0; i < numberOfMines; i++) {
            int randInd = floor(random(0, boardSize));
            board[randInd].setMine(true);
        }

        for(Tile t : board) {
            if(!t.isMine()) {
                t.setProximity();
            }
        }
    }

    public void mousePressed() {

        int tileX = floor(map(mouseX, padding, width-padding, 0, rowSize));
        int tileY = floor(map(mouseY, padding, height-padding, 0, rowSize));
        int pos = tileX + tileY*rowSize;

        board[pos].setShow(true);

    }

    public void draw() {
        background(0);
        translate(padding, padding);
        drawBoard();
    }

    void drawBoard() {
        for(Tile t : board) {
            t.draw();
        }
        stroke(210);
        strokeWeight(4);
        line(0, 0, rowSize*tileSize, 0);
        line(0, 0, 0, rowSize*tileSize);
        stroke(50);
        line(rowSize*tileSize+2, 0, rowSize*tileSize+2, rowSize*tileSize+2);
        line(0, rowSize*tileSize+2, rowSize*tileSize+2, rowSize*tileSize+2);
    }

    class Tile {

        private int tileX, tileY;
        private int pos;
        private int proximity = 0;
        boolean show = false;
        boolean mine;

        Tile(int pos, boolean mine) {
            this.pos = pos;
            this.tileX = pos%rowSize;
            this.tileY = pos/rowSize;
            this.mine = mine;
        }

        void draw() {
            if(show) {
                fill(200);
                strokeWeight(1);
                noStroke();
                rect(tileX*tileSize, tileY*tileSize, tileSize, tileSize);
                if(proximity > 0) {
                    fill(0, 0, 200);
                    textAlign(CENTER);
                    text(proximity, tileX*tileSize + tileSize/2, tileY*tileSize + 2*tileSize/3);
                }
            } else {
                fill(128);
                strokeWeight(1);
                noStroke();
                rect(tileX*tileSize, tileY*tileSize, tileSize, tileSize);
            }
            strokeWeight(2);
            stroke(255);
            line(tileX*tileSize+2, tileY*tileSize+2, tileX*tileSize+tileSize+2, tileY*tileSize+2);
            line(tileX*tileSize+2, tileY*tileSize+2, tileX*tileSize+2, tileY*tileSize+tileSize+2);
            stroke(60);
            line(tileX*tileSize+tileSize, tileY*tileSize, tileX*tileSize+tileSize, tileY*tileSize+tileSize);
            line(tileX*tileSize, tileY*tileSize+tileSize, tileX*tileSize+tileSize, tileY*tileSize+tileSize);


            if(mine) {
                stroke(0);
                fill(0);
                textAlign(CENTER);
                textSize(2*tileSize/3);
                text("\ud83d\udca3", tileX*tileSize + tileSize/2, tileY*tileSize + 2*tileSize/3);
            }
        }

        int checkNeighbours() {
            int n = 0;
            for(int i = 0; i < 8; i++) {
                if(pos+neighbours[i] >= 0 && pos+neighbours[i] < boardSize) {
                    if(board[pos+neighbours[i]].isMine()) n++;
                }
            }
            System.out.println(n);
            return n;
        }

        public void setShow(boolean b) {
            this.show = b;
        }

        public int getProximity() {
            return proximity;
        }

        public void setProximity() {
            this.proximity = checkNeighbours();
        }

        public void setMine(boolean b) {
            this.mine = b;
        }

        public boolean isMine() {
            return mine;
        }

        public int getTileX() {
            return tileX;
        }

        public int getTileY() {
            return tileY;
        }
    }

    public static void main(String[] args) {
        PApplet.main("Minesweeper");
    }
}