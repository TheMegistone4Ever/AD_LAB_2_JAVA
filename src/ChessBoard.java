import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class ChessBoard {
    int size, cellPx;
    State board;
    final JPanel boardPanel;
    JButton[][] cells;

    ChessBoard(int size) throws IOException {
        this.cellPx = (80 << 3) / (this.size = size);
        this.boardPanel = new JPanel(new GridLayout(size+1, size+1));
        this.board = new State(size, 0);
        this.cells = new JButton[size][size];
        for (int i = 1; i <= size; i++)
            for (int j = 1; j <= size; j++) {
                JButton cell = new JButton();
                cell.setMargin(new Insets(0,0,0,0));
                cell.setIcon(new ImageIcon(new BufferedImage(cellPx, cellPx, BufferedImage.TYPE_INT_ARGB)));
                cell.setBackground((1 & i + j) == 0 ? Color.WHITE : Color.GRAY);
                cells[i-1][j-1] = cell;
            }
    }

    List<State> AStar() {
        long start = System.currentTimeMillis();
        List<State> solutions = new ArrayList<>();
        if (size < 4) solutions.add(board);
        else { // todo A*
            PriorityQueue<State> states = new PriorityQueue<>();
            states.add(board);
            while (!states.isEmpty()) {
                State current = states.poll();
                if (current.heuristic() == 0) {solutions.add(current);break;}
                int currentDepth = current.getDepth();
                if (currentDepth != size)
                    for (int row = 0; row < size; row++) {
                        State newState = new State(current);
                        newState.put(row, currentDepth);
                        newState.setDepth(currentDepth + 1);
                        newState.setParent(current);
                        states.add(newState);
                    }
            }
        }
        System.out.println("A* on "  + size + "x" + size + " board has found solution in "
                + (System.currentTimeMillis()-start) + "ms...");
        return solutions;
    }

    List<State> BFS() {
        long start = System.currentTimeMillis();
        List<State> solutions = new ArrayList<>();
        if (size < 4) solutions.add(board);
        else for (int queen = 0; queen >= 0;) {
                do board.forward(queen); while (board.at(queen) < size && board.attacked(queen));
                if (board.at(queen) < size)
                    if (queen < size - 1) board.put(++queen, -1);
                    else {solutions.add(board); break;}
                else queen--;
            }
        System.out.println("BFS on "  + size + "x" + size + " board has found solution in "
                + (System.currentTimeMillis()-start) + "ms...");
        return solutions;
    }

    public final JComponent createGui() throws IOException {
        JPanel gui = new JPanel(new BorderLayout(3, 3));
        for (int row = 0; row <= size; row++)
            boardPanel.add(new JLabel(Character.toString((char)(row + 64)), SwingConstants.CENTER));
        for (int row = 0; row < size; row++) {
            boardPanel.add(new JLabel(Integer.toString(row+1), SwingConstants.CENTER));
            for (int col = 0; col < size; boardPanel.add(cells[row][col++]))
                if (board.at(row) == col) cells[row][col].setIcon(new ImageIcon("src\\queen.png"));
        }
        gui.add(boardPanel);
        return gui;
    }

    public void setBoard(State board) {
        this.board = board;
    }
}