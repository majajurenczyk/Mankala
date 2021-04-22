package com.mankala.players;

import com.mankala.GameState;

import java.util.HashMap;

public abstract class AIPlayer extends Player {
    public AIPlayer(String playerName) {
        super(playerName);
    }

    public int min_max(GameState node, int depth, int player){ //0 - max player, 1 - min player
        if(depth == 0)
            return node.countEvaluationFunctionValue();
        int value;
        if(player == 1){ //MINIMIZING PLAYER
            value = Integer.MAX_VALUE;
            HashMap<Integer, GameState> children = node.getPossibleNextStatesForFirstPlayer();
            for(int move : children.keySet()){
                value = Math.min(value, min_max(children.get(move), depth - 1, 2));
            }
            return value;
        }
        else{ //MAXIMIZING PLAYER
            value = Integer.MIN_VALUE;
            HashMap<Integer, GameState> children = node.getPossibleNextStatesForSecondPlayer();
            for(int move : children.keySet()){
                value = Math.max(value, min_max(children.get(move), depth - 1, 1));
            }
            return value;
        }
    }
}
