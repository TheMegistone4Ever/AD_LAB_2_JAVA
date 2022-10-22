import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class ChessBoard {
    private static final int MIN_SOLUTION_SIZE = 4, INIT_POS = 0;
    private final int size, cellPx;
    private State board;
    private final JPanel boardPanel;
    private final JButton[][] cells;

    ChessBoard(int size) throws IOException {
        cellPx = 700 / (this.size = size);
        this.boardPanel = new JPanel(new GridLayout(size + 1, size + 1));
        this.board = new State(size, INIT_POS);
        this.cells = new JButton[size][size];
        for (int i = 1; i <= size; ++i) {
            for (int j = 1; j <= size; ++j) {
                JButton cell = new JButton();
                cell.setMargin(new Insets(0, 0, 0, 0));
                cell.setIcon(new ImageIcon(new BufferedImage(cellPx, cellPx, BufferedImage.TYPE_INT_ARGB)));
                cell.setBackground((1 & i + j) == 0 ? Color.WHITE : Color.GRAY);
                cells[i - 1][j - 1] = cell;
            }
        }
    }

    ArrayList<State> AStar() {
        ArrayList<State> solutions = new ArrayList<>();
        long start = System.currentTimeMillis();
        if (size < MIN_SOLUTION_SIZE)
            solutions.add(board);
        else {
            PriorityQueue<State> states = new PriorityQueue<>();
            states.add(board);
            while (!states.isEmpty()) {
                State current = states.poll();
                if (current.done()) {
                    solutions.add(current);
                    break;
                }
                int currentDepth = current.getDepth();
                if (currentDepth != size) {
                    int currentGrade = current.heuristic();
                    for (int row = INIT_POS; row < size; ++row) {
                        State node = new State(current);
                        node.put(row, currentDepth);
                        node.setDepth(currentDepth + 1);
                        if (node.heuristic() <= currentGrade)
                            states.add(node);
                    }
                }
            }
        }
        System.out.println("A* on "  + size + "x" + size + " board has found solution in "
                + (System.currentTimeMillis()-start) + "ms:");
        return solutions;
    }

    ArrayList<State> BFS() {
        ArrayList<State> solutions = new ArrayList<>();
        State solution = new State(board);
        long start = System.currentTimeMillis();
        if (size < MIN_SOLUTION_SIZE)
            solutions.add(solution);
        else {
            for (int row = INIT_POS; row >= INIT_POS;) {
                do solution.forward(row);
                while (solution.at(row) < size && solution.attacked(row));
                if (solution.at(row) < size) {
                    if (row < size - 1)
                        solution.put(++row, INIT_POS - 1);
                    else {
                        solutions.add(new State(solution));
                        break;
                    }
                } else
                    --row;
            }
        }
        System.out.println("BFS on "  + size + "x" + size + " board has found solution in "
                + (System.currentTimeMillis()-start) + "ms:");
        return solutions;
    }

    public final JComponent createGui() throws IOException {
        JPanel gui = new JPanel(new BorderLayout(3, 3));
        for (int row = INIT_POS; row <= size; ++row)
            boardPanel.add(new JLabel(Character.toString((char)(row + 64)), SwingConstants.CENTER));
        for (int row = INIT_POS; row < size; ++row) {
            boardPanel.add(new JLabel(Integer.toString(row + 1), SwingConstants.CENTER));
            for (int col = INIT_POS; col < size; boardPanel.add(cells[row][col++])) {
                if (board.at(row) == col) {
                    cells[row][col]
                            .setIcon(new ImageIcon(new ImageIcon("src\\queen.png")
                                    .getImage()
                                    .getScaledInstance(cellPx, cellPx, java.awt.Image.SCALE_SMOOTH)));
                }
            }
        }
        gui.add(boardPanel);
        return gui;
    }

    public final int getSize() {
        return size;
    }

    public State getBoard() {
        return board;
    }

    public void setBoard(State board) {
        this.board = board;
    }
}