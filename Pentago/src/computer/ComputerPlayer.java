package computer;

import pentago.PentagoBoard;
import pentago.PentagoMove;

public interface ComputerPlayer {

	/**
	 * Finds the best move for a board.
	 */
	public PentagoMove move(PentagoBoard board, int movesLeft);
	
	/**
	 * Get the number of nodes expanded in the last operation of move().
	 */
	public long getNodesExpanded();
}
