package computer;

import java.util.Random;

import pentago.PentagoBoard;
import pentago.PentagoMove;

public class MinMaxPlayer implements ComputerPlayer {
	private static final Random rand = new Random();
	private static final boolean MAX = true;
	private static final boolean MIN = !MAX;

	private final int team;
	private final int opposition;
	
	private int max_depth;
	private boolean useABPruning;
	private long nodesExpanded;
	
	public MinMaxPlayer(int team, int opposition, boolean useABPruning) {
		this.team = team;
		this.opposition = opposition;
		this.useABPruning = useABPruning;
		
		max_depth = 0;
		nodesExpanded = 0;
	}

	@Override
	public PentagoMove move(PentagoBoard board, int movesLeft) {

		if (movesLeft > 34) {
			max_depth = 2;
		} else if (movesLeft > 10) {
			max_depth = 3;
		} else if (movesLeft > 6) {
			max_depth = 4;
		} else {
			max_depth = 5;
		}
		
		PentagoMove[] moves = new PentagoMove[PentagoMove.MAX_POSSIBLE_MOVES];
		int foundMoves = 0;
		int maxValue = Integer.MIN_VALUE;
		nodesExpanded = 0;
		
		for (PentagoMove move : PentagoMove.ALL_MOVES) {
			if (board.canMove(move)) {
				board.move(move, team);
				int value = useABPruning ? evaluateABPruning(board, 1, MIN, Integer.MIN_VALUE, Integer.MAX_VALUE) : evaluate(board, 1, MIN);
				board.undo(move);
				
				if (value > maxValue) {
					clearArray(moves);
					foundMoves = 0;
					maxValue = value;
				} 
				
				if (value == maxValue) {
					moves[foundMoves] = move;
					foundMoves++;
				}
			}
		}
		
		return moves[rand.nextInt(foundMoves)];
	}

	private int evaluateABPruning(PentagoBoard board, int depth, boolean minormax, int alpha, int beta) {
		nodesExpanded++;
		
		if (depth >= max_depth) {
			return board.value(team, opposition);
		}
		
		int bestValue;
		if (minormax == MAX) {
			bestValue = Integer.MIN_VALUE;
		} else {
			bestValue = Integer.MAX_VALUE;
		}
		
		for (PentagoMove move : PentagoMove.ALL_MOVES) {
			if (board.canMove(move)) {
				board.move(move, minormax ? team : opposition);
				int value = evaluateABPruning(board, depth + 1, !minormax, alpha, beta);
				board.undo(move);

				if (minormax == MAX && value > bestValue) {
					bestValue = value;
					
					if (bestValue > alpha) {
						alpha = bestValue;

						if (alpha > beta) {
							return minormax? alpha : beta;
						}
					}
				} else if (minormax == MIN && value < bestValue) {
					bestValue = value;

					if (bestValue < beta) {
						beta = bestValue;

						if (alpha > beta) {
							return minormax? alpha : beta;
						}
					}
				}
			}
		}
		
		return bestValue;
	}

	private int evaluate(PentagoBoard board, int depth, boolean minormax) {
		nodesExpanded++;
		
		if (depth >= max_depth) {
			return board.value(team, opposition);
		}
		
		int bestValue;
		if (minormax == MAX) {
			bestValue = Integer.MIN_VALUE;
		} else {
			bestValue = Integer.MAX_VALUE;
		}
		
		for (PentagoMove move : PentagoMove.ALL_MOVES) {
			if (board.canMove(move)) {
				board.move(move, minormax ? team : opposition);
				int value = evaluate(board, depth + 1, !minormax);
				board.undo(move);

				if (minormax == MAX && value > bestValue) {
					bestValue = value;
				} else if (minormax == MIN && value < bestValue) {
					bestValue = value;
				}
			}
		}
		
		return bestValue;
	}
	
	@Override
	public long getNodesExpanded() {
		return nodesExpanded;
	}
	
	private static void clearArray(Object[] a) {
		for (int i = 0; i < a.length; i++) {
			a[i] = null;
		}
	}
}
