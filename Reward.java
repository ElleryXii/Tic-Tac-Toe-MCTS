import java.util.*;
public class Reward {
  HashMap<Integer, Integer> rewards = new HashMap<>();
  public Reward(int reward0, int reward1){
    rewards.put(0, reward0);
    rewards.put(1, reward1);
  }
  HashMap<Integer, Integer> getReward(){
    return rewards;
  }

  int getReward(boolean player){
    if (player)
      return rewards.get(0);
    return rewards.get(1);
  }

  void addReward(Reward reward) {
    rewards.put(0, rewards.get(0) + reward.getReward(true));
    rewards.put(1, rewards.get(1) + reward.getReward(false));
  }

  String getString(){
    return "("+rewards.get(0)+", "+rewards.get(1)+")";
  }
}
