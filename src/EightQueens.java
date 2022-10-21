import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class EightQueens {
    public static void main(String[] args) {
        System.out.print("Please input size of board: ");
        Runnable runnable = () -> {
            try {
                BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
                int size = Integer.parseInt(console.readLine());
                if (size < 0) System.err.println("Error! Size cannot be less than Zero! Your input size is " + size + "...");
                else {
                    System.out.print("Which algorithm do you want to solve the problem, BFS or A* (0/1)? ");
                    boolean aStar = Integer.parseInt(console.readLine()) > 0;
                    System.out.println("Process of solving problem \"8-Queens\" has started:");
                    for (int k = 0; k < 20; ++k) {
                        ChessBoard board = new ChessBoard(size);
                        ArrayList<State> solutions = aStar ? board.AStar() : board.BFS();
                        ChessBoard tmp = new ChessBoard(board.getSize());
                        tmp.setBoard(solutions.get(0));

                        /*for (int i = 0; i < Math.min(solutions.size(), 15); i++) {
                            ChessBoard tmp = new ChessBoard(board.getSize());
                            tmp.setBoard(solutions.get(i));
                            JFrame f = new JFrame("ChessBoard #" + (i + 1));
                            f.add(tmp.createGui());
                            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            f.setLocationByPlatform(true);
                            f.setResizable(false);
                            f.pack();
                            f.setMinimumSize(f.getSize());
                            f.setVisible(true);
                        }*/
                    }
                }
                console.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        SwingUtilities.invokeLater(runnable);
    }
}
