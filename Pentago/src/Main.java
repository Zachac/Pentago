import java.io.FileNotFoundException;

import computer.ComputerPlayer;
import computer.MinMaxPlayer;
import pentago.PentagoBoard;
import pentago.PentagoMove;
import util.ConsoleIOFileOut;

public class Main {
	public static final boolean USER = true;
	public static final boolean COMPUTER = !USER;

	public static final boolean WHITE = true;
	public static final boolean BLACK = !WHITE;
	
	public static void main(String[] args) throws FileNotFoundException {
		PentagoBoard board = new PentagoBoard();
		ConsoleIOFileOut io = new ConsoleIOFileOut(System.in, System.out, "output.txt");
		ComputerPlayer minmax = new MinMaxPlayer(2, 1, true);
		
		boolean userTeamChoice = getUserTeam(io); 

		int userTeam;
		int compTeam;
		
		if (userTeamChoice == WHITE) {
			userTeam = 2;
			compTeam = 1;
		} else { // userTeamChoice == BLACK
			userTeam = 1;
			compTeam = 2;
		}
		
		boolean firstPlayer = getUserGoesFirst(io);
		
		gameLoop(board, io, minmax, userTeam, compTeam, firstPlayer);
		announceWinner(io, board);
	}
	
	private static void announceWinner(ConsoleIOFileOut io, PentagoBoard board) {
		int winner = board.getWinner();
		
		io.println(board.toString());
		
		if (winner == 0) {
			io.println("It's a draw!");
		} else {
			io.println("Player " + (winner == 1? "B" : winner == 2? "W" : winner) + " wins!");
		}
	}

	private static boolean getUserTeam(ConsoleIOFileOut io) {
		String choice = "";
		
		while (!choice.equals("B") && !choice.equals("W")) {
			io.print("User, would you like to be B or W? (B/W): ");
			choice = io.nextLine();
		}
		
		return choice.equals("W") == WHITE;
	}

	private static boolean getUserGoesFirst(ConsoleIOFileOut io) {
		String choice = "";
		
		while (!choice.equals("1") && !choice.equals("2")) {
			io.print("User, would you like to go first or second? (1/2): ");
			choice = io.nextLine();
		}
		
		return choice.equals("1");
	}

	public static void gameLoop(PentagoBoard board, ConsoleIOFileOut io,
			ComputerPlayer c, int userTeam, int compTeam, boolean firstPlayer) {
		
		int movesPerformed = 0;
		boolean curPlayer = firstPlayer;
		PentagoMove moveDone = null;
		
		if (curPlayer == COMPUTER) {
			doComputerMove(io, c, board, compTeam, movesPerformed);
			curPlayer = !curPlayer;
			movesPerformed++;
		}
		
		do {
			io.println(board.toString());
			
			if (curPlayer == USER) {
				moveDone = doUserMove(io, board, userTeam);
			} else if (curPlayer == COMPUTER) {
				moveDone = doComputerMove(io, c, board, compTeam, movesPerformed);
				io.println("Nodes Expanded: " + c.getNodesExpanded());
				io.println("");
			}

			curPlayer = !curPlayer;
			movesPerformed++;
		} while (!board.hasWinner() && !board.hasWinnerWithRotation(moveDone.boardToRotate, moveDone.rotation));
	}
	
	
	public static PentagoMove doComputerMove(ConsoleIOFileOut io, ComputerPlayer c, PentagoBoard board, int player, int movesPerformed) {
		io.print("Player " + (player == 1? "B" : player == 2? "W" : player) + ", please enter your move: ");
		PentagoMove result =  c.move(board, 36 - movesPerformed);
		io.println(result.toString());
		board.move(result, player);
		return result;
	}
	
	public static PentagoMove doUserMove(ConsoleIOFileOut io, PentagoBoard board, int player) {
		PentagoMove result = null;
		
		while (result == null) {
			io.print("Player " + (player == 1? "B" : player == 2? "W" : player) + ", please enter your move: ");
			String choice = io.nextLine();
			String[] choices = choice.split("[\\s/]");
			
			try {
				if (choices.length == 3 && choices[2].length() == 2) {
					int boardToPlace = Integer.parseInt(choices[0]);
					int position = Integer.parseInt(choices[1]);
					int boardToRotate = Integer.parseInt(Character.toString(choices[2].charAt(0)));
					char rotationSignal = choices[2].charAt(1);
					int rotation = 0;
					
					if (rotationSignal == 'l' || rotationSignal == 'L') {
						rotation = -1;
					} else if (rotationSignal == 'r' || rotationSignal == 'R') {
						rotation = 1;
					} else {
						throw new IllegalArgumentException("Unkown rotation: use L or R");
					}
					
					result = PentagoMove.getPentagoMove(boardToPlace, position, boardToRotate, rotation);
					
					if (!board.canMove(result)) {
						result = null;
						throw new IllegalArgumentException("You cannot move there.");
					}
				} else {
					throw new IllegalArgumentException("Unkown input format: use b/p bd");
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				io.println("Illegal value: " + e.getMessage());
			} catch (NumberFormatException e) {
				io.println("Number expected: " + e.getMessage());
			} catch (IllegalArgumentException e) {
				io.println(e.getMessage());
			}
		}
		
		board.move(result, player);
		return result;
	}

}
