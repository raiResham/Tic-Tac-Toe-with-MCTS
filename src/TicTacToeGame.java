import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class TicTacToeGame {
	// X = Human
	// O = AI
	private static final char HUMAN_MARKER = 'X';
	private static final char AI_MARKER = 'O';
	char board[][] = new char[3][3];
	private static final int HUMAN_WON = 0;
	private static final int AI_WON = 1;
	private static final int DRAW = 2;
	private static final int CONTINUE = -1;
	private static final int numberOfSimulations = 100;
	
	private float score[][] = new float[3][3];
	
	// 0 = Human
	// 1 = AI
	private int player = 0;
	Scanner scanner = new Scanner(System.in);
	
	Random rand = new Random();
	
	public void startGame() {
		gameInfo();
		init();
		drawTicTacToeBoard();
		while(!isGameOver()) {
			if(isHumanTurn()) {
				Position move = takeInputFromHuman();
				makeMove(move);
			}else {
				ArrayList<Position> possibleMoves = getPossibleMoves();
//				for(int j = 0; j < possibleMoves.size(); j++) {
//					System.out.println(possibleMoves.get(j));
//				}	
				ArrayList<Position> copyOfPossibleMoves =  new ArrayList<Position>();

				for(int i = 0; i < numberOfSimulations; i++) {
					for(int j = 0; j < possibleMoves.size(); j++) {
						copyOfPossibleMoves.add(new Position(possibleMoves.get(j).x, possibleMoves.get(j).y));
					}	
					
					simulate(copyOfPossibleMoves, i);
					restoreBoard(possibleMoves);
					restorePlayer();
					copyOfPossibleMoves.clear();
					
					
				}
				for(int i = 0; i < 3; i++) {
					for(int j = 0; j < 3; j++) {
						System.out.print(score[i][j]+" , ");
					}
				}
				System.out.println();
				Position bestMove = getBestPosition();
				System.out.println("best move :"+bestMove.x+","+bestMove.y);
				makeMove(bestMove);
				player = 1;
				resetScore();
				
			}
			drawTicTacToeBoard();
			switchPlayer();
		}
		int result = result();
		if(result == 0) System.out.println("Human Won");
		if(result == 1) System.out.println("AI Won");
		if(result == 2) System.out.println("Draw");
			
	}
	
	public void gameInfo() {
		System.out.println("TIC-TAC-TOE with Monte-Carlo-Tree-Simulation(MCTS)");
		System.out.println("===================================================\n");
		System.out.println("RULES FOR TIC-TAC-TOE");
		System.out.println("-------------------------------------");
		System.out.println("1. Board is made up of 3 by 3 squares.");

		System.out.println("2. You are X, AI is O. Players take turn putting their mark in an empty squares.");

		System.out.println("3. The first player to get 3 of their marks in a row or column or diagonal is the winner.");

		System.out.println("4. If the board gets full and rule number 3 is not satisfied then the game is draw.");
		
			
		System.out.println("-------------------------------------");
		
	}
	
	public void resetScore() {
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				score[i][j] = 0.0f;
			}
		}
	}
	
	public Position getBestPosition(){
		Position best = null;
		float maxScore = Integer.MIN_VALUE;
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				if(score[i][j] >= maxScore && board[i][j] == ' ') {
					maxScore = score[i][j];
					best = new Position(i, j);
		
				}
			}
		}
		return best;
	}
	
	public void restorePlayer() {
		player = 1;
	}
	
	public void restoreBoard(ArrayList<Position> possibleMoves) {
		for(int i = 0; i < possibleMoves.size(); i++) {
			Position possibleMove = possibleMoves.get(i);
			board[possibleMove.x][possibleMove.y] = ' ';
		}
	}
	
	public void init() {
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				board[i][j] = ' ';					
			}
		}
	}
	
	public boolean isGameOver() {
		if(hasWon('X') || hasWon('O') || isDraw()) return true;
		return false;

	}
	
	

	// 0 = Human Won
	// 1 = AI Won
	// 2 = Draw
	public int result() {
		if(hasWon('X')) return HUMAN_WON;
		if(hasWon('O')) return AI_WON;
		return DRAW;
	}
	
	public boolean isDraw() {
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				if(board[i][j] == ' ') 
					return false;
			}
		}
		return true;
	}
	
	public boolean hasWon(char playerMarker) {
		// For rows
		if(board[0][0] == playerMarker && board[0][1] == playerMarker && board[0][2] == playerMarker)
			return true;
		if(board[1][0] == playerMarker && board[1][1] == playerMarker && board[1][2] == playerMarker)
			return true;
		if(board[2][0] == playerMarker && board[2][1] == playerMarker && board[2][2] == playerMarker)
			return true;
		
		// For cols
		if(board[0][0] == playerMarker && board[1][0] == playerMarker && board[2][0] == playerMarker)
			return true;
		if(board[0][1] == playerMarker && board[1][1] == playerMarker && board[2][1] == playerMarker)
			return true;
		if(board[0][2] == playerMarker && board[1][2] == playerMarker && board[2][2] == playerMarker)
			return true;
		
		// Diagonals
		if(board[0][0] == playerMarker && board[1][1] == playerMarker && board[2][2] == playerMarker)
			return true;
		if(board[2][0] == playerMarker && board[1][1] == playerMarker && board[0][2] == playerMarker)
			return true;
		
		return false;
	}
	
	public boolean isHumanTurn() {
		return player == 0;
	}
	
	public Position takeInputFromHuman() {
		int x;
		int y;
		boolean isValid = false;
		do {
			
		
		
		Scanner reader = new Scanner(System.in);
		System.out.println("Enter row : ");
		x = scanner.nextInt();
		System.out.println("Enter col : ");
		y = scanner.nextInt();
				
			if(x>= 0 && x<=2 && y >=0 && y<=2 && board[x][y] == ' ')
				isValid = true;
		}while(!isValid);
		
		return new Position(x, y);
	}
	
	public void makeMove(Position position) {
		board[position.x][position.y] = putPlayerMarker();
	}
	
	public char putPlayerMarker() {
		return player == 0 ? HUMAN_MARKER : AI_MARKER;
	}
	
	public void switchPlayer() {
		player = 1 - player;
	}
	
	public void drawTicTacToeBoard() {
		System.out.println("Row and column of each positions are given below:");
		System.out.println("_________________________________________________");
		System.out.print("(0,0)"+" | "+"(0,1)"+" | "+"(0,2)");
		System.out.println("");
		System.out.println("-----------------------");
		System.out.print("(1,0)"+" | "+"(1,1)"+" | "+"(1,2)");
		System.out.println("");
		System.out.println("-----------------------");
		System.out.print("(2,0)"+" | "+"(2,1)"+" | "+"(2,2)");
		System.out.println("");
		
		System.out.println("======================================");
		System.out.print(board[0][0]+" | "+board[0][1]+" | "+board[0][2]);
		System.out.println("");
		System.out.println("-----------");
		System.out.print(board[1][0]+" | "+board[1][1]+" | "+board[1][2]);
		System.out.println("");
		System.out.println("-----------");
		System.out.print(board[2][0]+" | "+board[2][1]+" | "+board[2][2]);
		System.out.println("");
		
		System.out.println("======================================");
	}
	
	public ArrayList<Position> getPossibleMoves(){
		ArrayList<Position> possibleMoves = new ArrayList<Position>(); 
		String s = "";
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				if(board[i][j] == ' ') {
					possibleMoves.add(new Position(i,j));
				}					
			}
		}
		System.out.println(s);
		return possibleMoves;
	}
	
	public void simulate(ArrayList<Position> possibleMoves, int simulation) {
		Position move  = possibleMoves.get(simulation % possibleMoves.size());
		makeMove(move);
		switchPlayer();
		possibleMoves.remove(simulation % possibleMoves.size());
		
		while(!isGameOver()) {
			// simulate
			int possibleMovesSize = possibleMoves.size();
			int moveID = rand.nextInt(possibleMovesSize);
			Position moves = possibleMoves.get(moveID);
			makeMove(moves);
			possibleMoves.remove(moveID);
			switchPlayer();
		}
		
		int result = result();
		if(result == 0) score[move.x][move.y] -= 1f;
		if(result == 1) score[move.x][move.y] += 1f;

	}
}
