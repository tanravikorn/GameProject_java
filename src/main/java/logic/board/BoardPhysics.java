package logic.board;

import logic.candy.Candy;

public class BoardPhysics {
    public static void applyGravity(Board board) {
        int rows = board.getRows();
        int cols = board.getCols();

        for (int col = 0; col < cols; col++) {
            applyGravityColumn(board,rows,col);
        }
    }

    public static void applyGravityColumn(Board board,int rows,int col){
        int writeRow = rows - 1;
        for(int r = rows -1; r >=0; r--){
            Candy current = board.getCandy(r,col);
            if(current != null){
                board.setCandy(writeRow,col,current);
                writeRow--;
            }
        }
        while (writeRow >= 0){
            board.setCandy(writeRow,col,null);
            writeRow--;
        }

    }
}
