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

    public GameState(){
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
    }

    public GameState(int [] gameBoard, int startFirstHoles, int endFirstHoles, int firstWell, int startSecondHoles, int endSecondHoles, int secondWell, int numberOfHoles){
        this.gameBoard = gameBoard;
        this.startFirstPlayerHolesIndex = startFirstHoles;
        this.endFirstPlayerHolesIndex = endFirstHoles;
        this.firstPlayerWellIndex = firstWell;
        this.startSecondPlayerHolesIndex = startSecondHoles;
        this.endSecondPlayerHolesIndex = endSecondHoles;
        this.secondPlayerWellIndex = secondWell;
        this.numberOfHoles = numberOfHoles;
    }

    public int countEvaluationFunctionValue(){
        return 0;
    }

    public GameState copyGameState(GameState gameState){
        return new GameState(Arrays.copyOf(gameState.gameBoard, gameBoard.length), gameState.startFirstPlayerHolesIndex,
                gameState.endFirstPlayerHolesIndex, gameState.firstPlayerWellIndex, gameState.startSecondPlayerHolesIndex,
                gameState.endSecondPlayerHolesIndex, gameState.secondPlayerWellIndex, gameState.numberOfHoles);
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
            System.out.println("nastepny ruch bedzie xD");
            if(gameBoard[actualIndex] == 1){
                System.out.println("bicie bedzie!!!!!");
                int oppositeIndex = actualIndex + (numberOfHoles-actualIndex)*2;
                firstPlayerWellIndex += gameBoard[actualIndex] + gameBoard[oppositeIndex];
                gameBoard[actualIndex] = 0;
                gameBoard[oppositeIndex] = 0;
            }
        }
        return newGameState;
    }

    public GameState getGameStateAfterSecondPlayerMove(int holePosition) {
        GameState newGameState = copyGameState(this);
        int actualIndex = placePebbles(holePosition, newGameState, 2);

        if(actualIndex <= endSecondPlayerHolesIndex && actualIndex >= startSecondPlayerHolesIndex) {
            System.out.println("nastepny ruch bedzie xD");
            if(gameBoard[actualIndex] == 1){
                System.out.println("bicie bedzie!!!!!");
                int oppositeIndex = actualIndex - (numberOfHoles-actualIndex)*2;
                secondPlayerWellIndex += gameBoard[actualIndex] + gameBoard[oppositeIndex];
                gameBoard[actualIndex] = 0;
                gameBoard[oppositeIndex] = 0;
            }
        }
        return newGameState;
    }

    public void drawState(){
        System.out.println("\t<--- FIRST PLAYER <---");
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
        System.out.println("\t---> SECOND PLAYER --->");
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

    public int[] getGameBoard() {
        return gameBoard;
    }

    public static void main(String[] args) {
        GameState gs = new GameState();
        gs.drawState();
        GameState gs1 = gs.getGameStateAfterFirstPlayerMove(3);
        gs1.drawState();
        GameState gs2 = gs1.getGameStateAfterFirstPlayerMove(5);
        gs2.drawState();
    }
}
