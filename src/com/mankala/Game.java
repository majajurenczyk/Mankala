package com.mankala;
import com.mankala.players.HumanPlayer;
import com.mankala.players.Player;
import java.util.HashMap;
import java.util.Random;
import java.util.stream.IntStream;

public class Game {
    private Player firstPlayer;
    private Player secondPlayer;

    private Player actualPlayer;
    private GameState actualGameState;

    private HashMap<Integer, GameState> nextPossibleStatesByMove;

    public Game(Player firstPlayer, Player secondPlayer){
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        nextPossibleStatesByMove = new HashMap<>();
    }

    public void drawStartPlayer(){
        Random random = new Random();
        int result = random.nextInt(2);
        if(result == 0)
            actualPlayer = firstPlayer;
        else
            actualPlayer = secondPlayer;
    }

    public void changePlayer(){
        if(actualPlayer.equals(firstPlayer) && !actualGameState.isFirstNextMove()){
            actualPlayer = secondPlayer;
        }
        else if(actualPlayer.equals(secondPlayer) && !actualGameState.isSecondNextMove()){
            actualPlayer = firstPlayer;
        }
    }

    public void startGame(){
        drawStartPlayer();
        actualGameState = new GameState(firstPlayer.getPlayerName(), secondPlayer.getPlayerName());
        updateNextPossibleStatesByMove();
    }

    public void updateNextPossibleStatesByMove(){
        if(actualPlayer.equals(firstPlayer)){
            IntStream.range(1, 7).forEach(hole -> nextPossibleStatesByMove.put(hole, actualGameState.getGameStateAfterFirstPlayerMove(hole)));
        }
        else if(actualPlayer.equals(secondPlayer))
            IntStream.range(1, 7).forEach(hole -> nextPossibleStatesByMove.put(hole, actualGameState.getGameStateAfterSecondPlayerMove(hole)));
    }

    public Player gameLoop(){
        startGame();
        System.out.println("\nGra rozpoczęta!\n");
        while (!actualGameState.isOver()){
            actualGameState.drawState();
            actualGameState = actualPlayer.makeMove(nextPossibleStatesByMove);
            changePlayer();
            updateNextPossibleStatesByMove();
        }
        if(actualGameState.isFirstWinner()){
            System.out.println("\nWygrał" + firstPlayer.getPlayerName() + "!\n");
            return firstPlayer;
        }
        if(actualGameState.isSecondWinner()){
            System.out.println("\nWygrał" + secondPlayer.getPlayerName() + "!\n");
            return secondPlayer;
        }
        else{
            System.out.println("\nerror\n");
            return null;
        }
    }

    public static void main(String[] args) {
        Game game = new Game(new HumanPlayer("Maja1"), new HumanPlayer("Maja2"));
        game.gameLoop();
    }
}
