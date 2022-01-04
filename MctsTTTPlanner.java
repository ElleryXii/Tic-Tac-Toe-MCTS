//2019 March

import java.util.*;
import java.util.LinkedList;


public class MctsTTTPlanner {
  int maxIterations;
  ArrayList<Move> excluded = new ArrayList<Move>();

  public MctsTTTPlanner(int i) {
    this.maxIterations = i;
  }

  //get the best move
  Move getMove(TTTState game) {
    MctsNode rootNode = new MctsNode(null, null, game,excluded);
    for (int i = 0; i < maxIterations; i++) {
      TTTState gameCopy = game.copy();
      MctsNode node = select(rootNode, gameCopy);
      node = node.expand(gameCopy);
      Reward reward = rollout(gameCopy);
      node.backPropagate(reward);
    }
    MctsNode mostVisitedChild = rootNode.getMostVisited();
    return mostVisitedChild.bridge;
  }

  //get a list of best moves
  LinkedList<Move> getMoves(TTTState game) {
    MctsNode rootNode = new MctsNode(null, null, game,excluded);
    for (int i = 0; i < maxIterations; i++) {
      TTTState gameCopy = game.copy();
      MctsNode node = select(rootNode, gameCopy);
      node = node.expand(gameCopy);
      Reward reward = rollout(gameCopy);
      node.backPropagate(reward);
    }
    LinkedList<MctsNode> mostVisited = rootNode.getMostVisitedList();
    LinkedList<Move> moves = new LinkedList<Move>();
    for (int i = 0; i<mostVisited.size();i++){
      moves.add(mostVisited.get(i).bridge);
    }
    return moves;
  }

  MctsNode select(MctsNode node, TTTState game) {
    while (!(node.unexplored.size() > 0)&& game.checkWin()==0) {
      node = node.select();
      Move move = node.bridge;
      if (move != null)
        game.makeMove(move);
    }
    return node;
  }

  Reward rollout(TTTState game) {
    game.randomPlay();
    return game.getReward();
  }
}
