import javax.swing.*;
import java.awt.*;

public class Sudoku {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        SudokuPanel panel = new SudokuPanel();
        frame.getContentPane().add(panel);
        frame.setPreferredSize(new Dimension(325, 400));
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }
}
