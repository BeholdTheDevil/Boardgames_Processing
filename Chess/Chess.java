package Chess;

import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anton on 2017-02-11.
 */

public class Chess extends PApplet {

    boolean helpMode = true;

    float squareSize;
    int padding = 100;
    int prevX = -1;
    int prevY = -1;
    final static int WHITE_PIECE = 101;
    final static int BLACK_PIECE = -101;
    int currentPlayer = WHITE_PIECE;
    String player = "White player";

    Piece[][] board = new Piece[8][8];

    public void settings() {
        size(700, 700);
    }

    public void setup() {
        squareSize = (width-2*padding)/8;
        createBoard();
    }

    void createBoard() {
        //White rooks
        board[7][0] = new Piece('r', 7, 0, WHITE_PIECE);
        board[7][7] = new Piece('r', 7, 7, WHITE_PIECE);
        //Black rooks
        board[0][0] = new Piece('r', 0, 0, BLACK_PIECE);
        board[0][7] = new Piece('r', 0, 7, BLACK_PIECE);
        //White knight
        board[7][1] = new Piece('n', 7, 1, WHITE_PIECE);
        board[7][6] = new Piece('n', 7, 6, WHITE_PIECE);
        //Black knight
        board[0][1] = new Piece('n', 0, 1, BLACK_PIECE);
        board[0][6] = new Piece('n', 0, 6, BLACK_PIECE);
        //White bishops
        board[7][2] = new Piece('b', 7, 2, WHITE_PIECE);
        board[7][5] = new Piece('b', 7, 5, WHITE_PIECE);
        //Black knight
        board[0][2] = new Piece('b', 0, 2, BLACK_PIECE);
        board[0][5] = new Piece('b', 0, 5, BLACK_PIECE);
        //Queens
        board[7][4] = new Piece('q', 7, 4, WHITE_PIECE);
        board[0][4] = new Piece('q', 0, 4, BLACK_PIECE);
        //Kings
        board[7][3] = new Piece('k', 7, 3, WHITE_PIECE);
        board[0][3] = new Piece('k', 0, 3, BLACK_PIECE);

        //Pawns
        for(int i = 0; i < 8; i++) {
            board[6][i] = new Piece('p', 6, i, WHITE_PIECE);
            board[1][i] = new Piece('p', 1, i, BLACK_PIECE);
        }

        for(Piece[] row : board) {
            for(Piece p : row) {
                if(p != null) {
                    p.update();
                }
            }
        }
    }

    public void keyPressed() {
        if(keyCode == ENTER) {
            board = new Piece[8][8];
            currentPlayer = WHITE_PIECE;
            createBoard();
            loop();
        }
    }

    public void mousePressed() {
        if((mouseX > padding && mouseX < width-padding) && (mouseY > padding && mouseY < height-padding)) {
            boolean showTheMoves = true;
            int x = floor(map(mouseX, padding, width-padding, 0, 8));
            int y = floor(map(mouseY, padding, height-padding, 0, 8));
            if(prevY >= 0 && prevX >= 0 && board[prevY][prevX] != null && (x != prevX || y != prevY)) {
                board[prevY][prevX].showMoves = false;
                if(board[y][x] != null
                        && (board[prevY][prevX].getType() == 'k' || board[prevY][prevX].getType() == 'r')
                        && (board[y][x].getType() == 'k' || board[y][x].getType() == 'r')
                        && (board[y][x].numberOfMoves == 0 && board[prevY][prevX].numberOfMoves == 0)
                        && (board[y][x].getColor() == board[prevY][prevX].getColor())) {
                    int yCord = board[y][x].tileY;
                    int startXCord = -1;
                    int endXCord = -1;
                    boolean valid = false;
                    for(int i = 0; i < 8; i++) {
                        if(i == board[y][x].tileX || i == board[prevY][prevX].tileX) {
                            if(startXCord > -1) {
                                endXCord = i;
                            } else {
                                startXCord = i+1;
                            }
                        }
                    }
                    for(int i = startXCord; i < endXCord; i++) {
                        if(board[yCord][i] == null) valid = true;
                        else valid = false;
                    }
                    if(valid) {
                        char temp = board[y][x].getType();
                        board[y][x].setType(board[prevY][prevX].getType());
                        board[prevY][prevX].setType(temp);
                        showTheMoves = false;
                        currentPlayer = currentPlayer*-1;
                        //Update positions and increase move count
                        board[y][x].update();
                        board[prevY][prevX].update();
                        board[y][x].numberOfMoves++;
                        board[prevY][prevX].numberOfMoves++;
                    }
                } else {
                    for(Integer[] b : board[prevY][prevX].moves) {
                        if(b[0] == x && b[1] == y) {
                            if(board[prevY][prevX].getColor() == currentPlayer) {
                                if(board[y][x] != null && board[y][x].getType() == 'k') {
                                    textAlign(CENTER);
                                    text(currentPlayer == WHITE_PIECE ? "WHITE WINS!" : "BLACK WINS!", (width-200)/2, 10+(height-200)/2);
                                    noLoop();
                                }
                                //Move the piece
                                board[y][x] = board[prevY][prevX];
                                board[y][x].tileX = x;
                                board[y][x].tileY = y;
                                board[y][x].numberOfMoves++;
                                showTheMoves = false;
                                //Remove the piece from the previous tile
                                board[prevY][prevX] = null;
                                //Change player
                                currentPlayer = currentPlayer*-1;
                                if(board[y][x].getType() == 'p' && ((board[y][x].tileY == 7 && board[y][x].getColor() == BLACK_PIECE) || (board[y][x].tileY == 0 && board[y][x].getColor() == WHITE_PIECE))) {
                                    board[y][x].setType('q');
                                    board[y][x].update();
                                }
                                break;
                            }
                        }
                    }
                }
            }
            prevX = x;
            prevY = y;
            if(board[y][x] != null) {
                board[y][x].update();
                if(helpMode) board[y][x].showMoves = showTheMoves;
            }
        }
    }

    public void draw() {
        background(0);
        player = currentPlayer == WHITE_PIECE ? "White player" : "Black player";
        fill(255);
        textSize(60);
        textAlign(CENTER);
        text(player, width/2, 75);
        translate(padding, padding);
        drawBoard();
    }

    void drawBoard() {
        stroke(255);
        strokeWeight(4);
        rect(0, 0, width-200, height-200);
        strokeWeight(1);
        noFill();
        stroke(0);
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if((i+j) % 2 == 0) {
                    fill(255);
                } else {
                    fill(120,120,130);
                }
                rect(2+i*squareSize, 2+j*squareSize, squareSize, squareSize);
            }
        }
        for(Piece[] row : board) {
            for(Piece p : row) {
                if(p != null) {
                    p.showPiece();
                }
            }
        }
        if(prevY >= 0 && prevX >= 0 && board[prevY][prevX] != null) {
            board[prevY][prevX].showPiece();
        }
    }

    class Piece {
        private char display;
        private char type;
        int color;
        int tileX, tileY;
        int numberOfMoves = 0;
        boolean showMoves = false;
        List<Integer[]> moves = new ArrayList<>();

        Piece(char type, int tileY, int tileX, int color) {
            this.color = color;
            this.type = type;
            this.tileX = tileX;
            this.tileY = tileY;
        }

        void setType(char c) {
            this.type = c;
        }

        char getType() {
            return type;
        }

        int getColor() {
            return color;
        }

        void showPiece() {
            fill(0,0,0);
            pushMatrix();
            translate(0, 16);
            translate(squareSize/2, squareSize/2);
            textSize(40);
            textAlign(CENTER);
            text(display, tileX*squareSize, tileY*squareSize);
            popMatrix();
            if(showMoves) {
                for (Integer[] move : moves) {
                    fill(0, 255, 0);
                    stroke(0, 0, 0);
                    strokeWeight(1);
                    pushMatrix();
                    translate(-2 + (width - 2 * padding) / 16, (height - 2 * padding) / 16);
                    ellipse(move[0] * (width - 2 * padding) / 8, move[1] * (height - 2 * padding) / 8, 8, 8);
                    popMatrix();
                }
            }
        }

        void update() {
            moves = new ArrayList<>();
            switch (type) {
                case 'q':
                    moves.addAll(movesLine());
                    moves.addAll(movesDiag());
                    this.display = color == WHITE_PIECE ? '\u2655' : '\u265B';
                    break;

                case 'k':
                    if(moveSquare(tileX+1, tileY) != null)      moves.add(moveSquare(tileX+1, tileY));
                    if(moveSquare(tileX-1, tileY) != null)      moves.add(moveSquare(tileX-1, tileY));
                    if(moveSquare(tileX+1, tileY+1) != null) moves.add(moveSquare(tileX+1, tileY+1));
                    if(moveSquare(tileX+1, tileY-1) != null) moves.add(moveSquare(tileX+1, tileY-1));
                    if(moveSquare(tileX, tileY+1) != null)      moves.add(moveSquare(tileX, tileY+1));
                    if(moveSquare(tileX, tileY-1) != null)      moves.add(moveSquare(tileX, tileY-1));
                    if(moveSquare(tileX-1, tileY-1) != null) moves.add(moveSquare(tileX-1, tileY-1));
                    if(moveSquare(tileX-1, tileY+1) != null) moves.add(moveSquare(tileX-1, tileY+1));
                    this.display = color == WHITE_PIECE ? '\u2654' : '\u265A';
                    break;

                case 'b':
                    moves.addAll(movesDiag());
                    this.display = color == WHITE_PIECE ? '\u2657' : '\u265D';
                    break;

                case 'p':
                    if(color == BLACK_PIECE) {
                        if(moveSquare(tileX, tileY+1) != null) {
                            if(board[tileY+1][tileX] == null) {
                                moves.add(moveSquare(tileX, tileY+1));
                            }
                        }
                        if(tileY == 1 && moveSquare(tileX, tileY+2) != null) {
                            if(board[tileY+2][tileX] == null) {
                                moves.add(moveSquare(tileX, tileY+2));
                            }
                        }
                        //CHECK FOR POSSIBLE DIAGONAL TAKEOUTS
                        if(moveSquare(tileX+1, tileY+1) != null) {
                            if(board[tileY+1][tileX+1] != null && board[tileY+1][tileX+1].getColor() == WHITE_PIECE) {
                                Integer[] a = {tileX+1, tileY+1};
                                moves.add(a);
                            }
                        }
                        if(moveSquare(tileX-1, tileY+1) != null) {
                            if(board[tileY+1][tileX-1] != null && board[tileY+1][tileX-1].getColor() == WHITE_PIECE) {
                                Integer[] a = {tileX-1, tileY+1};
                                moves.add(a);
                            }
                        }
                        this.display = '\u265F';
                    } else {
                        if(moveSquare(tileX, tileY-1) != null) {
                            if(board[tileY-1][tileX] == null) {
                                moves.add(moveSquare(tileX, tileY-1));
                            }
                        }
                        if(tileY == 6 && moveSquare(tileX, tileY-2) != null) {
                            if(board[tileY-2][tileX] == null) {
                                moves.add(moveSquare(tileX, tileY-2));
                            }
                        }
                        //CHECK FOR POSSIBLE DIAGONAL TAKEOUTS
                        if(moveSquare(tileX+1, tileY-1) != null) {
                            if(board[tileY-1][tileX+1] != null && board[tileY-1][tileX+1].getColor() == BLACK_PIECE) {
                                Integer[] a = {tileX+1, tileY-1};
                                moves.add(a);
                            }
                        }
                        if(moveSquare(tileX-1, tileY-1) != null) {
                            if(board[tileY-1][tileX-1] != null && board[tileY-1][tileX-1].getColor() == BLACK_PIECE) {
                                Integer[] a = {tileX-1, tileY-1};
                                moves.add(a);
                            }
                        }
                        this.display = '\u2659';
                    }
                    break;

                case 'r':
                    moves.addAll(movesLine());
                    this.display = color == WHITE_PIECE ? '\u2656' : '\u265C';
                    break;

                case 'n':
                    if(moveSquare(tileX-1, tileY-2) != null) moves.add(moveSquare(tileX-1, tileY-2));
                    if(moveSquare(tileX+1, tileY-2) != null) moves.add(moveSquare(tileX+1, tileY-2));
                    if(moveSquare(tileX-1, tileY+2) != null) moves.add(moveSquare(tileX-1, tileY+2));
                    if(moveSquare(tileX+1, tileY+2) != null) moves.add(moveSquare(tileX+1, tileY+2));

                    if(moveSquare(tileX-2, tileY-1) != null) moves.add(moveSquare(tileX-2, tileY-1));
                    if(moveSquare(tileX-2, tileY+1) != null) moves.add(moveSquare(tileX-2, tileY+1));
                    if(moveSquare(tileX+2, tileY-1) != null) moves.add(moveSquare(tileX+2, tileY-1));
                    if(moveSquare(tileX+2, tileY+1) != null) moves.add(moveSquare(tileX+2, tileY+1));
                    this.display = color == WHITE_PIECE ? '\u2658' : '\u265E';
                    break;
            }
        }

        boolean validMove(int x, int y) {
            if(y >= 0 && y < 8 && x >= 0 && x < 8) {
                if (board[y][x] == null || this.getColor() != board[y][x].getColor()) {
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        }

        Integer[] moveSquare(int x, int y) {
            if(x >= 0 && x < 8 && y >= 0 && y < 8) {
                if(validMove(x, y)) {
                    Integer[] val = {x, y};
                    return val;
                }
            }
            return null;
        }

        List<Integer[]> movesLine() {
            List<Integer[]> val = new ArrayList<>();
            val.addAll(checkLine(1, 0));
            val.addAll(checkLine(-1, 0));
            val.addAll(checkLine(0, 1));
            val.addAll(checkLine(0, -1));
            return val;
        }

        List<Integer[]> movesDiag() {
            List<Integer[]> val = new ArrayList<>();
            val.addAll(checkLine(-1, -1));
            val.addAll(checkLine(+1, -1));
            val.addAll(checkLine(-1, +1));
            val.addAll(checkLine(+1, +1));
            return val;
        }

        List<Integer[]> checkLine(int xInc, int yInc) {
            List<Integer[]> temp = new ArrayList<>();
            int currentX = tileX;
            int currentY = tileY;
            while(currentX + 1*xInc >= 0 && currentY + 1*yInc >= 0 && currentX + 1*xInc < 8 && currentY + 1*yInc < 8) {
                currentX += 1*xInc;
                currentY += 1*yInc;
                if(!validMove(currentX, currentY)) break;
                Integer[] a = {currentX, currentY};
                temp.add(a);
                if(board[currentY][currentX] != null) {
                    if(board[currentY][currentX].getColor() != this.getColor()) break;
                }
            }
            return temp;
        }
    }

    public static void main(String[] args) {
        PApplet.main("Chess.Chess");
    }
}