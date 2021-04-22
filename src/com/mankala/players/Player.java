package com.mankala.players;

import com.mankala.GameState;
import sun.net.www.ApplicationLaunchException;

import java.util.HashMap;

public abstract class Player {
    protected String playerName;

    public Player(String playerName){
        this.playerName = playerName;
    }
    public abstract GameState makeMove(HashMap<Integer, GameState> possibleMoves);

    public String getPlayerName() {
        return playerName;
    }
}
