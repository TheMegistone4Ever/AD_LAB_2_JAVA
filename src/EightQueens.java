import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class EightQueens {
    public static void main(String[] args) {
        System.out.print("Please input size of board: ");
        Runnable runnable = () -> {
            try {
                int size = Integer.parseInt(new BufferedReader(new InputStreamReader(System.in)).readLine());
                if (size < 0) System.err.println("Error! Size cannot be less than Zero! Your input size is " + size + "...");
                else {
                    ChessBoard board = new ChessBoard(size);
                    System.out.println("Process of solving problem \"8-Queens\" by BFS and A* has started:");
                    List<int[]> solutions = board.BFS();
                    for (int i = 0; i < Math.min(solutions.size(), 1); i++) {
                        ChessBoard tmp = new ChessBoard(size);
                        tmp.setBoard(solutions.get(i));
                        JFrame f = new JFrame("ChessBoard #" + (i + 1));
                        f.add(tmp.getGui());
                        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        f.setLocationByPlatform(true);
                        f.setResizable(false);
                        f.pack();
                        f.setMinimumSize(f.getSize());
                        f.setVisible(true);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        SwingUtilities.invokeLater(runnable);
    }
}
