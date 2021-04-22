package com.mankala;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
        return gameBoard[secondPlayerWellIndex] + 10 * (isSecondCapture ? 1 : 0) + 3 * (isSecondNextMove ? 1 : 0)
                + (isSecondWinner ? 1 : 0);
    }

    public GameState copyGameState(GameState gameState){
        GameState newGameState = new GameState(gameState.firstPlayerName, gameState.secondPlayerName);
        newGameState.gameBoard = Arrays.copyOf(gameState.gameBoard, gameBoard.length);
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
        if(player == 1)
            startIndex = getIndexByPositionForFirstPlayer(holePosition);
        else if(player == 2)
            startIndex = getIndexByPositionForSecondPlayer(holePosition);
        else
            return -1;

        int numberOfPebbles = gameState.gameBoard[startIndex];
        int actualIndex = startIndex;
        gameState.gameBoard[actualIndex++] = 0;
        while(numberOfPebbles != 0){
            if(actualIndex == gameState.gameBoard.length)
                actualIndex = 0;
            if(actualIndex != gameState.secondPlayerWellIndex){
                gameState.gameBoard[actualIndex]++;
                numberOfPebbles--;
            }
            actualIndex++;
        }
        return actualIndex;
    }

    public GameState getGameStateAfterFirstPlayerMove(int holePosition) {
        GameState newGameState = copyGameState(this);
        int actualIndex = placePebbles(holePosition, newGameState, 1);

        if(actualIndex <= endFirstPlayerHolesIndex && actualIndex >= startFirstPlayerHolesIndex) {
            newGameState.isFirstNextMove = true;
            if(gameBoard[actualIndex] == 1){
                newGameState.isFirstCapture = true;
                int oppositeIndex = actualIndex + (numberOfHoles-actualIndex)*2;
                firstPlayerWellIndex += gameBoard[actualIndex] + gameBoard[oppositeIndex];
                gameBoard[actualIndex] = 0;
                gameBoard[oppositeIndex] = 0;
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

    public GameState getGameStateAfterSecondPlayerMove(int holePosition) {
        GameState newGameState = copyGameState(this);
        int actualIndex = placePebbles(holePosition, newGameState, 2);

        if(actualIndex <= endSecondPlayerHolesIndex && actualIndex >= startSecondPlayerHolesIndex) {
            isSecondNextMove = true;
            if(gameBoard[actualIndex] == 1){
                isSecondCapture = true;
                int oppositeIndex = actualIndex - (numberOfHoles-actualIndex)*2;
                secondPlayerWellIndex += gameBoard[actualIndex] + gameBoard[oppositeIndex];
                gameBoard[actualIndex] = 0;
                gameBoard[oppositeIndex] = 0;
            }
        }
        return newGameState;
    }

    public void drawState(){
        System.out.println("\t<---" + firstPlayerName + "<---");
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
        System.out.println("\t--->" + secondPlayerName + "--->");
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

    public int[] getGameBoard() {
        return gameBoard;
    }

    public int getStartFirstPlayerHolesIndex() {
        return startFirstPlayerHolesIndex;
    }

    public int getEndFirstPlayerHolesIndex() {
        return endFirstPlayerHolesIndex;
    }

    public int getFirstPlayerWellIndex() {
        return firstPlayerWellIndex;
    }

    public int getStartSecondPlayerHolesIndex() {
        return startSecondPlayerHolesIndex;
    }

    public int getEndSecondPlayerHolesIndex() {
        return endSecondPlayerHolesIndex;
    }

    public int getSecondPlayerWellIndex() {
        return secondPlayerWellIndex;
    }

    public int getNumberOfHoles() {
        return numberOfHoles;
    }

    public boolean isSecondCapture() {
        return isSecondCapture;
    }

    public boolean isSecondNextMove() {
        return isSecondNextMove;
    }

    public boolean isFirstCapture() {
        return isFirstCapture;
    }

    public boolean isFirstNextMove() {
        return isFirstNextMove;
    }
}
