import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChessBoard {
    int size, cellPx;
    int[] board;
    final JPanel gui, boardPanel;
    JButton[][] cells;

    ChessBoard(int size) throws IOException {
        this.size = size;
        cellPx = (80 << 3) / size;
        gui = new JPanel(new BorderLayout(3, 3));
        boardPanel = new JPanel(new GridLayout(size+1, size+1));
        board = new int[size];
        cells = new JButton[size][size];
        for (int i = 1; i <= size; i++) {
            for (int j = 1; j <= size; j++) {
                JButton cell = new JButton();
                cell.setMargin(new Insets(0,0,0,0));
                cell.setIcon(new ImageIcon(new BufferedImage(cellPx, cellPx, BufferedImage.TYPE_INT_ARGB)));
                cell.setBackground((1 & i + j) == 0 ? Color.WHITE : Color.GRAY);
                cells[i-1][j-1] = cell;
            }
        }
    }

    public final JComponent getGui() throws IOException {
        for (int i = 0; i <= size; i++)
            boardPanel.add(new JLabel(Character.toString((char)(i + 64)), SwingConstants.CENTER));
        for (int i = 0; i < size; i++) {
            boardPanel.add(new JLabel(Integer.toString(i+1), SwingConstants.CENTER));
            for (int j = 0; j < size; boardPanel.add(cells[i][j++]))
                if (board[i] == j) cells[i][j].setIcon(new ImageIcon("src\\queen.png"));
        }
        gui.add(boardPanel);
        return gui;
    }

    void setBoard(int[] board) {
        size = board.length;
        this.board = board;
    }

    int heuristic(int[] board) {
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (board[i] < 0) return 0;
            for (int enemy = 0; enemy < size && board[enemy] >= 0; enemy++)
                if (enemy != i
                        && (board[i] == board[enemy]
                        || i + board[i] == enemy + board[enemy]
                        || i - board[i] == enemy - board[enemy]))
                    count++;
        }
        return count >> 1;
    }

    boolean attacked(int y) {
        for (int i = 1; i <= y; i++)
            if (board[y-i] == board[y] || board[y-i] == board[y] - i || board[y-i] == board[y] + i)
                return true;
        return false;
    }

    List<int[]> BFS() {
        long start = System.currentTimeMillis();
        List<int[]> solutions = new ArrayList<>();
        if (size < 4) solutions.add(initBoard());
        else {
            putQueen(0, -1);
            for (int y = 0; y >= 0; ) {
                do board[y]++; while (board[y] < size && attacked(y));
                if (board[y] < size)
                    if (y < size - 1) putQueen(++y, -1);
                    else solutions.add(board.clone());
                else y--;
            }
        }
        System.out.println("BFS on "  + size + " by " + size + " board has found all solutions in "
                + (System.currentTimeMillis()-start) + "ms...");
        return solutions;
    }

    int[] initBoard() {
        int[] resBoard = new int[size];
        for (int i = 0; i < size; i++) putQueen(i, (int)(Math.random() * size));
        return resBoard;
    }

    void putQueen(int xAxis, int yAxis) {
        board[xAxis] = yAxis;
    }
}
