package logic.board;

import logic.candy.Candy;

public class Board {
    private int rows;
    private int cols;
    private Candy[][] grid;

    public Board(int rows, int cols){
        this.rows = rows;
        this.cols = cols;
        this.grid = new Candy[rows][cols];

    }

    public boolean isValid(int r, int c){
        return (r >= 0 && r < this.rows) && (c >= 0 && c < this.cols);
    }

    public Candy getCandy(int r, int c){
        if(isValid(r,c)){
            return grid[r][c];
        }
        return null;
    }
    public void setCandy(int r, int c, Candy candy){
        if(candy != null && isValid(r,c)){
            this.grid[r][c] = candy;
            candy.setRow(r);
            candy.setColumn(c);
        }
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Candy[][] getGrid() {
        return grid;
    }
}
