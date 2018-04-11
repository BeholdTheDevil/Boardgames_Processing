package Othello;

import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anton on 2017-03-02.
 */

public class Othello extends PApplet {

    Piece[][] board;
    int padding = 100;
    float squareSize;

    int whitePieces = 2;
    int blackPieces = 2;

    final static int WHITE_PIECE = 101;
    final static int BLACK_PIECE = -101;
    int currentPlayer = WHITE_PIECE;

    public void settings() {
        size(700, 700);
    }

    public void setup() {
        squareSize = (width-2*padding)/8;
        board = new Piece[8][8];
        board[3][3] = new Piece(WHITE_PIECE, 3*squareSize, 3*squareSize);
        board[3][4] = new Piece(BLACK_PIECE, 3*squareSize, 4*squareSize);
        board[4][3] = new Piece(BLACK_PIECE, 4*squareSize, 3*squareSize);
        board[4][4] = new Piece(WHITE_PIECE, 4*squareSize, 4*squareSize);
    }

    public void mousePressed() {
        if(mouseX >= padding && mouseY >= padding && mouseX < width-padding && mouseY < height-padding) {
            int x = floor(map(mouseX, padding, width-padding, 0, 8));
            int y = floor(map(mouseY, padding, height-padding, 0, 8));

            if(board[y][x] == null) {
                board[y][x] = new Piece(currentPlayer, y*squareSize, x*squareSize);
                if(flip(y, x) != 0) {
                    if(currentPlayer == WHITE_PIECE) {
                        whitePieces++;
                    } else if(currentPlayer == BLACK_PIECE) {
                        blackPieces++;
                    }
                    currentPlayer = currentPlayer*-1;
                } else {
                    board[y][x] = null;
                }
                String s = "";
                if(blackPieces == 0) {
                    drawBoard();
                    s = "White wins";
                    noLoop();
                }
                else if(whitePieces == 0) {
                    drawBoard();
                    s = "Black wins";
                    noLoop();
                }
                pushMatrix();
                translate((width-200)/2, (height-200)/2);
                textAlign(CENTER);
                fill(255, 0, 0);
                text(s, 0, 0);
                popMatrix();
            }
        }
    }

    int flip(int y, int x) {
        int flipped = 0;
        flipped += checkLine(1, 1, x, y);
        flipped += checkLine(-1, 1, x, y);
        flipped += checkLine(1, -1, x, y);
        flipped += checkLine(-1, -1, x, y);

        flipped += checkLine(-1, 0, x, y);
        flipped += checkLine(1, 0, x, y);
        flipped += checkLine(0, -1, x, y);
        flipped += checkLine(0, 1, x, y);
        return flipped;
    }

    int checkLine(int xInc, int yInc, int x, int y) {
        int currentX = x;
        int currentY = y;
        int flipped = 0;
        boolean valid = false;

        while(currentX + 1*xInc >= 0 && currentY + 1*yInc >= 0 && currentX + 1*xInc < 8 && currentY + 1*yInc < 8) {
            currentX += 1*xInc;
            currentY += 1*yInc;
            if(board[currentY][currentX] != null && board[currentY][currentX].color == currentPlayer) valid = true;
        }
        if(!valid) return 0;

        currentX = x;
        currentY = y;


        while(currentX + 1*xInc >= 0 && currentY + 1*yInc >= 0 && currentX + 1*xInc < 8 && currentY + 1*yInc < 8 && board[currentY+1*yInc][currentX+1*xInc] != null && board[currentY+1*yInc][currentX+1*xInc].color != currentPlayer) {
            currentX += 1*xInc;
            currentY += 1*yInc;
            if(board[currentY][currentX].color == WHITE_PIECE) {
                blackPieces++;
                whitePieces--;
            } else if(board[currentY][currentX].color == BLACK_PIECE) {
                whitePieces++;
                blackPieces--;
            }
            board[currentY][currentX].color = currentPlayer;
            flipped++;
        }
        return flipped;
    }

    public void draw() {
        background(120, 120, 120);
        pushMatrix();
        translate(padding, padding);
        drawBoard();
        for(Piece[] row : board) {
            for(Piece p : row) {
                if(p != null) p.show();
            }
        }
        popMatrix();
        textSize(40);
        textAlign(CENTER);
        fill(255);
        text(whitePieces, 20+padding, 20+padding/2);
        fill(0);
        text(blackPieces, width-(20+padding), 20+padding/2);
        if(currentPlayer == WHITE_PIECE) fill(255);
        else fill(0);
        ellipse(mouseX, mouseY, squareSize/2, squareSize/2);
    }

    void drawBoard() {
        strokeWeight(4);
        rect(0, 0, width-200, height-200);
        strokeWeight(1);
        fill(0, 200, 0);
        stroke(0);
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                rect(2+i*squareSize, 2+j*squareSize, squareSize, squareSize);
            }
        }
    }

    class Piece {

        int color;
        float x, y;

        Piece(int c, float y, float x) {
            color = c;
            this.x = x;
            this.y = y;
        }

        void show() {
            pushMatrix();
            translate(2+squareSize/2, 2+squareSize/2);
            if(color == WHITE_PIECE) fill(255);
            else fill(0);
            ellipse(x, y, squareSize/2, squareSize/2);
            popMatrix();
        }
    }

    public static void main(String[] args) {
        PApplet.main("Othello");
    }
}
