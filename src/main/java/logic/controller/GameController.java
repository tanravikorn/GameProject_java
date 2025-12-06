package logic.controller;

import logic.board.Board;
import logic.board.BoardInitializer;
import logic.board.BoardUpdater;
import logic.candy.Candy;
import logic.explosion.ExplosionStrategy;
import logic.utils.CandyMixer;
import logic.utils.MatchFinder;
import logic.utils.MatchProcessor;
import logic.utils.Point;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameController {
    private Board board;
    private MatchFinder matchFinder;
    private GameState gameState;

    private int score;
    private int moveLeft;

    public GameController(int rows, int cols){
        this.board = new Board(rows, cols);
        this.matchFinder = new MatchFinder(board);

        initializeGame();
    }

    public void initializeGame(){
        BoardInitializer.initialize(board, matchFinder);
        this.score = 0;
        this.gameState = GameState.PLAY;

    }

    public void handleSwap(int r1, int c1, int r2,int c2){
        if(gameState != GameState.PLAY) return;

        if(!board.isValid(r1,c1) || !board.isValid(r2,c2)) return;

        Candy candy1 = board.getCandy(r1,c1);
        Candy candy2 = board.getCandy(r2,c2);
        this.gameState = GameState.PROCESS;
        performSwap(r1,c1,r2,c2);
        boolean canSwap = processTurn(candy1,candy2);


        if(!canSwap){
            performSwap(r1,c1,r2,c2);
        }else{
            endTurn();
        }

        this.gameState = GameState.PLAY;
    }

    private void performSwap(int r1,int c1,int r2,int c2){
        Candy candy1 = board.getCandy(r1,c1);
        Candy candy2 = board.getCandy(r2,c2);

        board.setCandy(r1,c1,candy2);
        board.setCandy(r2,c2,candy1);
    }


    private boolean processTurn(Candy c1, Candy c2){
        boolean event = false;
        Set<Point> removes = new HashSet<>();
        ExplosionStrategy combo = CandyMixer.getComboExplosion(c1,c2);
        if(combo != null){
            combo.explode(board,c2.getRow(),c2.getColumn(), removes);
            removes.add(new Point(c1.getRow(),c1.getColumn()));
            event = true;
        }else{
            List<Set<Candy>> matches = new ArrayList<>();
            matches.addAll(matchFinder.findMatchAt(c1.getRow(), c1.getColumn()));
            matches.addAll(matchFinder.findMatchAt(c2.getRow(), c2.getColumn()));
            if(!matches.isEmpty()){
                Set<Point> matchesPoint = MatchProcessor.processMatches(matches,c2);
                removes.addAll(matchesPoint);
                event = true;
            }
        }

        if(removes.isEmpty()) return false;

        while(!removes.isEmpty()){
            score += removes.size() * 100;
            BoardUpdater.updateBoard(board, removes);
            removes.clear();
            List<Set<Candy>> chainMatches = matchFinder.findAllMatches();
            if(!chainMatches.isEmpty()){
                Set<Point> chainDeaths = MatchProcessor.processMatches(chainMatches, null);
                removes.addAll(chainDeaths);
            }
        }
        return true;
    }

    private void endTurn(){
        if(!matchFinder.hasPotentialMatch()){
            do{
                BoardUpdater.shuffleBoard(board);
            }while(!matchFinder.hasPotentialMatch());
        }
    }

    public int getMoveLeft() {
        return moveLeft;
    }
    public Board getBoard() {
        return board;
    }
    public GameState getGameState() {
        return gameState;
    }
    public int getScore() {
        return score;
    }
}
