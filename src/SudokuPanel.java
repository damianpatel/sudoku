import java.awt.event.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import javax.swing.*;
import javax.swing.border.Border;

public class SudokuPanel extends JPanel {
    JPanel panel2;
    JTextField[][] grid;
    JPanel[][] subGridPanel;
    JButton solveButton;
    JButton clearButton;
    JPanel jBottom;
    JPanel jTop;
    Map<JTextField, Point> mapFieldToCoordinates = new HashMap<>();

    public SudokuPanel() {
        panel2 = new JPanel();
        grid = new JTextField[9][9];
        subGridPanel = new JPanel[3][3];
        solveButton = new JButton("Solve");
        clearButton = new JButton("Clear Grid");
        jBottom = new JPanel();
        jTop = new JPanel();
        solveButton.addActionListener(new Listener());
        clearButton.addActionListener(new Listener());

        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                JTextField field = new JTextField();
                mapFieldToCoordinates.put(field, new Point(x, y));
                grid[y][x] = field;
            }
        }

        Border border = BorderFactory.createLineBorder(Color.BLACK);
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                JTextField field = grid[y][x];
                field.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        char c = e.getKeyChar();
                        if (!((c > '0') && (c <= '9') ||
                                (c == KeyEvent.VK_BACK_SPACE) ||
                                (c == KeyEvent.VK_DELETE))) {
                            getToolkit().beep();
                            e.consume();
                        }
                    }
                });
                field.setHorizontalAlignment(JTextField.CENTER);
                field.setFont(new Font("Arial", Font.BOLD, 30));
                field.setBorder(border);
                field.setPreferredSize(new Dimension(30,30));
            }
        }

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                JPanel panel = new JPanel();
                panel.setLayout(new GridLayout(3, 3));
                panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                subGridPanel[y][x] = panel;
                panel2.add(panel);
            }
        }

        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                int miniX = x/3;
                int miniY = y/3;
                subGridPanel[miniY][miniX].add(grid[y][x]);
            }
        }

        panel2.setBackground(new Color(175, 237, 255));
        jTop.setBackground(new Color(175, 237, 255));
        jBottom.setBackground(new Color(175, 237, 255));
        setLayout(new BorderLayout());
        clearButton.setPreferredSize(new Dimension(10, 30));
        solveButton.setPreferredSize(new Dimension(10, 30));
        add(jBottom, "South");
        add(jTop, "North");
        jBottom.setPreferredSize(new Dimension(150, 40));
        jTop.setPreferredSize(new Dimension(150, 40));
        jBottom.add(solveButton);
        jTop.add(clearButton);
        solveButton.setPreferredSize(new Dimension(120, 30));
        clearButton.setPreferredSize(new Dimension(120, 30));
        solveButton.setFocusPainted(false);
        clearButton.setFocusPainted(false);
        solveButton.setBackground(new Color(89, 235, 103));
        clearButton.setBackground(new Color(255, 100, 100));
        add(panel2, BorderLayout.CENTER);
    }

    public void clear() {
        for (JTextField[] row : grid) {
            for (JTextField field: row) {
                field.setText("");
            }
        }
    }

    public boolean gridValid (JTextField[][] bigGrid) {
        int invalidNum = 0;
        for (int i = 0; i < 9; i ++) {
            for (int j = 0; j < 9; j++) {
                if (bigGrid[i][j].getText().length() > 1) {
                    invalidNum++;
                }
            }
        }
        return invalidNum == 0;
    }

    private class Listener extends Solver implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == clearButton) {
                clear();
            } else if (e.getSource() == solveButton) {
                int[][] arr = new int[9][9];
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        if (grid[i][j].getText().equals("")) {
                            arr[i][j] = 0;
                        } else {
                            if (grid[i][j].getText().length() <= 1) {
                                arr[i][j] = Integer.parseInt(grid[i][j].getText());
                            } else {
                                JOptionPane.showMessageDialog(null, "Error: Please Only Enter Integers Between 1 and 9 (inclusive)");
                            }
                        }
                    }
                }

                if (gridValid(grid)) {
                    final Runnable stuffToDo = new Thread(() -> {
                        solve(arr);
                        for (int x = 0; x < 9; x++) {
                            for (int y = 0; y < 9; y++) {
                                grid[x][y].setText(String.valueOf(arr[x][y]));
                            }
                        }
                    });

                    final ExecutorService executor = Executors.newSingleThreadExecutor();
                    final Future<?> future = executor.submit(stuffToDo);
                    executor.shutdown(); // This does not cancel the already-scheduled task.

                    try {
                        future.get(3, TimeUnit.SECONDS);
                    }
                    catch (InterruptedException | ExecutionException | TimeoutException ie) {
                        JOptionPane.showMessageDialog(panel2, "Error: The Integers You've Entered Violate the Rules of Sudoku");
                    }
                    if (!executor.isTerminated())
                        executor.shutdownNow();
                }
            }
        }
    }
}
