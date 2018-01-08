package pentago;

import java.util.Objects;

public class PentagoBoard {

	private final PentagoSubBoard boards[];
	
	public PentagoBoard() {
		boards = new PentagoSubBoard[4];
		
		for (int i = 0; i < 4; i++) {
			boards[i] = new PentagoSubBoard();
		}
	}

	public void move(PentagoMove move, int team) {
		Objects.requireNonNull(move);
		
		if (team == 0 || !canMove(move)) {
			throw new IllegalArgumentException(team == 0? "Cannot use zero team" : "Illegal move");
		}

		place(move.board, move.position, team);
		rotate(move.boardToRotate, move.rotation);
	}
	
	public void undo(PentagoMove move) {
		Objects.requireNonNull(move);

		rotate(move.boardToRotate, -move.rotation);
		place(move.board, move.position, 0);
	}
	
	public boolean canMove(PentagoMove move) {
		return get(move.board, move.position) == 0;
	}

	private int get(int board, int position) {
		return boards[board - 1].get(position);
	}
	
	private int getAbsolute(int posx, int posy) {
		int board = 0;

		if (posx > 2) {
			posx -= 3;
			board += 1;
		}
		
		if (posy > 2) {
			posy -= 3;
			board += 2;
		}
		
		return boards[board].getAbsolute(posx, posy);
	}

	private void place(int board, int position, int value) {
		boards[board - 1].place(position, value);
	}
	
	private void rotate(int board, int rotation) {
		boards[board - 1].rotate(rotation);
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		
		for (int i = 0; i < 4; i += 2) {
			result.append("+-------+-------+\n");
			for (int j = 0; j < 3; j++) {
				result.append("| ");
				for (int k = 0; k < 3; k++) {
					int team = boards[i].get(j*3 + k + 1);
					result.append(team == 1 ? "B" : team == 2 ? "W" : Integer.toString(team));
					result.append(' ');
				}
				result.append("| ");
				for (int k = 0; k < 3; k++) {
					int team = boards[i + 1].get(j*3 + k + 1);
					result.append(team == 1 ? "B" : team == 2 ? "W" : Integer.toString(team));
					result.append(' ');
				}
				result.append("|\n");
			}
		}

		result.append("+-------+-------+");
		
		return result.toString();
	}

	public int value(int team, int opposition) {
		return value(team) - value(opposition);
	}
	
	public int value(int team) {
		
		int total = 0;
		
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) {
				if (getAbsolute(i, j) == team) {
					total += valueOfCount(count(i, j, 1, 0, team)); // right
					total += valueOfCount(count(i, j, 0, 1, team)); // down
					total += valueOfCount(count(i, j, 1, 1, team)); // diagonal down
					total += valueOfCount(count(i, j, 1, -1, team)); // diagonal up					
				}
			}
		}
		
		return total;
	}

	public int valueOfCount(int count) {
		int total = 1;
		
		for (int i = 1; i < count; i++) {
			total *= 10;
		}
		
		return total == 1? 0 : total;
	}
	
	public int count(int i, int j, int id, int jd, int team) {
		if (i-id>=0 && i-id<6 && j-jd>=0 && j-jd<6 && getAbsolute(i-id, j-jd) == team) {
			return 0;
		}
		
		int count = 0;
		
		while (i < 6 && i >= 0 && j < 6 && j >= 0 && getAbsolute(i, j) == team && count < 5) {
			count++;
			i += id;
			j += jd;
		}
		
		return count;
	}

	public boolean hasWinner() {
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) {
				int team = getAbsolute(i, j);
				if ((team != 0) && (
					count(i, j, 1, 0, team) == 5 ||   // right
					count(i, j, 0, 1, team) == 5 ||   // down
					count(i, j, 1, 1, team) == 5 ||   // diagonal down
					count(i, j, 1, -1, team) == 5)) { // diagonal up
					return true; 						
				}
			}
		}
		
		return false;
	}

	public int getWinner() {
		boolean foundWinner = false;
		int result = 0;
		
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) {
				int team = getAbsolute(i, j);
				if ((team != 0) && (
					count(i, j, 1, 0, team) == 5 ||   // right
					count(i, j, 0, 1, team) == 5 ||   // down
					count(i, j, 1, 1, team) == 5 ||   // diagonal down
					count(i, j, 1, -1, team) == 5)) { // diagonal up
					
					if (!foundWinner) {
						foundWinner = true;
						result = team;
					} else if (result != team) {
						return 0;
					}
				}
			}
		}
		
		return result;
	}

	public boolean hasWinnerWithRotation(int boardToRotate, int rotation) {
		this.rotate(boardToRotate, -rotation);
		boolean result = hasWinner();
		this.rotate(boardToRotate, rotation);
		return result;
	}
}
