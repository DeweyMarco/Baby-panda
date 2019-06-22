import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Scanner;

public class Loa {

	private static final int W = 1500;
	private static final int H = 1500;
	private static int size = 0;
	private static int mode = 0;
	// private static String S = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public static void main(String[] args) {
		if (args.length < 2) {
			reportErrorAndTerminate("too few arguments");
		}
		size = Integer.parseInt(args[0]);
		if ((size < 4) || (size > 16)) {
			reportErrorAndTerminate("illegal size");
		}
		mode = Integer.parseInt(args[1]);
		if (mode == 0) {
			testMode();
		} else if (mode == 1) {
			singleMode();
		} else if (mode == 2) {
			multiMode(args[2]);
		} else if (mode == 3) {
			guiMode();
		} else {
			reportErrorAndTerminate("illegal mode");
		}
	}

	private static void reportAndTerminate(String message) {
		System.out.println(message);
		System.exit(-1);
	}

	private static void reportErrorAndTerminate(String message) {
		reportAndTerminate("ERROR: " + message);
	}

	private static void testMode() {
		Scanner s = new Scanner(System.in);
		int nextPlayer = Board.BLACK;
		boolean done = false;
		Board.newBoard(size);
		showBoard();
		while (!done) {
			String move = s.next();
			if (move.length() != 4) {
				reportErrorAndTerminate("illegal move");
			}
			if (move.equals("QUIT")) {
				reportAndTerminate("player quit");
			}
			int srcRow = move.charAt(0) - 'A';
			int srcCol = move.charAt(1) - 'A';
			int dstRow = move.charAt(2) - 'A';
			int dstCol = move.charAt(3) - 'A';
			if (!Board.isValidMove(nextPlayer, srcRow, srcCol, dstRow, dstCol)) {
				reportErrorAndTerminate("invalid move");
			}
			Board.makeMove(nextPlayer, srcRow, srcCol, dstRow, dstCol);
			showBoard();
			if (Board.hasWon(nextPlayer)) {
				done = true;
			} else if (nextPlayer == Board.BLACK) {
				nextPlayer = Board.WHITE;
			} else {
				nextPlayer = Board.BLACK;
			}
		}
		if (nextPlayer == Board.BLACK) {
			reportAndTerminate("WINNER: black");
		} else {
			reportAndTerminate("WINNER: white");
		}
		s.close();
	}

	private static void singleMode() {
		Scanner s = new Scanner(System.in);
		int nextPlayer = Board.BLACK;
		boolean done = false;
		Board.newBoard(size);
		showBoard();
		while (!done) {
			if (nextPlayer == Board.BLACK) {
				String move = s.next();
				if (move.length() != 4) {
					reportErrorAndTerminate("illegal move");
				}
				if (move.equals("QUIT")) {
					reportAndTerminate("player quit");
				}
				int srcRow = move.charAt(0) - 'A';
				int srcCol = move.charAt(1) - 'A';
				int dstRow = move.charAt(2) - 'A';
				int dstCol = move.charAt(3) - 'A';
				if (!Board.isValidMove(nextPlayer, srcRow, srcCol, dstRow, dstCol)) {
					reportErrorAndTerminate("invalid move");
				}
				Board.makeMove(nextPlayer, srcRow, srcCol, dstRow, dstCol);
			} else {
				Player.makeMove(nextPlayer);
			}
			showBoard();
			if (Board.hasWon(nextPlayer)) {
				done = true;
			} else if (nextPlayer == Board.BLACK) {
				nextPlayer = Board.WHITE;
			} else {
				nextPlayer = Board.BLACK;
			}
		}
		if (nextPlayer == Board.BLACK) {
			reportAndTerminate("WINNER: black");
		} else {
			reportAndTerminate("WINNER: white");
		}
		s.close();
	}
	
	private static String getIpAddress() {
		try {
			Enumeration<NetworkInterface> n = NetworkInterface.getNetworkInterfaces();
			for (; n.hasMoreElements();) {
				NetworkInterface e = n.nextElement();
				Enumeration<InetAddress> a = e.getInetAddresses();
				for (; a.hasMoreElements();) {
					InetAddress addr = a.nextElement();
					String address = addr.getHostAddress();
					if (address.indexOf('.') >= 0) {
						try {
							String[] components = address.split("\\.");
							int first = Integer.parseInt(components[0]);
							if ((first > 0) && (first < 256) && (first != 127)) {
								return address;
							}
						} catch (NumberFormatException x) {
							// ignore
						}
					}
				}
			}
		} catch (SocketException x) {
			// ignore
		}
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException x) {
			// ignore
		}
		return "127.0.0.1";
	}

	private static void showBoard() {
		System.out.print("  ");
		for (int c = 0; c < size; c++) {
			System.out.print((char) ('A' + c));
			System.out.print(' ');
		}
		System.out.println();
		for (int r = size - 1; r >= 0; r--) {
			System.out.print((char) ('A' + r) + " ");
			for (int c = 0; c < size; c++) {
				int piece = Board.getPiece(r, c);
				if (piece == Board.BLACK) {
					System.out.print("B");
				} else if (piece == Board.WHITE) {
					System.out.print("W");
				} else {
					System.out.print(".");
				}
				System.out.print(' ');
			}
			System.out.println();
		}
	}

	private static void multiMode(String host) {
		Scanner s = new Scanner(System.in);
		int nextPlayer = Board.BLACK;
		int otherPlayer = Board.WHITE;
		int turn = 0;
		boolean done = false;
		String r = getIpAddress();
		int c = Networking.connect(r);
		if (c == -1) {
			System.out.println("Error connecting");
		}
		else if (c == 0) {
			nextPlayer = Board.WHITE;
			otherPlayer = Board.BLACK;
			turn = 1;
		}
		while (!done) {
			while (turn == 0) {
				String move = s.next();
				if (move.length() != 4) {
					reportErrorAndTerminate("illegal move");
				}
				if (move.equals("QUIT")) {
					reportAndTerminate("player quit");
				}
				int srcRow = move.charAt(0) - 'A';
				int srcCol = move.charAt(1) - 'A';
				int dstRow = move.charAt(2) - 'A';
				int dstCol = move.charAt(3) - 'A';
				if (!Board.isValidMove(nextPlayer, srcRow, srcCol, dstRow, dstCol)) {
					reportErrorAndTerminate("invalid move");
				}
				Board.makeMove(nextPlayer, srcRow, srcCol, dstRow, dstCol);
				showBoard();
				Networking.write(move);
				turn = 1;
				if (Board.hasWon(nextPlayer)) {
					done = true;
				}
			}
			while (turn == 1) {
				String move = Networking.read();
				System.out.println(move);
				int srcRow = move.charAt(0) - 'A';
				int srcCol = move.charAt(1) - 'A';
				int dstRow = move.charAt(2) - 'A';
				int dstCol = move.charAt(3) - 'A';
				Board.makeMove(otherPlayer, srcRow, srcCol, dstRow, dstCol);
				if (Board.hasWon(otherPlayer)) {
					done = true;
				}
				showBoard();
				turn = 0;
			}
		}
		if (nextPlayer == Board.BLACK) {
			reportAndTerminate("WINNER: black");
		} else {
			reportAndTerminate("WINNER: white");
		}
	}

	private static void guiMode() {
		Scanner s = new Scanner(System.in);
		int nextPlayer = Board.BLACK;
		boolean done = false;
		Board.newBoard(size);
		while (!done) {
			if (nextPlayer == Board.BLACK) {
				showGuiBoard(nextPlayer);
			} else {
				Player.makeMove(nextPlayer);
			}
			if (Board.hasWon(nextPlayer)) {
				done = true;
			} else if (nextPlayer == Board.BLACK) {
				nextPlayer = Board.WHITE;
			} else {
				nextPlayer = Board.BLACK;
			}
		}
		if (nextPlayer == Board.BLACK) {
			reportAndTerminate("WINNER: black");
		} else {
			reportAndTerminate("WINNER: white");
		}
		s.close();
	}

	private static void showGuiBoard(int nextPlayer) {
		StdDraw.setCanvasSize(W, H);
		StdDraw.setXscale(0, W);
		StdDraw.setYscale(0, H);
		double x = W * 1.0 / size;
		double y = H * 1.0 / size;
		printGui();
		boolean moveMade = false;
		while (!StdDraw.mousePressed()) {
		}
		int frow = 0;
		int fcol = 0;
		while (!moveMade) {
			boolean firstPiece = false;
			while (!firstPiece) {
				while (StdDraw.mousePressed()) {
					double xx = StdDraw.mouseX();
					double yy = StdDraw.mouseY();
					frow = convertClick(xx);
					fcol = convertClick(yy);
					if (Board.getPiece(frow, fcol) == nextPlayer) {
						firstPiece = true;
						StdDraw.setPenRadius(0.05);
						StdDraw.setPenColor(StdDraw.DARK_GRAY);
						StdDraw.square(frow * x + (x / 2), fcol * y + (y / 2), (x / 2));
						for (int i = 0; i < size; i++) {
							for (int j = 0; j < size; j++) {
								if (Board.isValidMove(nextPlayer, frow, fcol, i, j)) {
									StdDraw.setPenColor(StdDraw.GREEN);
									StdDraw.square(i * x + (x / 2), j * y + (y / 2), (x / 2));
								}
							}
						}
					}
				}
			}
			while (!StdDraw.mousePressed()) {
			}
			while (StdDraw.mousePressed()) {
				double xxx = StdDraw.mouseX();
				double yyy = StdDraw.mouseY();
				int trow = convertClick(xxx);
				int tcol = convertClick(yyy);
				if (!Board.isValidMove(nextPlayer, frow, fcol, trow, tcol)) {

				} else {
					Board.makeMove(nextPlayer, frow, fcol, trow, tcol);
					moveMade = true;
				}
			}
		}
	}
	private static void printGui() {
		double x = W * 1.0 / size;
		double y = H * 1.0 / size;
		for (int i = 0; i <= size; i++) {
			for (int j = 0; j <= size; j++) {
				if ((j + i) % 2 == 0) {
					int r = (int) (Math.random() * 40) + 100;
					int g = (int) (Math.random() * 40);
					int b = (int) (Math.random() * 40) + 130;
					StdDraw.setPenColor(r, g, b);
				} else {
					StdDraw.setPenColor(StdDraw.BLACK);
				}
				StdDraw.filledSquare(i * x + (x / 2), j * y + (y / 2), (x / 2));

				int g = Board.getPiece(i, j);
				if (g == 1) {
					StdDraw.setPenColor(StdDraw.WHITE);
					StdDraw.filledCircle(i * x + (x / 2), j * y + (y / 2), (x / 2));
				}
				if (g == 2) {
					StdDraw.setPenColor(StdDraw.GRAY);
					StdDraw.filledCircle(i * x + (x / 2), j * y + (y / 2), (x / 2));

				}
			}
		}
	}

	private static int convertClick(double xx) {
		double x = W / size;
		int final1 = 0;
		boolean go = true;
		while (go == true)
			for (int i = 0; i < size; i++) {
				if ((i * x < xx) && (xx < (i + 1) * x)) {
					final1 = i;
					go = false;
				}
			}
		return final1;
	}

}
