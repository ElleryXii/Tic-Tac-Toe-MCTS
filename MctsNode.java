//2019 March

import java.util.LinkedList;
import java.util.Random;
import java.util.*;

public class MctsNode {
  ArrayList<Move> excluded;
  MctsNode parent;
  int numSim = 0;
  Reward reward;
  LinkedList<MctsNode> children = new LinkedList<>();
  LinkedList<Move> unexplored;
  Move bridge;   //the move from the parent state to this state
  boolean player;

  public MctsNode(MctsNode parent, Move move, TTTState state, ArrayList<Move> excluded) {
    this.excluded = excluded;
    this.parent = parent;
    bridge = move;
    this.player = state.player;
    unexplored = state.getMoves(player);
    for (int i = 0; i<excluded.size();i++){
      if (unexplored.get(i).isequal(excluded.get(i)))
        unexplored.remove(i);
    }
    reward = new Reward(0, 0);
  }

  //select the node max uctvalue
  MctsNode select() {
    MctsNode selectedNode = this;
    double max = Integer.MIN_VALUE;
    for (MctsNode child : children){
      double uctValue = getUctValue(child);
      if (uctValue>max){
        max = uctValue;
        selectedNode = child;
      }
    }
    return selectedNode;
  }

  //get the uctvalue
  double getUctValue(MctsNode child) {
    double uctValue;
    if (child.numSim==0)
      uctValue = 1;
    else
      uctValue = (1.0*child.getReward(player))/(child.numSim * 1.0) + (Math.sqrt(2.0*(Math.log(numSim*1.0)/child.numSim)));
    Random r = new Random();
    uctValue += (r.nextDouble()/10000000);
    return uctValue;
  }

  //expend an unexplored node
  MctsNode expand(TTTState state) {
    if (!(unexplored.size() > 0))
      return this;
    Random random = new Random();
    int moveIndex = random.nextInt(unexplored.size());
    Move move = unexplored.remove(moveIndex);
    state.makeMove(move);
    MctsNode child = new MctsNode(this, move, state,excluded);
    children.add(child);
    return child;
  }

  //update the reward of this and parent
  void backPropagate(Reward reward) {
    this.reward.addReward(reward);
    this.numSim++;
    if (parent != null)
      parent.backPropagate(reward);
  }

  //get the most mostVisited node among the children
  MctsNode getMostVisited() {
    int max = 0;
    MctsNode mostVisited = null;
    for (MctsNode child : children) {
      if (child.numSim > max) {
        mostVisited = child;
        max = child.numSim;
      }
    }
    return mostVisited;
  }


  //get a list of equally most mostVisited node among the children
  LinkedList<MctsNode> getMostVisitedList() {
    LinkedList<MctsNode> mostVisited = new LinkedList<MctsNode>();
    int max = 0;
    for (MctsNode child : children) {
      if (child.numSim == max) {
        mostVisited.add(child);
      }
      else if (child.numSim > max) {
        mostVisited.clear();
        mostVisited.add(child);
        max = child.numSim;
      }
    }
    return mostVisited;
  }

  double getReward(boolean player) {
    return reward.getReward(player);
  }

  void print(){
    System.out.println("numsim+ " +numSim+" reward "+reward.getString());
  }
}
