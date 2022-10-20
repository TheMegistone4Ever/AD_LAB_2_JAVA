import java.util.Random;

public class State implements Comparable<State> {

    private final int[] board;
    private int depth;
    private final int size;

    public State(int size, int depth) {
        this.board = new int[size];
        this.depth = depth;
        this.size = size;
        Random random = new Random();
        for (int i = 0; i < size; i++) put(i, random.nextInt(size));
    }

    public State(State state) {
        this.board = state.board;
        this.depth = state.depth;
        this.size = state.size;
    }

    void put(int row, int col) {board[row] = col;}

    boolean attacked(int y) {
        for (int i = 1; i <= y; i++)
            if (board[y-i] == board[y] || board[y-i] == board[y] - i || board[y-i] == board[y] + i) return true;
        return false;
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

    public void setDepth(int depth) {
        this.depth = depth;
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

    @Override
    public int compareTo(State o) {
        return Integer.compare(this.heuristic(), o.heuristic());
    }
}