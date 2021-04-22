package com.mankala.players;

import com.mankala.GameState;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class PlayerMax extends AIPlayer {
    private int searchDepth;

    public PlayerMax(String playerName, int searchDepth) {
        super(playerName);
        this.searchDepth = searchDepth;
    }

    @Override
    public GameState makeMove(HashMap<Integer, GameState> possibleMoves) {
        HashMap<Integer, Integer> evaluationFunctionForMoveOnDepth =  new HashMap<>();
        for(int move : possibleMoves.keySet()){
            evaluationFunctionForMoveOnDepth.put(move, min_max(possibleMoves.get(move), searchDepth, 1));
        }
        int resultMove = Collections.max(evaluationFunctionForMoveOnDepth.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
        return possibleMoves.get(resultMove);
    }
}
