package pentago;

public class PentagoSubBoard {
	
	private final int board[][];
	private int rotation;
	
	public PentagoSubBoard() {
		board = new int[3][3];
		rotation = 0;
	}
	
	
	public void rotate(int rotation) {
		this.rotation = (this.rotation + rotation) % 4;
		
		if (this.rotation < 0) {
			this.rotation += 4;
		}
	}

	public int get(int pos) {
		pos = pos - 1;
		int px = pos / 3;
		int py = pos % 3;
		
		switch (rotation) {
		case 0:
			return board[px][py];
			
		case 1:
			return board[2 - py][px];
			
		case 2:
			return board[2 - px][2 - py];
			
		case 3:
			return board[py][2 - px];
			
		default:
			throw new IllegalStateException();
		}
	}
	
	public void place(int pos, int value) {
		pos = pos - 1;
		int px = pos / 3;
		int py = pos % 3;
		
		switch (rotation) {
		case 0:
			board[px][py] = value;
			break;
			
		case 1:
			board[2 - py][px] = value;
			break;
			
		case 2:
			board[2 - px][2 - py] = value;
			break;
			
		case 3:
			board[py][2 - px] = value;
			break;
			
		default:
			throw new IllegalStateException();
		}
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("+-------+");
		
		for (int i = 0; i < 3; i++) {
			result.append('|');
			result.append(' ');
			for (int j = 0; j < 3; j++) {
				result.append(get(i * 3 + j + 1));
				result.append(' ');
			}
			result.append("|\n");
		}
		result.append("+-------+");
		
		return result.toString();
	}


	public int getAbsolute(int posx, int posy) {
		return this.get(1 + posx + posy * 3);
	}
}
