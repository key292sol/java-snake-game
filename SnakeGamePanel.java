import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;


// TODO: Maybe make the iterations in paint() instead of using Timer
public class SnakeGamePanel extends JPanel {
    private Random rand = new Random();
    private int appleX, appleY;
    private Vector<Integer> x, y;
    private int lastX, lastY;
    private char direction;
    private boolean gameStarted;
    private Timer timer;
    private int winningScore;
    
    public int applesEaten;
    Color headColor = new Color(0, 200, 0);

    SnakeGamePanel() {
    	SnakeEvents ev = new SnakeEvents();
        timer = new Timer(Prefs.gameDelay, ev);
        addKeyListener(ev);
    }
    
    public void reset() {
    	gameStarted = false;
    	direction = 'R';
    	applesEaten = 0;
    	lastX = lastY = -1;
    	
        x = new Vector<Integer>();
        y = new Vector<Integer>();

        // Adding 3 parts to snake
        for (int i = 0; i < 3; i++) {
            x.add(Prefs.gridSize/2 - i - 1);
            y.add(Prefs.gridSize/2 - 1);
        }
    	winningScore = (Prefs.gridSize * Prefs.gridSize) - x.size();
        
        spawnApple();
        
        // Get Focus because KeyListener works only on a focused element
        setFocusable(true);
    }

    private void spawnApple() {
        do {
            appleX = rand.nextInt(Prefs.gridSize);
            appleY = rand.nextInt(Prefs.gridSize);
        } while (!isValidSpawn());
    }

    private boolean isValidSpawn() {
        // Check that is apple hasn't spawned inside of the snake
        // Apple shouldn't spawn where a body part of the snake exists
    	if ((appleX == Prefs.gridSize - 1) && (appleY == 0)) {
    		return false;
    	}
        for (int i = 0; i < x.size(); i++) {
            if (appleX == x.get(i).intValue() && appleY == y.get(i).intValue()) {
                return false;
            }
        }
        return true;
    }

    private void move() {
    	lastX = x.lastElement().intValue();
    	lastY = y.lastElement().intValue();
        // Move the elements in the vector 
        for (int i = x.size() - 1; i > 0; i--) {
            x.set(i, x.get(i-1));
            y.set(i, y.get(i-1));
        }

        // Move the snake head
        switch (direction) {
            case 'R': x.set(0, x.get(0) + 1); break;
            case 'L': x.set(0, x.get(0) - 1); break;
            case 'U': y.set(0, y.get(0) - 1); break;
            case 'D': y.set(0, y.get(0) + 1); break;
        }
        repaint();
    }

    private void eatApple() {
        // Check if snake is at apple
        if(x.get(0).intValue() == appleX && y.get(0).intValue() == appleY) {
            x.add(x.lastElement());
            y.add(y.lastElement());
            applesEaten++;
            if (applesEaten == winningScore) {
            	gameWon();
            	return;
            }
            spawnApple();
        }
    }

    private void checkCollision() {
        int headX = x.get(0).intValue();
        int headY = y.get(0).intValue();

        // Collision with itself
        for (int i = 1; i < x.size(); i++) {
            if (headX == x.get(i).intValue() &&  headY == y.get(i).intValue()) {
                gameOver();
            }
        }

        // Goes outside frame or over the score box
        if ( (headX < 0 || headY < 0) || (headX >= Prefs.gridSize || headY >= Prefs.gridSize) || (headX == Prefs.gridSize - 1 && headY == 0) )
            gameOver();
    }
    
    private void gameWon() {
    	timer.stop();
    	JOptionPane.showMessageDialog(null, "Congratulations!\nYou won the game !!!");
    	((SnakeFrame) SwingUtilities.getRoot(this)).gameOver();
    }

    private void gameOver() {
        // Stop the timer so that snake doesn't move
        timer.stop();

        // Wait for 3 seconds before exiting this panel
        long startTime = System.currentTimeMillis();
        long curTime = System.currentTimeMillis();
        while (curTime - startTime <= 3000){
            curTime = System.currentTimeMillis();
        }
        ((SnakeFrame) SwingUtilities.getRoot(this)).gameOver();
    }

    private void drawSnakePart(Graphics g, int index) {
        g.fillRect(
            x.get(index) * Prefs.getBlockSize(), 
            y.get(index) * Prefs.getBlockSize(), 
            Prefs.getBlockSize(), 
            Prefs.getBlockSize()
        );
    }
    
    private void drawApple(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(
            appleX * Prefs.getBlockSize(),
            appleY * Prefs.getBlockSize(),
            Prefs.getBlockSize(),
            Prefs.getBlockSize()
        );
    }
    
    private void showScore(Graphics g) {
        // Score background
        g.setColor(Color.WHITE);
        g.fillRect(
            (Prefs.gridSize - 1) * Prefs.getBlockSize(), 0,
            Prefs.getBlockSize(),  Prefs.getBlockSize()
        );

        // Score text
        g.setColor(Color.BLACK);
        g.setFont(new Font("Calibri", Font.PLAIN, 18));
        g.drawString(
            Integer.toString(applesEaten),
            (Prefs.gridSize - 1) * Prefs.getBlockSize() + 5,
            Prefs.getBlockSize() - 7
        );
    }

    public void paint(Graphics g) {
    	if (!gameStarted) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, getWidth(), getHeight());
    	}
		
		// Draw apple

        // Draw Snake Head
        // g.setColor(Color.YELLOW);
        g.setColor(headColor);
        drawSnakePart(g, 0);
    	
        // Draw Snake Body
        g.setColor(Color.GREEN);
        if (!gameStarted) {
	        for (int i = 1; i < x.size(); i++) {
	            drawSnakePart(g, i);
	        }
            drawApple(g);
            showScore(g);
        } else {
        	drawSnakePart(g, 1);
            if (lastX == -1) {
                drawApple(g);
                showScore(g);
            } else {
                g.setColor(Color.BLACK);
                g.fillRect(
                    lastX * Prefs.getBlockSize(), 
                    lastY * Prefs.getBlockSize(), 
                    Prefs.getBlockSize(), 
                    Prefs.getBlockSize()
                );
            }
        }

        drawGrid(g);
    }

    class SnakeEvents extends KeyAdapter implements ActionListener {
        @Override
        public void keyPressed(KeyEvent e) {
            // Start the game if it hasn't started
            if (!gameStarted && e.getKeyCode() != KeyEvent.VK_A) {
                startGame();
            }

            // Change direction of the snake
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                    if (direction != 'D') direction = 'U';
                    break;
                case KeyEvent.VK_A:
                    if (direction != 'R') direction = 'L';
                    break;
                case KeyEvent.VK_S:
                    if (direction != 'U') direction = 'D';
                    break;
                case KeyEvent.VK_D:
                    if (direction != 'L') direction = 'R';
                    break;
            }
        }

        // Called using timer
        @Override
        public void actionPerformed(ActionEvent e) {
            move();
            eatApple();
            checkCollision();
            if (lastX == x.lastElement().intValue() && lastY == y.lastElement().intValue())
            	lastX = lastY = -1;
        }

        private void startGame() {
            // Set gameStarted and start the timer to do the required functions
            gameStarted = true;
            timer.start();
        }
    }
    
    // Draw lines for showing grid
    private void drawGrid(Graphics g) {
        g.setColor(new Color(30, 30, 30));
        int size = Prefs.SCREEN_SIZE, pos;
        for (int i = 0; i < Prefs.gridSize; i++) {
            pos = i * Prefs.getBlockSize();
            g.drawLine(pos, 0, pos, size);
            g.drawLine(0, pos, size, pos);
        }
    }
}
