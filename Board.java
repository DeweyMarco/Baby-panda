

public class Board {

	// The size of the board
	private static int size;

	// The pieces
	public static final int INVALID = -1;
	public static final int EMPTY = 0;
	public static final int WHITE = 1;
	public static final int BLACK = 2;

	// The board
	private static int[][] board;
	// Convention: board[0][0] is the southwest square

	/*
	 * Create and set up a new board
	 */
	public static void newBoard(int newSize) {
	
		size = newSize;
		board = new int[size][size];

		for (int r = 0; r < size; r++) {
			for (int c = 0; c < size; c++) {
				if (((r == 0) || (r == size - 1)) && ((c != 0) && (c != size - 1))) {
					board[r][c] = 2;
				} else if (((c == 0) || (c == size - 1)) && ((r != 0) && (r != size - 1))) {
					board[r][c] = 1;
				} else {
					board[r][c] = 0;
				}
			}
		}
	}

	/*
	 * Function that returns the piece currently on the board at the specified row
	 * and column.
	 */
	public static int getPiece(int row, int col) {
		
		if ((row < 0) || (row >= size)) {
			return INVALID;
		}
		if ((col < 0) || (col >= size)) {
			return INVALID;
		}
		return board[row][col];
	}

	/*
	 * Make a move. Check that the move is valid. If not, return false. If valid,
	 * modify the board that the piece has moved from (fromRow, fromCol) to (toRow,
	 * toCol).
	 */
	public static boolean makeMove(int player, int fromRow, int fromCol, int toRow, int toCol) {
		
		if (isValidMove(player, fromRow, fromCol, toRow, toCol) == true) {
			if (player == 1) {
				board[toRow][toCol] = 1;
				board[fromRow][fromCol] = 0;
			} else if (player == 2) {
				board[toRow][toCol] = 2;
				board[fromRow][fromCol] = 0;
			}
		}
		return false;
	}

	/*
	 * Return the size of the board.
	 */
	public static int getSize() {
		return size;
	}

	/*
	 * Check if the given move is valid. This entails checking that:
	 * 
	 * - the player is valid
	 * 
	 * - (fromRow, fromCol) is a valid coordinate
	 * 
	 * - (toRow, toCol) is a valid coordinate
	 * 
	 * - the from square contains a marker that belongs to the player
	 * 
	 * - check that we are moving a "legal" number of squares
	 */
	public static boolean isValidMove(int player, int fromRow, int fromCol, int toRow, int toCol) {
		if (!isLeagleCoordinate(fromRow, fromCol, toRow, toCol)) {
			return false;
		}
		if (!isLeaglePlayer(player, fromRow, fromCol)) {
			return false;
		}
		if (!isLeagleMove(fromRow, fromCol, toRow, toCol)) {
			return false;
		}
		if (!isExtraLeagleMove(player,fromRow, fromCol, toRow, toCol)) {
			return false;
		}
		else {
			return true;
		}
	}

	/*
	 * Count the number of markers (non-empty squares) in the specified row.
	 */
	public static int rowCount(int row) {
		int R = 0;
		for (int i = 0; i < size; i++) {
			if ((board[row][i] == 1) || (board[row][i] == 2)) {
				R++;
			}
		}
		return R;
	}

	/*
	 * Count the number of markers (non-empty squares) in the specified column.
	 */
	public static int colCount(int col) {
		int C = 0;
		for (int i = 0; i < size; i++) {
			if (board[i][col] > 0) {
				C+=1;
			}
		}
		return C;
	}

	/*
	 * Count the number of markers (non-empty squares) in the diagonal that runs
	 * from the north-east corner to the south-west corner of the board, and that
	 * passes through the specified row and column.
	 */
	public static int diagNortheastCount(int row, int col) {
		int sr = row;
		int sc = col;
		while(sc > 0 && sr > 0) {
		sc--;
		sr--;
		}
		int count = 0;
		for (int j = 0; (sr + j < size) && (sc + j < size); j++) {
			if(board[sr + j][sc + j] > 0) {
				count++;
			}
		}
		return count;		
		
	}

	/*
	 * Count the number of markers (non-empty squares) in the diagonal that runs
	 * from the north-west corner to the south-east corner of the board, and that
	 * passes through the specified row and column.
	 */
	public static int diagNorthwestCount(int row, int col) {
		int sr = row;
		int sc = col;
		while(sc > 0 && sr < size - 1) {
		sc--;
		sr++;
		}
		int count = 0;
		for (int j = 0; (sr - j > -1) && (sc + j < size); j++) {
			if(board[sr - j][sc + j] > 0) {
				count++;
			}
		}
		return count;
		
	}

	public static boolean hasWon(int player) {
		return Util.isConnected(board, player);
	}
	/*
	 * The checks if the move distance is equal to the number of pieces in that direction
	 */
	public static boolean isLeagleMove(int fromRow, int fromCol, int toRow, int toCol) {
		int Right = Math.abs(fromCol - toCol);
		int Up = Math.abs(fromRow - toRow);

		if (Right == 0 && Up == colCount(fromCol)) {
				return true;
		}

		if (Right == rowCount(fromRow) && Up == 0) {
				return true;
		}

		if (Right != 0 && Up != 0) {
			int checkRight = fromCol - toCol;
			int checkUp = fromRow - toRow;
			if((checkRight*checkUp<0)){
				if (Right == diagNorthwestCount(fromRow, fromCol) && Up == diagNorthwestCount(fromRow, fromCol)) {
					return true;
				}
				else {
					return false;
				}
			}
			if ((checkRight*checkUp>0)) {
				if (Right == diagNortheastCount(fromRow, fromCol) && Up == diagNortheastCount(fromRow, fromCol)) {
					return true;
				}
				else {
					return false;
				}
			}
			else {
				return false;
			}
		} 
		else {
			return false;
		}
	}
	/*
	 * Checks if the move is being made to the correct piece
	 */
	public static boolean isLeaglePlayer(int player, int fromRow, int fromCol) {
		if (player == board[fromRow][fromCol]) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isLeagleCoordinate(int fromRow, int fromCol, int toRow, int toCol) {
		 if (board[fromRow][fromCol] != board[toRow][toCol]) {
			if ((fromRow < size) && (fromCol < size) && (toRow < size) && (toCol < size)) {
				return true;
			}
		}
		return false;
	}
 /*
  * Checking if the move "jumps over" one of its own pieces 
  */
	public static boolean isExtraLeagleMove(int Player, int fromRow, int fromCol, int toRow, int toCol) {
		int xPlayer = 0;
		if (Player == 1) {
			xPlayer = 2;
		}
		if (Player == 2) {
			xPlayer = 1;
		}
		int Right = fromCol - toCol;
		int Up = fromRow - toRow;
		if (Right < 0 && Up < 0) {
			for (int j = 1; j < Up - 1; j++) {
				if (board[fromRow + j][fromCol + j] == xPlayer) {
					return false;
				}
			}
			return true;
		}
		if (Right > 0 && Up > 0) {
			for (int j = 1; j < Up - 1; j++) {
				if (board[fromRow - j][fromCol - j] == xPlayer) {
					return false;
				}
			}
			return true;
		}
		if (Right < 0 && Up > 0) {
			for (int j = 1; j < Up - 1; j++) {
				if (board[fromRow + j][fromCol - j] == xPlayer) {
					return false;
				}
			}
			return true;
		}
		if (Right > 0 && Up < 0) {
			for (int j = 1; j < Up - 1; j++) {
				if (board[fromRow - j][fromCol + j] == xPlayer) {
					return false;
				}
			}
			return true;
		}
		if (Right == 0 && Up > 0) {
			for (int j = 1; j < Up - 1; j++) {
				if (board[fromRow - j][fromCol] == xPlayer) {
					return false;
				}
			}
			return true;
		}
		if (Right == 0 && Up < 0) {
			for (int j = 1; j < Up - 1; j++) {
				if (board[fromRow - j][toCol] == xPlayer) {
					return false;
				}
			}
			return true;
		}
		if (Right > 0 && Up == 0) {
			for (int j = 1; j < Right - 1; j++) {
				if (board[fromRow][fromCol - j] == xPlayer) {
					return false;
				}
			}
			return true;
		}
		if (Right < 0 && Up == 0) {
			for (int j = 1; j < Right - 1; j++) {
				if (board[fromRow][fromCol + j] == xPlayer) {
					return false;
				}
			}
			return true;
		}
		else {
			return true;
		}
	}

}
