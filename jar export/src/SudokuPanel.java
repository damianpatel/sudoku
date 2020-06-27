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
    Map<JTextField, Point> mapFieldToCoordinates = new HashMap<>();

    public SudokuPanel() {
        panel2 = new JPanel();
        grid = new JTextField[9][9];
        subGridPanel = new JPanel[3][3];
        solveButton = new JButton("Solve");
        clearButton = new JButton("Clear Grid");
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

        setLayout(new BorderLayout());
        clearButton.setPreferredSize(new Dimension(10, 30));
        solveButton.setPreferredSize(new Dimension(10, 30));
        add(clearButton, BorderLayout.NORTH);
        add(solveButton, BorderLayout.SOUTH);
        add(panel2, BorderLayout.CENTER);
    }

    public void clear() {
        for (JTextField[] row : grid) {
            for (JTextField field: row) {
                field.setText("");
            }
        }
    }

    private class Listener extends Solver implements ActionListener{

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
                            arr[i][j] = Integer.parseInt(grid[i][j].getText());
                        }
                    }
                }
                final Runnable stuffToDo = new Thread(() -> {
                    solve(arr);
                    for (int i = 0; i < 9; i++) {
                        for (int j = 0; j < 9; j++) {
                            grid[i][j].setText(String.valueOf(arr[i][j]));
                        }
                    }
                });

                final ExecutorService executor = Executors.newSingleThreadExecutor();
                final Future<?> future = executor.submit(stuffToDo);
                executor.shutdown(); // This does not cancel the already-scheduled task.

                try {
                    future.get(4, TimeUnit.SECONDS);
                }
                catch (InterruptedException | ExecutionException | TimeoutException ie) {
                    JOptionPane.showMessageDialog(null, "Error");

                }
                if (!executor.isTerminated())
                    executor.shutdownNow();
            }
        }
    }
}
