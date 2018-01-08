package pentago;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PentagoMove {
	private static final int BOARDS = 4;
	private static final int POSITIONS = 9;
	private static final int ROTATIONS = 2;
	public static final int MAX_POSSIBLE_MOVES = BOARDS * POSITIONS * BOARDS * ROTATIONS;
	private static final PentagoMove PENTAGO_MOVES[][][][] = new PentagoMove[BOARDS][POSITIONS][BOARDS][ROTATIONS];
	public static final List<PentagoMove>ALL_MOVES;
	
	static {
		LinkedList<PentagoMove> moves = new LinkedList<PentagoMove>();
		
		for (int i = 0; i < BOARDS; i++) {
			for (int j = 0; j < POSITIONS; j++) {
				for (int k = 0; k < BOARDS; k++) {
					PENTAGO_MOVES[i][j][k][0] = new PentagoMove(i + 1, j + 1, k + 1, -1);
					PENTAGO_MOVES[i][j][k][1] = new PentagoMove(i + 1, j + 1, k + 1, 1);
					moves.add(PENTAGO_MOVES[i][j][k][0]);
					moves.add(PENTAGO_MOVES[i][j][k][1]);
				}
			}
		}
		
		ALL_MOVES = Collections.unmodifiableList(moves);
	}
	
	public int board;
	public int position;
	public int boardToRotate;
	public int rotation;
	
	private PentagoMove(int board, int pos, int boardToRotate, int rotation) {
		this.board = board;
		this.position = pos;
		this.boardToRotate = boardToRotate;
		this.rotation = rotation;
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();

		result.append(board);
		result.append('/');
		result.append(position);
		result.append(' ');
		result.append(boardToRotate);
		
		if (rotation == -1) {
			result.append("L");
		} else if (rotation == 1) {
			result.append("R");
		} else {
			result.append('/');
			result.append(rotation);
		}
		
		return result.toString();
	}
	
	public static PentagoMove getPentagoMove(int board, int pos, int boardToRotate, int rotation) {
		return PENTAGO_MOVES[board-1][pos-1][boardToRotate-1][rotation==-1? 0: rotation];
	}
}
