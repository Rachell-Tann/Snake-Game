/*
*Author: Tan,Rachel
*Personal Project: Snake Game
*Brief purpose: The objective is to eat as many apples as possible. 
*Each time the snake eats an apple its body grows. 
*There will be a time limit of 5 minutes per game.
*The snake must avoid the walls and its own body in order to continue the game.
*/

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {

	// Variables
	private final int B_WIDTH = 300;
	private final int B_HEIGHT = 300;
	private final int DOT_SIZE = 10;
	private final int ALL_DOTS = 900;
	private final int RAND_POS = 29;
	private final int SPEED = 100;
	private final int SECOND = 1000; // 1000ms = 1s
	private final long TIME_LIMIT = 300000; // 300000ms = 5min

	private final int x[] = new int[ALL_DOTS];
	private final int y[] = new int[ALL_DOTS];

	private int dots;
	private int appleEaten;
	private int apple_x;
	private int apple_y;

	private Image ball;
	private Image apple;
	private Image head;

	private boolean inGame = true;

	private char direction = 'R';

	private Timer timer;
	private Timer countDownTimer;

	private SimpleDateFormat dispTime;

	private long startTime;
	private long currentTime;
	private long elapseTime;

	public Board() {

		initBoard();
	}

	// Game boatd setup
	private void initBoard() {

		addKeyListener(new TAdapter());
		setBackground(Color.black);
		setFocusable(true);

		setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
		loadImages();
		initGame();
	}

	// Import images
	private void loadImages() {

		ImageIcon iid = new ImageIcon("dot.png");
		ball = iid.getImage();

		ImageIcon iia = new ImageIcon("apple.png");
		apple = iia.getImage();

		ImageIcon iih = new ImageIcon("head.png");
		head = iih.getImage();
	}

	// Dot setup
	private void initGame() {

		dots = 3;
		startTime = -1;

		for (int z = 0; z < dots; z++) {
			x[z] = 50 - z * 10;
			y[z] = 50;
		}

		locateApple();

		// Countdown timer setup
		dispTime = new SimpleDateFormat("mm:ss");
		timer = new Timer(SPEED, this);
		countDownTimer = new Timer(SECOND, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (startTime < 0) {
					startTime = System.currentTimeMillis();
				}
				currentTime = System.currentTimeMillis();
				elapseTime = currentTime - startTime;
				if (elapseTime >= TIME_LIMIT) {
					inGame = false;
					countDownTimer.stop();
				}
			}
		});
		countDownTimer.start();
		timer.start();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		doDrawing(g);
	}

	// Imported images are painted/drew onto a square for each section
	private void doDrawing(Graphics g) {

		if (inGame) {

			g.drawImage(apple, apple_x, apple_y, this);

			for (int z = 0; z < dots; z++) {
				if (z == 0) {
					g.drawImage(head, x[z], y[z], this);
				} else {
					g.drawImage(ball, x[z], y[z], this);
				}
			}
			g.setColor(Color.red);
			g.setFont(new Font("Helvetica", Font.BOLD, 20));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: " + appleEaten, (B_WIDTH - metrics.stringWidth("Score: " + appleEaten)) / 2,
					g.getFont().getSize()); // Define Score text
			g.drawString(dispTime.format(TIME_LIMIT - elapseTime), 240, g.getFont().getSize()); // Define timer format
		} else {

			gameOver(g);
		}
	}

	// Define Game Over text
	private void gameOver(Graphics g) {

		g.setColor(Color.red);
		g.setFont(new Font("Helvetica", Font.BOLD, 20));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Game Over", (B_WIDTH - metrics.stringWidth("Game Over")) / 2, B_HEIGHT / 2);
	}

	private void checkApple() {

		if ((x[0] == apple_x) && (y[0] == apple_y)) {

			dots++;
			appleEaten++;
			locateApple();
		}
	}

	// Se direction
	private void move() {

		for (int z = dots; z > 0; z--) {
			x[z] = x[(z - 1)];
			y[z] = y[(z - 1)];
		}

		switch (direction) {

		case 'U':
			y[0] = y[0] - DOT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + DOT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - DOT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + DOT_SIZE;
			break;
		}

	}

	// Set coordination
	private void checkCollision() {

		for (int z = dots; z > 0; z--) {

			if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
				inGame = false;
			}
		}

		if (y[0] >= B_HEIGHT) {
			inGame = false;
		}

		if (y[0] < 0) {
			inGame = false;
		}

		if (x[0] >= B_WIDTH) {
			inGame = false;
		}

		if (x[0] < 0) {
			inGame = false;
		}

		if (!inGame) {
			timer.stop();
		}
	}

	// Locate apples
	private void locateApple() {

		int r = (int) (Math.random() * RAND_POS);
		apple_x = ((r * DOT_SIZE));

		r = (int) (Math.random() * RAND_POS);
		apple_y = ((r * DOT_SIZE));
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (inGame) {

			checkApple();
			checkCollision();
			move();
		}

		repaint();
	}

	// Set direction with its own key
	private class TAdapter extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {

			switch (e.getKeyCode()) {

			case KeyEvent.VK_LEFT:
				if (direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if (direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if (direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if (direction != 'U') {
					direction = 'D';
				}
				break;
			}
		}
	}
}
