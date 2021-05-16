package com.mankala.players;

import com.mankala.Game;
import com.mankala.GameState;
import sun.net.www.ApplicationLaunchException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public abstract class Player {
    protected String playerName;

    public Player(String playerName){
        this.playerName = playerName;
    }
    public abstract GameState makeMove(HashMap<Integer, GameState> possibleMoves);
    public GameState makeRandomMove(HashMap<Integer, GameState> possibleMoves){
        //System.out.println("RANDOM MOVE!");
        Random rand = new Random();
        ArrayList<Integer> arr = new ArrayList<>(possibleMoves.keySet());
        return possibleMoves.get(arr.get(rand.nextInt(arr.size())));
    }

    public GameState makeSpecifiedMove(HashMap<Integer, GameState> possibleMoves, int move){
        return possibleMoves.get(move);
    }

    public String getPlayerName() {
        return playerName;
    }
}
