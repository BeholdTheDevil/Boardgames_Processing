import processing.core.PApplet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by anton on 2017-02-05.
 */

public class Sudoku extends PApplet {

    final static int EASY = (int)'E';
    final static int HARD = (int)'H';

    final static int BACKTRACK = -MAX_INT;

    boolean load = false;
    int mode = EASY;
    Tile[][] board = new Tile[9][9];
    float xSize, ySize;
    int currentX, currentY;
    int notFilled = 9*9;
    int sudokuNum = floor(random(1, 50));

    public void settings() {
        size(540, 540);
    }

    public void setup() {
        xSize = floor((width)/9);
        ySize = floor((height)/9);

        startGame();
    }

    //Game start method (also restart)
    void startGame() {
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[0].length; j++) {
                board[i][j] = new Tile(j, i);
            }
        }
        notFilled = 9*9;
        if(load) {
            String[] textGrid = load(new File("/home/anton/private/programs/java/ProcessingExperiments/src/sudoku.txt"), sudokuNum);
            if(textGrid != null) {
                generateFromText(textGrid);
            }
        } else {
            randomBoard();
        }
    }



    //Random board generation
    void randomBoard() {
        background(0);
        genTile(0);
        if(incorrectBoard()) {
            startGame();
        } else {
            removeTiles(floor(random(18, 24)));
        }
    }

    /*Recursive function with backtracking for generating random board tiles
    For some reason sometimes stops generating numbers, temporary fix in function randomBoard checking the board and restarting if it is invalid*/
    int genTile(int count) {
        if(count < 81) {
            int column = count%9;
            int row = (count-column)/9;
            List<Character> possible = new ArrayList<>();
            List<Character> unsolvableFor = new ArrayList<>();
            for(int i = 1; i <= 9; i++) {
                possible.add(Character.forDigit(i,10));
            }

            while(possible.size() != 0) {
                int index = floor(random(0, possible.size()));
                char randnum = possible.get(index);
                if(board[row][column].validInput(randnum) && !unsolvableFor.contains(randnum)) {
                    board[row][column].setCurrent(randnum);
                    if(genTile(count+1) != BACKTRACK) {
                        return 1;
                    } else {
                        possible = new ArrayList<>();
                        for(int i = 1; i <= 9; i++) {
                            possible.add(Character.forDigit(i,10));
                        }
                        unsolvableFor.add(randnum);
                    }
                } else {
                    possible.remove(index);
                }
            }

            return BACKTRACK;
        } else {
            return 1;
        }
    }

    //Checks if the board is valid                  -------------IMPORTANT BUG NEEDS FIXING. SEE FUNCTION genTile(int count)---------------------
    boolean incorrectBoard() {
        for(Tile[] row : board) {
            for(Tile t : row) {
                if(t.getCurrent() == ' ') return true;
            }
        }
        return false;
    }



    //Retrieve board from textfile
    void generateFromText(String[] textGrid) {
        for(int i = 0; i < textGrid.length; i++) {
            for(int j = 0; j < textGrid[i].length(); j++) {
                if(textGrid[i].charAt(j) != '0') {
                    //notFilled--;
                    board[i][j].setCurrent(textGrid[i].charAt(j));
                    board[i][j].setEditable(false);
                }
            }
        }
    }

    //Load the sudoku textfile as a String[]
    String[] load(File f, int n) {
        String gridNum = Integer.toString(n);
        String[] grid = new String[9];
        if(gridNum.length() < 2) gridNum = "0" + gridNum;
        try {
            Scanner scan = new Scanner(f);

            while(scan.hasNext()) {
                if(scan.nextLine().equals("Grid " + gridNum)) {
                    for(int i = 0; i < 9; i++) {
                        grid[i] = scan.nextLine();
                    }
                    return grid;
                }
            }
        } catch (IOException ioe) {
            System.out.println("No sudokufile found");
            ioe.printStackTrace();
            noLoop();
        }
        return null;
    }

    //Remove tiles so that board actually looks like a sudoku
    void removeTiles(int difficulty) {
        int count = 0;
        while(count < notFilled - difficulty) {
            int randX = floor(random(0, 9));
            int randY = floor(random(0, 9));
            board[randY][randX].setCurrent(' ');
            count++;
        }
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                if(board[i][j].getCurrent() != ' ') {
                    board[i][j].setEditable(false);
                }
            }
        }
    }



    //Mouse click handling
    public void mousePressed() {
        currentX = floor(map(mouseX, 0, width, 0, 9));
        currentY = floor(map(mouseY, 0, height, 0, 9));
    }

    //Keyboard input handling
    public void keyPressed() {
        if(key == CODED) {
            switch(keyCode) {
                case LEFT:
                    if(currentX - 1 >= 0) {
                        currentX -= 1;
                    }
                    break;

                case RIGHT:
                    if(currentX + 1 < board[0].length) {
                        currentX += 1;
                    }
                    break;

                case UP:
                    if(currentY - 1 >= 0) {
                        currentY -= 1;
                    }
                    break;

                case DOWN:
                    if(currentY + 1 < board.length) {
                        currentY += 1;
                    }
                    break;
            }
        } else {
            switch (Character.toLowerCase(key)) {
                case 'r':
                    if(load && notFilled == 0) {
                        sudokuNum = floor(random(1, 50));
                    }
                    startGame();
                    break;

                case (char)8:
                    board[currentY][currentX].setCurrent(' ');
                    board[currentY][currentX].setValid(false);
                    break;

                default:
                    try {
                        int num = Integer.parseInt(Character.toString(key));
                        if(board[currentY][currentX].getEditable() && num > 0 && num <= 9) {
                            boolean correct = board[currentY][currentX].validInput(key);
                            board[currentY][currentX].setValid(correct);
                            board[currentY][currentX].setCurrent(key);
                        }
                    } catch (NumberFormatException nfe) {}
                    break;
            }
        }
    }



    //Draw loop
    public void draw() {
        background(0);
        stroke(255);
        noFill();

        drawBoard();

        stroke(0, 180, 0);
        rect(currentX * xSize, currentY * ySize, xSize, ySize);
        if(notFilled == 0) {
            noLoop();
            textAlign(CENTER);
            textSize(80);
            text("You win!", width/2, height/2);
        }
    }

    //Draw board function
    private void drawBoard() {
        notFilled = 9*9;
        for(Tile[] row : board) {
            for(Tile t : row) {
                notFilled -= (t.valid) ? 1:0;
                t.show();
            }
        }
        strokeWeight(4);
        for(int a = 0; a < 3; a++) {
            for(int b = 0; b < 3; b++) {
                rect(b * 3 * xSize, a * 3 * ySize, xSize*3, ySize*3);
            }
        }
    }



    //Tile object
    class Tile {
        int xCord, yCord;
        char current = ' ';
        boolean valid = false;
        boolean editable = true;

        Tile(int xCord, int yCord) {
            this.xCord = xCord;
            this.yCord = yCord;
        }

        public void setEditable(boolean editable) {
            this.editable = editable;
        }

        public boolean getEditable() {
            return editable;
        }

        public void setValid(boolean valid) {
            this.valid = valid;
        }

        public void setCurrent(char current) {
            this.current = current;
        }

        public char getCurrent() {
            return current;
        }

        //Check if character is valid on this tile
        boolean validInput(char key) {
            for(int i = 0; i < board[xCord].length; i++) {
                if((key == board[yCord][i].getCurrent() && i != xCord)|| (key == board[i][xCord].getCurrent() && i != yCord)) {
                    return false;
                }
            }
            for(int i = yCord - (yCord % 3); i < 3 + (yCord - (yCord % 3)); i++) {
                for(int j = xCord - (xCord % 3); j < 3 + (xCord - (xCord % 3)); j++) {
                    if(key == board[i][j].getCurrent() && (i != yCord && j != xCord)) {
                        return false;
                    }
                }
            }
            return true;
        }


        //Show function for tile (for readability in draw loop and drawBoard())
        void show() {
            strokeWeight(1);
            rect(xCord * xSize, yCord * ySize, xSize, ySize);
            fill(255);
            valid = validInput(getCurrent());
            if(!valid && mode == EASY) {
                fill(255, 0, 0);
            }
            if(!editable) fill(128);

            pushMatrix();
            textSize(32);
            textAlign(CENTER);
            translate(0, 10);
            text(current, xCord * xSize + (xSize/2), yCord * ySize + (ySize/2));
            popMatrix();
            noFill();
        }
    }

    public static void main(String[] args) {
        PApplet.main("Sudoku");
    }
}