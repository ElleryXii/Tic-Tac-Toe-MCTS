//2019 March


import java.util.ArrayList;
import java.util.*;
import java.util.Random;


public class TTTState {
  int n, m;
  int[][] board;
  int lasti,lastj;
  int totalmove = 0;
  boolean player;
  int remainingMove;

  public TTTState(int n, int m){
    player = true;
    this.n = n;
    this.m = m;
    remainingMove = n*n;
    board = new int[n][n];
  }


  boolean makeMove(Move move){
    if (isValidMove(move)){
      player = !player;
      lasti = move.i;
      lastj = move.j;
      if (move.player)
        board[lasti][lastj] = 1;
      else
        board[lasti][lastj] = -1;
      totalmove++;
      remainingMove--;
      return true;
    }
    return false;
  }

  //check if the passed in move is valid
  boolean isValidMove(Move move){
    if (move.i>n||move.j>n)
      return false;
    if (board[move.i][move.j]!=0)
      return false;
    return true;
  }


  //return all the valid moves
  LinkedList<Move> getMoves(boolean player){
    LinkedList<Move> moves = new LinkedList<Move>();
    for (int i = 0; i < n; i++){
      for (int j=0; j < n; j++){
        Move newMove = new Move(i,j,player);
        if (isValidMove(newMove)){
          moves.add(newMove);
        }
      }
    }
    return moves;
  }

  //get the evaluation of this state
  int getEval(){
    int w = checkWin();
    if (w==1)
      return Integer.MAX_VALUE;
    if (w==-1)
      return Integer.MIN_VALUE;
    if (w==2)
      return 0;
    // System.out.println("h");
    return heuRow(lasti,lastj)+heuCol(lasti, lastj)+heuDia(lasti,lastj)+heuAntiDia(lasti,lastj);
  }

  //check if game terminates. Return 0 is game has not terminated, 2 if it terminates in a draw, 1 is x win, -1 if o win
  int checkWin(){
    int rowCount = 0;
    int colCount = 0;
    int diaCount = 0;
    int antiDiaCount = 0;
    int p = board[lasti][lastj];
    if (p==0)
      return 0;
    //check row
    int i = lasti;
    while (i<n&&board[i][lastj]==p){
      i++;
      rowCount++;
      if (rowCount>=m)
        return p;
    }
    i = lasti-1;
    while (i>=0&&board[i][lastj]==p){
      i--;
      rowCount++;
      if (rowCount>=m)
        return p;
    }

    //check column
    int j = lastj;
    while (j<n&&board[lasti][j]==p){
      j++;
      colCount++;
      if (colCount>=m)
        return p;
    }
    j = lastj-1;
    while (j>=0 && board[lasti][j]==p){
      j--;
      colCount++;
      if (colCount>=m)
        return p;
    }

    //check diagnols
    i = lasti;
    j = lastj;
    while (i<n && j<n && board[i][j]==p){
      i++;
      j++;
      diaCount++;
      if (diaCount>=m)
        return p;
    }
    i = lasti-1;
    j = lastj-1;
    while (i>=0 && j>=0 && board[i][j]==p){
      i--;
      j--;
      diaCount++;
      if (diaCount>=m)
        return p;
    }

    i = lasti;
    j = lastj;
    while (i<n && j>=0 && board[i][j]==p){
      i++;
      j--;
      antiDiaCount++;
      if (antiDiaCount>=m)
        return p;
    }
    i = lasti-1;
    j = lastj+1;
    while (i>=0 && j<n && board[i][j]==p){
      i--;
      j++;
      antiDiaCount++;
      if (antiDiaCount>=m)
        return p;
    }
    if (totalmove>=n*n)
      return 2;
    return 0;
  }



//************heuristics******************//
  int heuRow(int row, int col){
    int val = 0;
    for (int i = col; i < n; i++){
      if (board[row][i]==0)
        val += board[row][col];
      else if (board[row][i]!=board[row][col]){
        // val+=heuRowDef(row,i);
        break;
      }
      val += 5*board[row][i];
    }
    for (int i = col-1; i >=0; i--){
      if (board[row][i]==0)
        val += board[row][col];
      else if (board[row][i]!=board[row][col])
        break;
      val += 5*board[row][i];
    }
    return val;
  }

  // int heuRowDef(int row, int col){
  //   int count = 0;
  //   int val = 0;
  //   boolean blank = false;
  //   if (lastj>col){
  //     for (int i = col;i>=0;i--){
  //       if (board[row][i]==board[row][col])
  //         count++;
  //       else if (board[row][i]==0){
  //         blank = true;
  //         break;
  //       }
  //       else;
  //     }
  //   }
  //   else{
  //     for (int i = col;i<0;i++){
  //       if (board[row][i]==board[row][col])
  //         count++;
  //       else if (board[row][i]==0){
  //         blank = true;
  //         break;
  //       }
  //       else;
  //     }
  //   }
  //   if (blank){
  //     val = 8*count*board[row][col];
  //   }
  //   else{
  //     val = 2*count*board[row][col];
  //   }
  //   if (count==m-1){
  //     val = 1000*board[row][col];
  //   }
  //   return -1*val;
  // }


  int heuCol(int row, int col){
    int val = 0;
    int p;

    if (player)
      p = 1;
    else
      p = -1;
    for (int i = row; i < n; i++){
      if (board[i][col]==0)
        val += board[row][col];
      else if (board[i][col]!=board[row][col])
        break;
      val += 5*board[i][col];
    }
    for (int i = row; i >=0; i--){
      if (board[i][col]==0)
        val += board[row][col];
      if (board[i][col]!=board[row][col])
        break;
      val += 5*board[i][col];
    }
    return val;
  }

  int heuDia(int row, int col){
    int val = 0;
    int p;
    if (player)
      p = 1;
    else
      p = -1;
    int i = lasti;
    int j = lastj;
    while (i<n && j<n && board[i][j]==p){
      i++;
      j++;
      val++;
    }
    i = lasti-1;
    j = lastj-1;
    while (i>=0 && j>=0 && board[i][j]==p){
      i--;
      j--;
      val++;
    }
    return val*5;
  }

  int heuAntiDia(int row, int col){
    int val = 0;
    int p;
    if (player)
      p = 1;
    else
      p = -1;
    int i = lasti;
    int j = lastj;
    while (i<n && j>=0 && board[i][j]==p){
      i++;
      j--;
      val++;
    }
    i = lasti-1;
    j = lastj+1;
    while (i>=0 && j<n && board[i][j]==p){
      i--;
      j++;
      val++;
    }
    return val*5;
  }

  //************heuristics end******************//


  //print the current state
  void print(){
    for (int i = 0; i<n;i++){
      for (int j =0; j<n; j++){
        if (board[i][j]==1)
          System.out.print(" X ");
        else if (board[i][j]==-1)
          System.out.print(" O ");
        else
          System.out.print(" _ ");
      }
      System.out.println("");
    }
    System.out.println("=============");
  }

  //return a deep copy of the current state;
  TTTState copy(){
    TTTState newState = new TTTState(n, m);
    newState.n = n;
    newState.m = m;
    newState.player = this.player;
    newState.totalmove = totalmove;
    newState.lasti = lasti;
    newState.lastj = lastj;
    for (int i = 0; i<n; i++){
      for (int j = 0; j<n; j++){
        newState.board[i][j]=this.board[i][j];
      }
    }
    return newState;
  }

  //return the state after the passed in move was made
  TTTState getState(Move move){
    TTTState state = this.copy();
    state.makeMove(move);
    return state;
  }

  //play randomly until game is over
  void randomPlay(){
    Random r = new Random();
    Move move;
    while(this.checkWin()==0){
      if (remainingMove/(n*n)<=remainingMove){
        int i = r.nextInt(n);
        int j = r.nextInt(n);
        move = new Move(i,j,player);
        while (!isValidMove(move)){
          i = r.nextInt(n);
          j = r.nextInt(n);
          move = new Move(i,j,player);
        }
      }
      else{
        LinkedList<Move> moves = getMoves(player);
        move = moves.get(r.nextInt(moves.size()));
      }
      makeMove(move);
    }
  }

  //get the reward of current state. For MCTS.
  Reward getReward() {
    if (checkWin()==1)
      return new Reward(1, -1);
    else if (checkWin()==-1)
      return new Reward(-1, 1);
    return new Reward(0, 0);
  }
}
