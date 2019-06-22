

public class Util {

	public static boolean isConnected(int[][] matrix, int value) {
		int rows = matrix.length;
		int columns = matrix[0].length;
		int minR = -1, maxR = -1, startR = -1;
		int minC = -1, maxC = -1, startC = -1;
		int count = 0;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < columns; c++) {
				if (matrix[r][c] == value) {
					if ((startR == -1) || (c < startC) || ((c == startC) && (r < startR))) {  
						startR = r; startC = c;
					}
					if ((minR == -1) || (r < minR)) {
						minR = r;
					}
					if ((maxR == -1) || (r > maxR)) {
						maxR = r;
					}
					if ((minC == -1) || (c < minC)) {
						minC = c;
					}
					if ((maxC == -1) || (c > maxC)) {
						maxC = c;
					}
					count++;
				}
			}
		}
		if (count == 0) {
			return true;
		}
		boolean[][] visited = new boolean[rows][columns];
		int found = connectedExplore(startR, startC, visited, matrix, value, minR, maxR, minC, maxC);
		return (found == count);
	}

	public static int connectedExplore(int r, int c, boolean[][] visited, int[][] matrix, int value, int minR, int maxR,
			int minC, int maxC) {
		if ((r < minR) || (r > maxR)) { return 0; }
		if ((c < minC) || (c > maxC)) { return 0; }
		if (visited[r][c]) { return 0; }
		visited[r][c] = true;
		if (matrix[r][c] != value) { return 0; }
		int reachableCount = 1;
		reachableCount += connectedExplore(r - 1, c + 0, visited, matrix, value, minR, maxR, minC, maxC);
		reachableCount += connectedExplore(r + 1, c + 0, visited, matrix, value, minR, maxR, minC, maxC);
		reachableCount += connectedExplore(r - 1, c + 1, visited, matrix, value, minR, maxR, minC, maxC);
		reachableCount += connectedExplore(r + 0, c + 1, visited, matrix, value, minR, maxR, minC, maxC);
		reachableCount += connectedExplore(r + 1, c + 1, visited, matrix, value, minR, maxR, minC, maxC);
		return reachableCount;
	}

}