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
import logic.Item.*;
import java.util.*;

public class GameController {
    private Board board;
    private MatchFinder matchFinder;
    private GameState gameState;
    private GameMode gameMode;
    private int score;
    private int moveLeft;
    private Random random = new Random();
    private int bombItemAmount;
    private int iceItemAmount;
    private int stripedItemAmount;

    public GameController(int rows, int cols, GameMode mode){
        this.board = new Board(rows, cols);
        this.matchFinder = new MatchFinder(board);
        this.gameMode = mode;
        initializeGame();
    }

    public void initializeGame(){
        BoardInitializer.initialize(board, matchFinder, gameMode);
        this.score = 0;
        this.gameState = GameState.PLAY;
        setMoveLeft(gameMode == GameMode.HARD ? 15:20);
        this.bombItemAmount = 2;
        this.iceItemAmount = 1;
        this.stripedItemAmount = 2;
    }

    public Set<Point> handleSwap(int r1, int c1, int r2,int c2){
        if(gameState != GameState.PLAY) return new HashSet<>();

        if(!board.isValid(r1,c1) || !board.isValid(r2,c2)) return new HashSet<>();

        Candy candy1 = board.getCandy(r1,c1);
        Candy candy2 = board.getCandy(r2,c2);
        if(candy1 != null && candy1.isFrozen() || candy2 != null && candy2.isFrozen()){
            return new HashSet<>();
        }
        performSwap(r1,c1,r2,c2);
        Set<Point> removes = new HashSet<>();
        ExplosionStrategy combo = CandyMixer.getComboExplosion(candy1, candy2);

        if (combo != null) {
            combo.explode(board, candy2.getRow(), candy2.getColumn(), removes);
            removes.add(new Point(candy1.getRow(), candy1.getColumn()));
        } else {
            List<Set<Candy>> matches = new ArrayList<>();
            matches.addAll(matchFinder.findMatchAt(r1, c1));
            matches.addAll(matchFinder.findMatchAt(r2, c2));

            if (!matches.isEmpty()) {
                removes.addAll(MatchProcessor.processMatches(board,matches, candy2));
            }
        }
        if (removes.isEmpty()) {
            performSwap(r1, c1, r2, c2);
        } else {
            score += removes.size() * 100;
            setMoveLeft(moveLeft-1);
        }
        if(moveLeft <= 0) {
            gameState = GameState.GAME_OVER;
        }
        return removes;
    }

    private void performSwap(int r1,int c1,int r2,int c2){
        Candy candy1 = board.getCandy(r1,c1);
        Candy candy2 = board.getCandy(r2,c2);

        board.setCandy(r1,c1,candy2);
        board.setCandy(r2,c2,candy1);
    }

    public void applyPhysics(Set<Point> removes) {
        BoardUpdater.updateBoard(board, removes);
    }
    public Set<Point> checkChainReaction() {
        Set<Point> newRemoves = new HashSet<>();
        List<Set<Candy>> matches = matchFinder.findAllMatches();

        if (!matches.isEmpty()) {
            newRemoves.addAll(MatchProcessor.processMatches(board,matches, null));
            score += newRemoves.size() * 100;
        }
        return newRemoves;
    }

    public void endTurn(){
        if(!matchFinder.hasPotentialMatch()){
            do{
                BoardUpdater.shuffleBoard(board);
            }while(!matchFinder.hasPotentialMatch());
        }
    }

    public List<Point> applyItemTransform(Item item){
        if(gameState != GameState.PLAY) return new ArrayList<>();
        boolean canUse = true;
        if(item != null){
            if(item instanceof BombItem && bombItemAmount> 0){
                bombItemAmount--;
            } else if(item instanceof IceBreakItem && iceItemAmount >0){
                iceItemAmount--;
            } else if (item instanceof StripedItem && stripedItemAmount > 0) {
                stripedItemAmount--;
            }else{
                canUse = false;
            }
        }else{
            return new ArrayList<>();
        }
        if(canUse){
            return item.use(board,gameMode);
        }
        else return new ArrayList<>();
    }
    public Set<Point> activateItems(List<Point> target){
        Set<Point> removes = new HashSet<>();
        for(Point p : target){
            Candy candy = board.getCandy(p.r,p.c);
            if(candy != null){
                candy.performExplosion(board, removes);
                score += 500;
            }
        }
        return removes;
    }
    public void setMoveLeft(int moveLeft){
        this.moveLeft = Math.max(0,moveLeft);
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
    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }
    public int getBombItemAmount() {
        return bombItemAmount;
    }

    public int getIceItemAmount() {
        return iceItemAmount;
    }

    public int getStripedItemAmount() {
        return stripedItemAmount;
    }
}
