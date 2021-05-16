package com.mankala.players;
import com.mankala.GameState;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class AIPlayer extends Player {
    private boolean isRandomMove;
    protected ArrayList<Double> moveTimes;

    public AIPlayer(String playerName, boolean isRandom) {
        super(playerName);
        this.isRandomMove = isRandom;
        moveTimes = new ArrayList<>();
    }

    public int min_max_ab(GameState node, int depth, boolean minPlayer, int alpha, int beta, int heuristic){
        if(depth == 0 || ((minPlayer && node.isFirstOver()) || (!minPlayer && node.isSecondOver())))
            return node.countEvaluationFunctionValue(heuristic);
        int value;
        if(minPlayer){
            HashMap<Integer, GameState> children = node.getPossibleNextStatesForFirstPlayer();
            for(int move : children.keySet()){
                value = min_max_ab(children.get(move), depth - 1, node.isFirstNextMove(), alpha, beta, heuristic);
                if(value < beta) //found better worst move
                    beta = value;
                if(alpha >= beta)//cut off
                    return beta;
            }
            return beta; //best worst move
        }
        else{
            HashMap<Integer, GameState> children = node.getPossibleNextStatesForSecondPlayer();
            for(int move : children.keySet()){
                value = min_max_ab(children.get(move), depth - 1, !node.isSecondNextMove(), alpha, beta, heuristic);
                if(value > alpha) //found better best move
                    alpha = value;
                if(alpha >= beta)//cut off
                    return alpha;
            }
            return alpha; //best best move
        }
    }

    public int min_max(GameState node, int depth, boolean minPlayer, int heuristic){
        if(depth == 0 || ((minPlayer && node.isFirstOver()) || (!minPlayer && node.isSecondOver())))
            return node.countEvaluationFunctionValue(heuristic);
        int value;
        if(minPlayer){
            value = Integer.MAX_VALUE;
            HashMap<Integer, GameState> children = node.getPossibleNextStatesForFirstPlayer();
            for(int move : children.keySet()){
                value = Math.min(value, min_max(children.get(move), depth - 1, node.isFirstNextMove(), heuristic));
            }
            return value;
        }
        else{
            value = Integer.MIN_VALUE;
            HashMap<Integer, GameState> children = node.getPossibleNextStatesForSecondPlayer();
            for(int move : children.keySet()){
                value = Math.max(value, min_max(children.get(move), depth - 1, !node.isSecondNextMove(), heuristic));
            }
            return value;
        }
    }

    public boolean isRandomMove() {
        return isRandomMove;
    }

    public ArrayList<Double> getMoveTimes(){
        return moveTimes;
    }
}
