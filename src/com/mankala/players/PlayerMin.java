package com.mankala.players;
import com.mankala.GameState;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class PlayerMin extends AIPlayer{
    private int searchDepth;
    private int mode;
    private int heuristic;

    public PlayerMin(String playerName, int searchDepth, boolean isRandom, int mode, int heuristic) {
        super(playerName, isRandom);
        this.searchDepth = searchDepth;
        this.mode = mode;
        this.heuristic = heuristic;
    }

    @Override
    public GameState makeMove(HashMap<Integer, GameState> possibleMoves) {
        //System.out.print("\nPLAYER MIN MOVE : ");
        HashMap<Integer, Integer> evaluationFunctionForMoveOnDepth =  new HashMap<>();

        long start = 0;
        long end = 0;

        for(int move : possibleMoves.keySet()){
            if(mode == 1) {
                start = System.nanoTime();
                evaluationFunctionForMoveOnDepth.put(move, min_max(possibleMoves.get(move), searchDepth, possibleMoves.get(move).isFirstNextMove(), this.heuristic));
                end = System.nanoTime();
            }
            else if(mode == 2) {
                start = System.nanoTime();
                evaluationFunctionForMoveOnDepth.put(move, min_max_ab(possibleMoves.get(move), searchDepth, possibleMoves.get(move).isFirstNextMove(), Integer.MIN_VALUE, Integer.MAX_VALUE, this.heuristic));
                end = System.nanoTime();
            }
        }
        moveTimes.add((double)(end - start)/(double)1000000);
        int resultMove = Collections.min(evaluationFunctionForMoveOnDepth.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
        //System.out.print("hole " + resultMove + " chosen.\n");
        return possibleMoves.get(resultMove);
    }
}
