import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SnakeFrame extends JFrame {
    SnakeGamePanel game;
    SnakeMenu menu;
    CardLayout card;

    SnakeFrame() {
        card = new CardLayout();
        setLayout(card);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Adding menu screen
        menu = new SnakeMenu();
        add("menu", menu);
        
        game = new SnakeGamePanel();
        add("game", game);

        // The added values because of the frame borders
        setSize(Prefs.SCREEN_SIZE + 14, Prefs.SCREEN_SIZE + 37);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void gameOver() {
        // Set score on menu screen
        menu.setScore(game.applesEaten);
        card.show(this.getContentPane(), "menu");
    }

    public void startGame() {
        // Add the game JPanel and show it to start the game
    	game.reset();
        card.show(this.getContentPane(), "game");
        game.grabFocus();
    }
}

class SnakeMenu extends JPanel implements ActionListener {
    public int score;
    JLabel scoreLab;
    SnakeMenu() {
    	setLayout(null);
        setBackground(Color.BLACK);

        JLabel title = new JLabel("SNAKE GAME");
        title.setBounds(0, Prefs.SCREEN_SIZE/8, Prefs.SCREEN_SIZE, 100);
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("Calibri", Font.BOLD, 100));
        title.setForeground(Color.WHITE);
        add(title);
        
        scoreLab = new JLabel();
        scoreLab.setBounds( 0, Prefs.SCREEN_SIZE/2 - 60, Prefs.SCREEN_SIZE, 60 );
        scoreLab.setHorizontalAlignment(JLabel.CENTER);
        scoreLab.setFont(new Font("Calibri", Font.BOLD, 40));
        scoreLab.setForeground(Color.WHITE);
        add(scoreLab);
        setScore(20);
        
        JButton start = new JButton("Start");
        start.setBounds( (Prefs.SCREEN_SIZE/2) - 100, Prefs.SCREEN_SIZE/2 + 40, 200, 60 );
        start.setFont(new Font("Calibri", Font.BOLD, 35));
        add(start);
        start.addActionListener(this);
        start.setFocusPainted(false);
        
    }

    public void setScore(int score) {
        this.score = score;
        scoreLab.setText("Score: " + Integer.toString(score));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ((SnakeFrame) SwingUtilities.getRoot(this)).startGame();
    }
}
