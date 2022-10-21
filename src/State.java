import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class State implements Comparable<State> {
    private final int[] board;
    private final int[][] queens; // 0 - columns 1 - main diagonals 2 - secondary diagonals
    private final int size;
    private int depth;
    private State parent;

    public State(int size, int depth, State parent) {
        this.board = new int[size];
        this.queens = new int[3][size << 1];
        this.depth = depth;
        this.size = size;
        this.parent = parent;

        List<Integer> pos = new ArrayList<>();
        for (int i = 0; i < size; i++) pos.add(i);
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            int rand = pos.remove(random.nextInt(pos.size()));
            put(i, rand);
        }
    }

    public State(State state) {
        this.board = state.board.clone();
        this.queens = state.queens;
        this.depth = state.depth;
        this.size = state.size;
        this.parent = state.parent;
    }

    void put(int row, int col) {
        board[row] = col;
    }

    boolean attacked(int y) {
        for (int i = 1; i <= y; ++i)
            if (board[y-i] == board[y] || board[y-i] == board[y] - i || board[y-i] == board[y] + i) return true;
        return false;
    }

    boolean done() {
        for (int queen = 0; queen < board.length; queen++)
            for (int enemy = 0; enemy < board.length && board[enemy] >= 0; enemy++)
                if (enemy != queen
                        && (board[queen] == board[enemy]
                        || queen + board[queen] == enemy + board[enemy]
                        || queen - board[queen] == enemy - board[enemy]))
                    return false;
        return true;
    }

    int heuristic() {
        int conflicts = 0;
        for (int queen = 0; queen < board.length; queen++)
            for (int enemy = 0; enemy < board.length && board[enemy] >= 0; enemy++)
                if (enemy != queen
                        && (board[queen] == board[enemy]
                        || queen + board[queen] == enemy + board[enemy]
                        || queen - board[queen] == enemy - board[enemy]))
                    conflicts++;
        return conflicts >> 1;
    }

    public int at(int row) {
        return board[row];
    }

    public void forward(int row) {
        board[row]++;
    }

    public void back(int row) {
        board[row]--;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void setParent(State parent) {
        this.parent = parent;
    }

    public int[] getBoard() {
        return board;
    }

    public int getDepth() {
        return depth;
    }

    public int getSize() {
        return size;
    }

    public State getParent() {
        return parent;
    }

    @Override
    public int compareTo(State o) {
        return Integer.compare(this.heuristic(), o.heuristic());
    }
}