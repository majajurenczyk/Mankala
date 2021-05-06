package com.mankala.players;

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
        Random rand = new Random();
        ArrayList<Integer> arr = new ArrayList<>(possibleMoves.keySet());
        return possibleMoves.get(arr.get(rand.nextInt(arr.size())));
    }
    public String getPlayerName() {
        return playerName;
    }
}
