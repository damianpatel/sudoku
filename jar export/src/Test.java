public class Test {
    public static void main(String[] args) {
        int[][] board = {
                {0, 2, 2, 0, 0, 0, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0}
        };

        printBoard(board);
        System.out.println("\n\n");
        if (solve(board)) {
            printBoard(board);
        }
    }

    static boolean solve(int[][] board) {
        int row = 0;
        int col = 0;
        boolean isEmpty = true;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == 0) {
                    row = i;
                    col = j;
                    isEmpty = false;
                    break;
                }
            }
            if (!isEmpty) {
                break;
            }
        }
        if (isEmpty) {
            return true;
        }

        for (int i = 1; i < 10; i++) {
            if (valid(board, i, row, col)) {
                board[row][col] = i;

                if (solve(board)) {
                    return true;
                }
                else {
                    board[row][col] = 0;
                }
            }
        }

        return false;
    }

    static boolean valid(int[][] board, int num, int row, int col) {
        // check row/col
        for (int i = 0; i < board.length; i++) {
            if (board[row][i] == num) {
                return false; // row
            }
            if (board[i][col] == num) {
                return false; // col
            }
        }

        // check 3 x 3 box
        int boxY = row - row % 3;
        int boxX = col - col % 3;

        for (int i = boxY; i < boxY + 3; i++) {
            for (int j = boxX; j < boxX + 3; j++) {
                if (board[i][j] == num) {
                    return false;
                }
            }
        }

        return true;
    }

    static void printBoard(int[][] board) {
        for (int i = 0; i < board.length; i++) {
            if (i % 3 == 0 && i != 0) {
                System.out.println("- - - - - - - - - - - - -");
            }

            for (int j = 0; j < board[0].length; j++) {
                if (j % 3 == 0 && j != 0) {
                    System.out.print(" | ");
                }

                if (j == 8) {
                    System.out.println(board[i][j]);
                } else {
                    System.out.print(board[i][j] + " ");
                }
            }
        }
    }
}


