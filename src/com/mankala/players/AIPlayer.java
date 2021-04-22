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
        if(player == 1){ //MINIMIZING PLAYER
            int funcValueMax = Integer.MIN_VALUE;
            HashMap<Integer, GameState> children = node.getPossibleNextStatesForFirstPlayer();
            for(int move : children.keySet()){
                funcValueMax = Math.max(funcValueMax, min_max(children.get(move), depth - 1, 2));
            }
            return funcValueMax;
        }
        else{ //MAXIMIZING PLAYER
            int funcValueMin = Integer.MAX_VALUE;
            HashMap<Integer, GameState> children = node.getPossibleNextStatesForFirstPlayer();
            for(int move : children.keySet()){
                funcValueMin = Math.min(funcValueMin, min_max(children.get(move), depth - 1, 1));
            }
            return  funcValueMin;
        }
    }
}
