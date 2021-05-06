package com.mankala.players;
import com.mankala.GameState;
import java.util.HashMap;

public abstract class AIPlayer extends Player {
    private boolean isRandomMove;

    public AIPlayer(String playerName, boolean isRandom) {
        super(playerName);
        this.isRandomMove = isRandom;
    }

    public int min_max_ab(GameState node, int depth, boolean minPlayer, int alpha, int beta){
        if(depth == 0 || ((minPlayer && node.isFirstOver()) || (!minPlayer && node.isSecondOver())))
            return node.countEvaluationFunctionValue();
        int value;
        if(minPlayer){
            HashMap<Integer, GameState> children = node.getPossibleNextStatesForFirstPlayer();
            for(int move : children.keySet()){
                value = min_max(children.get(move), depth - 1, node.isFirstNextMove());
                if(value < beta)
                    beta = value;
                if(alpha >= beta)
                    return beta;
            }
            return beta;
        }
        else{
            HashMap<Integer, GameState> children = node.getPossibleNextStatesForSecondPlayer();
            for(int move : children.keySet()){
                value = min_max_ab(children.get(move), depth - 1, !node.isSecondNextMove(), alpha, beta);
                if(value > alpha)
                    alpha = value;
                if(alpha >= beta)
                    return alpha;
            }
            return alpha;
        }
    }

    public int min_max(GameState node, int depth, boolean minPlayer){
        if(depth == 0 || ((minPlayer && node.isFirstOver()) || (!minPlayer && node.isSecondOver())))
            return node.countEvaluationFunctionValue();
        int value;
        if(minPlayer){
            value = Integer.MAX_VALUE;
            HashMap<Integer, GameState> children = node.getPossibleNextStatesForFirstPlayer();
            for(int move : children.keySet()){
                value = Math.min(value, min_max(children.get(move), depth - 1, node.isFirstNextMove()));
            }
            return value;
        }
        else{
            value = Integer.MIN_VALUE;
            HashMap<Integer, GameState> children = node.getPossibleNextStatesForSecondPlayer();
            for(int move : children.keySet()){
                value = Math.max(value, min_max(children.get(move), depth - 1, !node.isSecondNextMove()));
            }
            return value;
        }
    }

    public boolean isRandomMove() {
        return isRandomMove;
    }
}
