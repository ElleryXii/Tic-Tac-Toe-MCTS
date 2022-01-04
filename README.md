# Tic-Tac-Toe
An old Java implementation of n*n tic-tac-toe game using Monte Carlo Tree Search and minimax.


# How to run
Compile Play.java. Execute with Java Play auto to let the AI play with itself, or with Java
Play manual to play with the AI with user input.


# Problem description
## General
A tic-tac-toe game on a n*n sized board. Two players play in an alternate order to place his choice on the board. A player wins when he places m parts of his choice in a consecutive order.
## Environment
Two-agent, known, fully observable, deterministic, discrete, sequential, and static.
## Search Space
O(3^n)

# Algorithm description
This implementation uses Monte Carlo Tree Search with minimax verification and completely switches to Minimax at endgame (when there are only 15 or fewer available moves left). 

## MCTS
A big part of this AI uses Monte Carlo Tree Search, an algorithm based on random selecting and expanding states and aggregating the result. In this case, it will run a large number of random simulations of tic-tac-toe games and return the moves that are most likely to win. The performance of MCTS depends on its iteration limit. Higher `maxIteration` gives better results but takes longer time. The iteration limit of MCTS is set to 50000 in the code, a number that gives decent result at n ≤ 12 (higher n value not tested) without slowing down the planning unreasonably.

## Minimax
For the heuristic used by Minimax, see the next section (evaluation function). In this implementation, Minimax algorithm is used for two purposes:
1. Test the candidate moves returned by MCTS.
Since MCTS is based on probability calculated by random simulations, there’s a chance that the move it obtains will in fact fail. To avoid this as best as we can, each time MCTS returns a list of the best moves it got, Depth-3 Minimax will be run on each of the candidate moves to make sure they won’t lead to loss in 3 steps. Any move that does fail will be removed. If the list of move returned by MCTS become empty after Minimax test was run, MCTS will be called again to generate a new list, for up to 3 times. After that, if there’s still no eligible move, depth-3 heuristic minimax will be called to find the last-resort move.
2. Endgame search
When there are only 12 or less moves remaining, exhaustive minimax will be used to make optimal decisions.
# Evaluation Function
Since this AI uses primarily MCTS, heuristic minimax is only rarely used when there’s no alternative, so the effectiveness of heuristic functions are not as important as they otherwise would be. Some rudimentary heuristic functions in `TTTState.java` are the following:
- `heuRow()`: return a value based on the number of consecutive symbols in the row of the
most recent move.
- `heuCol()`: return a value based on the number of consecutive symbols in the column of the
most recent move.
- `heuDia()`: return a value based on the number of consecutive symbols in the diagonal of
the most recent move.
- `heuAntiDia()`: return a value based on the number of consecutive symbols in the anti-diagonal of the most recent move.
  
The overall evaluation function `getEval()` returns `Integer.MAX_VALUE` when ‘x’ won, returns `Integer.MIN_VALUE` when ‘o’ won, returns `0` when it’s a draw. Otherwise, it returns the sum of the above heuristics.