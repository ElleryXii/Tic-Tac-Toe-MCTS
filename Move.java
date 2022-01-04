public class Move{
  int i,j;
  boolean player;
  int visitCount = 0;
  public Move(int i,int j, boolean player){
    this.i=i;
    this.j=j;
    this.player = player;
  }
  boolean isequal(Move move){
    if (move.i == this.i && move.j == this.j && move.player == this.player)
      return true;
    return false;
  }
  void print(){
    System.out.println("("+i+", "+j+") by player "+player);
  }
}
