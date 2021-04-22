package com.mankala.players;
import com.mankala.GameState;
import java.util.HashMap;
import java.util.Scanner;

public class HumanPlayer extends Player {
    private Scanner scanner;

    public HumanPlayer(String playerName) {
        super(playerName);
        this.scanner = new Scanner(System.in);
    }

    private int getHoleInput(){
        boolean correctInput = false;
        System.out.println("Twój ruch " + playerName + "!\n");
        System.out.print("Wybierz dołek: ");
        int hole = -1;
        while(!correctInput){
            String holeInput = this.scanner.nextLine();
            try{
                hole = Integer.parseInt(holeInput);
                if(hole >= 1 && hole <= 6)
                    correctInput = true;
                else
                    System.out.println("Możesz wybrać dołek od 1 do 6");
            }
            catch (Exception e){
                System.out.println("Podaj numer dołka od 1 do 6");
            }
        }
        return hole;
    }

    @Override
    public GameState makeMove(HashMap<Integer, GameState> possibleMoves) {
        int hole = getHoleInput();
        return  possibleMoves.get(hole);
    }
}
