package com.mankala;
import com.mankala.players.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Game {
    private Player firstPlayer;
    private Player secondPlayer;

    private Player actualPlayer;
    private GameState actualGameState;

    private HashMap<Integer, GameState> nextPossibleStatesByMove;

    private static final int MIN_MAX = 1;
    private static final int ALPHA_BETA = 2;
    private static final int HEURISTIC_SUB_WINNER = 1;
    private static final int HEURISTIC_SUB_CAPTURE = 2;
    private static final int HEURISTIC_SUB_NEXT_MOVE = 3;


    public Game(Player firstPlayer, Player secondPlayer){
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        nextPossibleStatesByMove = new HashMap<>();
    }

    public void drawStartPlayer(){
        /*Random random = new Random();
        int result = random.nextInt(2);
        if(result == 0)
            actualPlayer = firstPlayer;
        else
            actualPlayer = secondPlayer;*/
        //actualPlayer = firstPlayer;
        actualPlayer = secondPlayer;
    }

    public Player getFirstPlayer(){
        return firstPlayer;
    }

    public Player getSecondPlayer(){
        return secondPlayer;
    }

    public void changePlayer(){
        if(actualPlayer.equals(firstPlayer) && !actualGameState.isFirstNextMove()){
            actualPlayer = secondPlayer;
        }
        else if(actualPlayer.equals(secondPlayer) && !actualGameState.isSecondNextMove()){
            actualPlayer = firstPlayer;
        }
    }

    public void startGame(int numberOfHoles, int numberOfPebbles){
        drawStartPlayer();
        actualGameState = new GameState(firstPlayer.getPlayerName(), secondPlayer.getPlayerName(),numberOfHoles, numberOfPebbles);
        updateNextPossibleStatesByMove();
    }

    public void updateNextPossibleStatesByMove(){
        if(actualPlayer.equals(firstPlayer)){
            nextPossibleStatesByMove = actualGameState.getPossibleNextStatesForFirstPlayer();
        }
        else if(actualPlayer.equals(secondPlayer))
            nextPossibleStatesByMove = actualGameState.getPossibleNextStatesForSecondPlayer();
    }

    public Player gameLoop(int hole){
        startGame(6, 6);
        boolean start = true;
        //System.out.println("\nGra rozpoczęta!\n");
        while (/*!actualGameState.isOver()*/ !((actualPlayer == firstPlayer && actualGameState.isFirstOver()) || (actualPlayer == secondPlayer && actualGameState.isSecondOver()))){
            //actualGameState.drawState();
            if(start && actualPlayer instanceof AIPlayer && ((AIPlayer) actualPlayer).isRandomMove()) {
                //actualGameState = actualPlayer.makeRandomMove(nextPossibleStatesByMove);
                actualGameState = actualPlayer.makeSpecifiedMove(nextPossibleStatesByMove, hole);
                start = false;
            }
            else{
                actualGameState = actualPlayer.makeMove(nextPossibleStatesByMove);
            }
            changePlayer();
            updateNextPossibleStatesByMove();
        }

        //actualGameState.drawState();

        //System.out.println("\nFINAL RESULT\n");

        //actualGameState.drawState();

        if(actualGameState.isFirstWinner()){
            //System.out.println("\nWygrał " + firstPlayer.getPlayerName() + "!\n");
            return firstPlayer;
        }
        if(actualGameState.isSecondWinner()){
            //System.out.println("\nWygrał " + secondPlayer.getPlayerName() + "!\n");
            return secondPlayer;
        }
        else{
            //System.out.println("\nDRAW!\n");
            return firstPlayer;
        }
    }

    public static void main(String[] args) {
        int first = 0;
        int second = 0;
        ArrayList<Integer> firstMove = new ArrayList<>();
        ArrayList<Integer> secondMove = new ArrayList<>();

        for(int i = 1; i <= 6; i++){
            //firstMove.clear();
            //secondMove.clear();
            Game game = new Game(new PlayerMin("min", 2, true, ALPHA_BETA, HEURISTIC_SUB_NEXT_MOVE),
                    new PlayerMax("max", 7, true, ALPHA_BETA, HEURISTIC_SUB_NEXT_MOVE));
            Player winner = game.gameLoop(i);
            if(winner.equals(game.firstPlayer)) {
                first++;
                firstMove.add(((AIPlayer)winner).getMoveTimes().size());
            }
            else {
                second++;
                secondMove.add(((AIPlayer)winner).getMoveTimes().size());
            }
        }
        double avgF = (double)(firstMove.stream().mapToInt(Integer::intValue).sum()) / firstMove.size();
        double avgS = (double)(secondMove.stream().mapToInt(Integer::intValue).sum()) / secondMove.size();

        System.out.println(first + "\t"+ Double.toString(avgF).replace(".", ","));
        System.out.println(second + "\t"+ Double.toString(avgS).replace(".", ","));
    }
}
