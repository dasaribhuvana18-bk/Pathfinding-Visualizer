import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Queue;

public class Main extends JPanel implements MouseListener, KeyListener {

    // Grid Settings
    private final int ROWS = 20;
    private final int COLS = 20;
    private final int SIZE = 30;

    // Node Class
    class Node {
        int row, col;

        boolean visited;
        boolean wall;
        boolean path;

        Node parent;

        Node(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    // Grid
    private Node[][] grid;

    // Start & End Nodes
    private Node start;
    private Node end;

    // Constructor
    public Main() {

        setPreferredSize(new Dimension(COLS * SIZE, ROWS * SIZE));

        grid = new Node[ROWS][COLS];

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                grid[r][c] = new Node(r, c);
            }
        }

        // Start and End Positions
        start = grid[2][2];
        end = grid[15][15];

        addMouseListener(this);
        addKeyListener(this);

        setFocusable(true);
    }

    // Paint Grid
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        for (int r = 0; r < ROWS; r++) {

            for (int c = 0; c < COLS; c++) {

                Node node = grid[r][c];

                // Colors
                if (node == start) {
                    g.setColor(Color.GREEN);
                }
                else if (node == end) {
                    g.setColor(Color.RED);
                }
                else if (node.path) {
                    g.setColor(Color.YELLOW);
                }
                else if (node.wall) {
                    g.setColor(Color.BLACK);
                }
                else if (node.visited) {
                    g.setColor(Color.CYAN);
                }
                else {
                    g.setColor(Color.WHITE);
                }

                // Draw Cell
                g.fillRect(c * SIZE, r * SIZE, SIZE, SIZE);

                // Grid Border
                g.setColor(Color.GRAY);
                g.drawRect(c * SIZE, r * SIZE, SIZE, SIZE);
            }
        }

        repaint();
    }

    // Clear Grid
    private void clearGrid() {

        for (int r = 0; r < ROWS; r++) {

            for (int c = 0; c < COLS; c++) {

                grid[r][c].visited = false;
                grid[r][c].path = false;
                grid[r][c].parent = null;
            }
        }
    }

    // BFS Algorithm
    private void bfs() {

        clearGrid();

        Queue<Node> queue = new LinkedList<>();

        start.visited = true;
        queue.add(start);

        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};

        while (!queue.isEmpty()) {

            Node current = queue.poll();

            // End Found
            if (current == end) {
                createPath(end);
                return;
            }

            // Explore Neighbors
            for (int i = 0; i < 4; i++) {

                int nr = current.row + dr[i];
                int nc = current.col + dc[i];

                if (isValid(nr, nc)) {

                    Node neighbor = grid[nr][nc];

                    if (!neighbor.visited && !neighbor.wall) {

                        neighbor.visited = true;
                        neighbor.parent = current;

                        queue.add(neighbor);

                        repaint();

                        try {
                            Thread.sleep(30);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    // Create Final Path
    private void createPath(Node end) {

        Node current = end;

        while (current != null) {

            current.path = true;

            current = current.parent;

            repaint();

            try {
                Thread.sleep(50);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Check Valid Cell
    private boolean isValid(int r, int c) {
        return r >= 0 && c >= 0 && r < ROWS && c < COLS;
    }

    // Mouse Click -> Add Walls
    @Override
    public void mousePressed(MouseEvent e) {

        int col = e.getX() / SIZE;
        int row = e.getY() / SIZE;

        if (row >= ROWS || col >= COLS) {
            return;
        }

        Node node = grid[row][col];

        if (node != start && node != end) {
            node.wall = !node.wall;
        }

        repaint();
    }

    // Key Controls
    @Override
    public void keyPressed(KeyEvent e) {

        // SPACE -> Start BFS
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {

            new Thread(() -> bfs()).start();
        }

        // C -> Clear All
        if (e.getKeyCode() == KeyEvent.VK_C) {

            for (int r = 0; r < ROWS; r++) {

                for (int c = 0; c < COLS; c++) {

                    grid[r][c].wall = false;
                    grid[r][c].visited = false;
                    grid[r][c].path = false;
                    grid[r][c].parent = null;
                }
            }

            repaint();
        }
    }

    // Unused Methods
    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    // Main Method
    public static void main(String[] args) {

        JFrame frame = new JFrame("Pathfinding Visualizer");

        Main panel = new Main();

        frame.add(panel);

        frame.pack();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
    }
}
