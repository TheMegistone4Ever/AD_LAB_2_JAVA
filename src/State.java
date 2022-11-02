import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class State implements Comparable<State> {
    private final int[] board;
    private final int size;
    private int depth;

    public State(int size, int depth) {
        this.board = new int[this.size = size];
        this.depth = depth;
        Random random = new Random();
        for (int i = 0; i < size; ++i) put(i, random.nextInt(size));
    }

    public State(State state) {
        this.board = state.board.clone();
        this.depth = state.depth;
        this.size = state.size;
    }

    void put(int row, int col) {
        board[row] = col;
    }

    boolean attacked(int row) {
        for (int i = 1; i <= row; ++i)
            if (board[row - i] == board[row] || board[row - i] == board[row] - i || board[row - i] == board[row] + i)
                return true;
        return false;
    }

    boolean done() {
        for (int queen = 0; queen < board.length; ++queen)
            for (int enemy = 0; enemy < board.length && board[enemy] >= 0; ++enemy)
                if (enemy != queen
                        && (board[queen] == board[enemy]
                        || queen + board[queen] == enemy + board[enemy]
                        || queen - board[queen] == enemy - board[enemy]))
                    return false;
        return true;
    }

    int heuristic() {
        int conflicts = 0;
        for (int queen = 0; queen < board.length; ++queen)
            for (int enemy = 0; enemy < board.length && board[enemy] >= 0; ++enemy)
                if (enemy != queen
                        && (board[queen] == board[enemy]
                        || queen + board[queen] == enemy + board[enemy]
                        || queen - board[queen] == enemy - board[enemy]))
                    ++conflicts;
        return (conflicts >> 1) + depth;
    }

    public int at(int row) {
        return board[row];
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getDepth() {
        return depth;
    }

    @Override public int compareTo(State o) {
        return Integer.compare(this.heuristic(), o.heuristic());
    }

    @Override public String toString() {
        return  depth + "d" + Arrays.toString(board) + "b";
    }
}