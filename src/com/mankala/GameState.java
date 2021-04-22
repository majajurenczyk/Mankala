package com.mankala;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.stream.IntStream;

public class GameState {
    private int [] gameBoard;
    private int startFirstPlayerHolesIndex;
    private int endFirstPlayerHolesIndex;
    private int firstPlayerWellIndex;
    private int startSecondPlayerHolesIndex;
    private int endSecondPlayerHolesIndex;
    private int secondPlayerWellIndex;

    private int numberOfHoles;
    private boolean isSecondCapture;
    private boolean isSecondNextMove;
    private boolean isFirstCapture;
    private boolean isFirstNextMove;

    private boolean isFirstWinner;
    private boolean isSecondWinner;

    private String firstPlayerName;
    private String secondPlayerName;

    public GameState(String firstPlayerName, String secondPlayerName){
        gameBoard = new int [14];
        Arrays.fill(gameBoard, 4);
        gameBoard[6] = 0;
        gameBoard[13] = 0;

        numberOfHoles = 6;
        this.startFirstPlayerHolesIndex = 0;
        this.endFirstPlayerHolesIndex = 5;
        this.firstPlayerWellIndex = 6;
        this.startSecondPlayerHolesIndex = 7;
        this.endSecondPlayerHolesIndex = 12;
        this.secondPlayerWellIndex = 13;

        this.isSecondCapture = false;
        this.isSecondNextMove = false;
        this.isFirstCapture = false;
        this.isFirstNextMove = false;

        this.isFirstWinner = false;
        this.isSecondWinner = false;

        this.firstPlayerName = firstPlayerName;
        this.secondPlayerName = secondPlayerName;

    }

    public int countEvaluationFunctionValue(){
/*        return gameBoard[secondPlayerWellIndex] + 10 * (isSecondCapture ? 1 : 0) + 3 * (isSecondNextMove ? 1 : 0)
                + 20 * (isSecondWinner ? 1 : 0) + 5*(gameBoard[secondPlayerWellIndex] - gameBoard[firstPlayerWellIndex]);*/
    return gameBoard[secondPlayerWellIndex] - gameBoard[firstPlayerWellIndex];
    }

    public HashMap<Integer, GameState> getPossibleNextStatesForFirstPlayer(){
        HashMap<Integer, GameState> resultStatesByMove = new HashMap<>();
        IntStream.range(1, 7).filter(index -> gameBoard[getIndexByPositionForFirstPlayer(index)] > 0).forEach(hole -> resultStatesByMove.put(hole, this.getGameStateAfterFirstPlayerMove(hole)));
        return resultStatesByMove;
    }

    public HashMap<Integer, GameState> getPossibleNextStatesForSecondPlayer(){
        HashMap<Integer, GameState> resultStatesByMove = new HashMap<>();
        IntStream.range(1, 7).filter(index -> gameBoard[getIndexByPositionForSecondPlayer(index)] > 0).forEach(hole -> resultStatesByMove.put(hole, this.getGameStateAfterSecondPlayerMove(hole)));
        return resultStatesByMove;
    }

    public GameState copyGameState(GameState gameState){
        GameState newGameState = new GameState(gameState.firstPlayerName, gameState.secondPlayerName);
        int [] newBoard = new int[gameState.gameBoard.length];
        System.arraycopy(gameState.gameBoard, 0, newBoard, 0, gameState.gameBoard.length);
        newGameState.gameBoard = newBoard;
        newGameState.startFirstPlayerHolesIndex = gameState.startFirstPlayerHolesIndex;
        newGameState.endFirstPlayerHolesIndex = gameState.endFirstPlayerHolesIndex;
        newGameState.firstPlayerWellIndex = gameState.firstPlayerWellIndex;
        newGameState.startSecondPlayerHolesIndex = gameState.startSecondPlayerHolesIndex;
        newGameState.endSecondPlayerHolesIndex = gameState.endSecondPlayerHolesIndex;
        newGameState.secondPlayerWellIndex = gameState.secondPlayerWellIndex;
        newGameState.numberOfHoles = gameState.numberOfHoles;
        newGameState.isSecondCapture = false;
        newGameState.isSecondNextMove = false;
        newGameState.isFirstCapture = false;
        newGameState.isFirstNextMove = false;
        newGameState.isFirstWinner = false;
        newGameState.isSecondWinner = false;
        return newGameState;
    }

    private int placePebbles(int holePosition, GameState gameState, int player){
        int startIndex;
        int actualPlayerOpponentWellIndex;

        if(player == 1) {
            startIndex = getIndexByPositionForFirstPlayer(holePosition);
            actualPlayerOpponentWellIndex = gameState.secondPlayerWellIndex;
        }
        else if(player == 2) {
            startIndex = getIndexByPositionForSecondPlayer(holePosition);
            actualPlayerOpponentWellIndex = gameState.firstPlayerWellIndex;
        }
        else
            return -1;

        int numberOfPebbles = gameState.gameBoard[startIndex];

        int actualIndex = startIndex;
        gameState.gameBoard[actualIndex] = 0;
        actualIndex++;

        while(numberOfPebbles != 0){
            if(actualIndex == gameState.gameBoard.length)
                actualIndex = 0;
            if(actualIndex != actualPlayerOpponentWellIndex){
                gameState.gameBoard[actualIndex]++;
                numberOfPebbles--;
            }
            if(numberOfPebbles != 0)
                actualIndex++;
        }
        return actualIndex;
    }

    public GameState getGameStateAfterFirstPlayerMove(int holePosition) {
        GameState newGameState = copyGameState(this);
        if(newGameState.gameBoard[getIndexByPositionForFirstPlayer(holePosition)] == 0) {
            newGameState.isFirstNextMove = true;
            return newGameState;
        }

        int actualIndex = placePebbles(holePosition, newGameState, 1);

        if(actualIndex <= newGameState.endFirstPlayerHolesIndex && actualIndex >= newGameState.startFirstPlayerHolesIndex) {
            newGameState.isFirstNextMove = true;
            int oppositeIndex = 12 - actualIndex;
            if(newGameState.gameBoard[actualIndex] == 1 && newGameState.gameBoard[oppositeIndex] > 0){
                newGameState.isFirstCapture = true;
                newGameState.gameBoard[newGameState.firstPlayerWellIndex] += (newGameState.gameBoard[actualIndex] + newGameState.gameBoard[oppositeIndex]);
                newGameState.gameBoard[actualIndex] = 0;
                newGameState.gameBoard[oppositeIndex] = 0;
            }
        }
        return newGameState;
    }

    public GameState getGameStateAfterSecondPlayerMove(int holePosition) {
        GameState newGameState = copyGameState(this);
        if(newGameState.gameBoard[getIndexByPositionForSecondPlayer(holePosition)] == 0) {
            newGameState.isSecondNextMove = true;
            return newGameState;
        }

        int actualIndex = placePebbles(holePosition, newGameState, 2);

        if(actualIndex <= newGameState.endSecondPlayerHolesIndex && actualIndex >= newGameState.startSecondPlayerHolesIndex) {
            newGameState.isSecondNextMove = true;
            int oppositeIndex = 12 - actualIndex;
            if(newGameState.gameBoard[actualIndex] == 1 && newGameState.gameBoard[oppositeIndex] > 0){
                newGameState.isSecondCapture = true;
                newGameState.gameBoard[newGameState.secondPlayerWellIndex] += (newGameState.gameBoard[actualIndex] + newGameState.gameBoard[oppositeIndex]);
                newGameState.gameBoard[actualIndex] = 0;
                newGameState.gameBoard[oppositeIndex] = 0;
            }
        }
        return newGameState;
    }

    public boolean isOver(){
        boolean firstEnd = IntStream.range(startFirstPlayerHolesIndex, endFirstPlayerHolesIndex+1).allMatch(index -> gameBoard[index] == 0);
        boolean secondEnd = IntStream.range(startSecondPlayerHolesIndex, endSecondPlayerHolesIndex+1).allMatch(index -> gameBoard[index] == 0);
        return  firstEnd || secondEnd;
    }

    public boolean isFirstWinner(){
        return isOver() &&  gameBoard[firstPlayerWellIndex] > gameBoard[secondPlayerWellIndex];
    }

    public  boolean isSecondWinner(){
        return isOver() &&  gameBoard[secondPlayerWellIndex] > gameBoard[firstPlayerWellIndex];
    }


    public void drawState(){
        System.out.println("\t\t<---" + firstPlayerName + "<---");
        System.out.println();
        System.out.print("\t");
        IntStream.range(1, numberOfHoles+1).forEach(index -> System.out.print(numberOfHoles - index + 1 + "\t"));
        System.out.println();
        System.out.print("\t");
        IntStream.range(startFirstPlayerHolesIndex, endFirstPlayerHolesIndex+1).boxed().sorted(Collections.reverseOrder()).forEach(index -> System.out.print(gameBoard[index] + "\t"));
        System.out.print("\n" + gameBoard[firstPlayerWellIndex] + "\t\t\t\t\t\t\t" + gameBoard[secondPlayerWellIndex] + "\n");
        System.out.print("\t");
        IntStream.range(startSecondPlayerHolesIndex, endSecondPlayerHolesIndex+1).forEach(index -> System.out.print(gameBoard[index] + "\t"));
        System.out.println();
        System.out.print("\t");
        IntStream.range(1, numberOfHoles+1).forEach(index -> System.out.print(index + "\t"));
        System.out.println("\n");
        System.out.println("\t\t--->" + secondPlayerName + "--->");
    }

    public int getIndexByPositionForFirstPlayer(int holePosition){
        if(holePosition < 1 || holePosition > numberOfHoles)
            return -1;
        return holePosition - 1;
    }

    public int getIndexByPositionForSecondPlayer(int holePosition){
        if(holePosition < 1 || holePosition > numberOfHoles)
            return -1;
        return firstPlayerWellIndex + holePosition;
    }

    //GETTERS

    public boolean isSecondNextMove() {
        return isSecondNextMove;
    }

    public boolean isFirstNextMove() {
        return isFirstNextMove;
    }
}
