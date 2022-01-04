//2019 March

import java.util.LinkedList;
import java.util.*;


public class TicTacToe{
  MctsTTTPlanner mcts = new MctsTTTPlanner(100000);
  Move getBestMove(TTTState game){
    mcts.excluded = new ArrayList<Move>();
    int remainingMove = game.n*game.n-game.totalmove;
    int depth = 5;
    int bestu = 0;
    boolean player = game.player;
    //blank board, go for the center
    if (game.totalmove==0)
      return (new Move(game.n/2,game.n/2,player));
    //use monte carlo tree search
    if (remainingMove>12){
      //max 3 chances of re-run mcts if no move left after minimax test
      // return mcts.getMove(game);
      int i = 3;
      while (i>0){
        //get a list of move candidate use mcts
        LinkedList<Move> moves =  mcts.getMoves(game);
        for (int j = 0; j<moves.size(); j++){
          System.out.print("mcts move ");
          moves.get(j).print();
        }
        for (int j = 0; j<mcts.excluded.size(); j++){
          System.out.print("mcts excluded ");
          mcts.excluded.get(j).print();
        }
        for (Move move : moves){
          //test the moves with minimax, make sure they won't fail on the next several steps
          int u = minimax(game.getState(move), depth, Integer.MIN_VALUE, Integer.MAX_VALUE, !player);
          if ((player && u==Integer.MIN_VALUE)||(!player && u==Integer.MAX_VALUE)){
            mcts.excluded.add(move);
            moves.remove(move);
          }
        }
        //if there's move remaing, return the first one
        if (!moves.isEmpty())
          return moves.get(0);
        System.out.println("isEmpty");
        i--;
      }
      System.out.println("isEmpty after 5");
    }
    //switch to minimax at endgame
    if (remainingMove<=12)
      depth = 15;
    LinkedList<Move> moves = game.getMoves(player);
    if (player)
      bestu = Integer.MIN_VALUE;
    else
      bestu = Integer.MAX_VALUE;
    Move bestMove = moves.get(0);
    for (Move move : moves){
      int u = minimax(game.getState(move), depth, Integer.MIN_VALUE, Integer.MAX_VALUE, !player);
      if ((player && u>=bestu)||(!player && u<=bestu)){
        bestu = u;
        bestMove = move;
      }
    }
    return bestMove;
  }

  //get the dynamic evaluation of the state using minimax algorithm
  int minimax(TTTState state, int depth, int alpha, int beta, boolean maximizing){
    if (state.checkWin()!=0)
      return state.getEval();
    if (depth <= 0)
      return state.getEval();
    //get max value
    if (maximizing){
      LinkedList<Move> branch = state.getMoves(true);
      int maxVal = Integer.MIN_VALUE;
      for (Move move : branch){
        int eval = minimax(state.getState(move), depth-1, alpha, beta, false);
        maxVal = Math.max(maxVal,eval);
        alpha = Math.max(alpha,eval);
        if (beta<=alpha)
          break;
      }
      return maxVal;
    }
    //get min value
    else{
      LinkedList<Move> branch = state.getMoves(false);
      int minVal = Integer.MAX_VALUE;
      for (Move move : branch){
        int eval = minimax(state.getState(move), depth-1, alpha, beta, true);
        minVal = Math.min(minVal,eval);
        beta = Math.min(beta,eval);
        if (beta<=alpha)
          break;
      }
      return minVal;
    }
  }
}
