package com.mankala.players;

import com.mankala.GameState;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class PlayerMax extends AIPlayer {
    private int searchDepth;
    private int mode;

    public PlayerMax(String playerName, int searchDepth, boolean isRandom,  int mode) {
        super(playerName, isRandom);
        this.searchDepth = searchDepth;
        this.mode = mode;
    }

    @Override
    public GameState makeMove(HashMap<Integer, GameState> possibleMoves) {
        System.out.print("\nPLAYER MAX MOVE : ");
        HashMap<Integer, Integer> evaluationFunctionForMoveOnDepth =  new HashMap<>();
        for(int move : possibleMoves.keySet()){
            if(mode == 1)
                evaluationFunctionForMoveOnDepth.put(move, min_max(possibleMoves.get(move), searchDepth, !possibleMoves.get(move).isSecondNextMove()));
            else if(mode == 2)
                evaluationFunctionForMoveOnDepth.put(move, min_max_ab(possibleMoves.get(move), searchDepth, !possibleMoves.get(move).isSecondNextMove(), Integer.MIN_VALUE, Integer.MAX_VALUE));
        }
        int resultMove = Collections.max(evaluationFunctionForMoveOnDepth.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
        System.out.print("hole " + resultMove + " chosen.\n");
        return possibleMoves.get(resultMove);
    }
}
