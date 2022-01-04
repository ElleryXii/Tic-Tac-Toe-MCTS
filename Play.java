//2019 March


import java.util.Scanner;
import java.util.InputMismatchException;


public class Play{
  boolean auto;
  public static void main(String[] argv) {
    if ( (argv == null) || (argv.length != 1) ) {
      System.out.println ("Use: java Play manual (user vs ai) \n Or: java Play auto (ai vs ai)");
      System.exit (0);
    }
    Play play = new Play();
    if (argv[0].equals ("auto"))
       play.auto = true;
    else
       play.auto = false;
    play.startGame();
  }

  void startGame(){
    Scanner in = new Scanner(System.in);
    System.out.println("Enter n: ");
    int n = in.nextInt();
    System.out.println("Enter m: ");
    int m = in.nextInt();
    TTTState game = new TTTState(n,m);
    TicTacToe Planner = new TicTacToe();
    // boolean auto = true;
    boolean player = true;
    while(game.checkWin()==0){
      if (player){
        if (auto){
          game.makeMove(Planner.getBestMove(game));
        }
        else{
            Move thisMove = getPlayerMove(game.player,in);
            while (thisMove==null){
              in.nextLine();
              thisMove = getPlayerMove(game.player,in);
            }
            game.makeMove(thisMove);
        }
      }
      else{
        Move thisMove = Planner.getBestMove(game);
        thisMove.print();
        game.makeMove(thisMove);
      }
      game.print();
      player = !player;
    }
    in.close();
    printResutl(game);
  }

  void printResutl(TTTState game){
    switch(game.checkWin()){
      case 1:
        System.out.println("x won");
        break;
      case -1:
        System.out.println("o won");
        break;
      case 2:
        System.out.println("Draw");
        break;
    }
  }

  static Move getPlayerMove(boolean player, Scanner in){
    try{
      System.out.println("Enter i: ");
      int i = in.nextInt();
      System.out.println("Enter j: ");
      int j = in.nextInt();
      Move move = new Move(i,j,player);
      return move;
    }
    catch (InputMismatchException e) {
      System.out.println("error");
      return null;
    }
  }

}
