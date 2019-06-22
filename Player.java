/*
 * Computer player.  Makes random moves.
 */
public class Player {

	public static void makeMove(int nextPlayer) {
		int s = Board.getSize();
		int r = (int) (s * Math.random());
		int c = (int) (s * Math.random());
		while (Board.getPiece(r, c) != nextPlayer) {
			c = (c + 1) % s;
			if (c == 0) {
				r = (r + 1) % s;
			}
		}
		int tr = (int) (s * Math.random());
		int tc = (int) (s * Math.random());
		while (!Board.isValidMove(nextPlayer, r, c, tr, tc)) {
			tr = (int) (s * Math.random());
			tc = (int) (s * Math.random());
		}
		Board.makeMove(nextPlayer, r, c, tr, tc);
	}

}
