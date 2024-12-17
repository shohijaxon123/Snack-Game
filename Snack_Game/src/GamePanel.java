import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener {

	private static final int SCREEN_WIDTH = 600;
	private static final int SCREEN_HEIGHT = 600;
	private static final int UNIT_SIZE = 25;
	private static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / (UNIT_SIZE);
	//Control speed
	private static final int DELAY = 75;
	
	private final int[] x = new int[GAME_UNITS];
	private final int[] y = new int[GAME_UNITS];
	
	private int bodySize = 6;
	private int applesEaten;
	private int appleX;
	private int appleY;
	private char direction = 'R';
	private boolean running = false;
	
	Timer timer;
	Random random;
	
	GamePanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	
	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		if(running) {
			/*
			//Creates square
			for(int i = 0; i < SCREEN_HEIGTH / UNIT_SIZE; i++) {
				g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGTH);
				g.drawLine(0, i * UNIT_SIZE, SCREEN_WEIDTH, i*UNIT_SIZE);
			}
			*/
			//Giving color to apple
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
			
			//Giving color to snake
			for(int i = 0; i < bodySize; i++) {
				if(i == 0) {
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
				else {
					g.setColor(new Color(45, 180, 0));
					//Random colors of snake
					//g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
					
				}
				
				//Score in game
				g.setColor(Color.red);
				g.setFont( new Font("Ink Free",Font.BOLD, 40));
				FontMetrics metrics = getFontMetrics(g.getFont());
				g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
			}
		}
		else {
			gameOver(g);
		}
	}
	//Creating random coordinates for apple
	public void newApple() {
		appleX = random.nextInt((int)(SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
	}
	
	public void move() {
		for(int i = bodySize; i > 0; i--) {
			x[i] = x[i - 1];
			y[i] = y[i - 1];
		}
		
		switch(direction) {
			case 'D':
				y[0] = y[0] + UNIT_SIZE;
				break;
			case 'U':
				y[0] = y[0] - UNIT_SIZE;
				break;
			case 'L':
				x[0] = x[0] - UNIT_SIZE;
				break;
			case 'R':
				x[0] = x[0] + UNIT_SIZE;
				break;
		
		}
	}
	
	//Checking apple and creating new one
	public void checkApple() {
		if((x[0] == appleX) && (y[0] == appleY)) {
			bodySize++;
			applesEaten++;
			newApple();
		}
	}
	
	public void checkCollision() {
		
		for(int i = bodySize; i > 0; i--) {
			//Check if head touches body
			if((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
			}
			
			//Check if head touches left border
			if(x[0] < 0) {
				running = false;
			}
			//Check if head touches right border
			if(x[0] > SCREEN_WIDTH) {
				running = false;
			}
			//Check if head touches top
			if(y[0] < 0) {
				running = false;
			}
			//Check if head touches bottom
			if(y[0] > SCREEN_HEIGHT) {
				running = false;
			}
			
			if(!running) {
				timer.stop();
			}
		}
	}
	
	public void gameOver(Graphics g) {
		//Score
		g.setColor(Color.red);
		g.setFont( new Font("Ink Free",Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
		//Game Over text
		g.setColor(Color.red);
		g.setFont( new Font("Ink Free",Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(running) {
			move();
			checkApple();
			checkCollision();
		}
		repaint();
	}
	
	//Making turning only for 90°
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					if(direction != 'R') {
						direction = 'L';
					}
					break;
				case KeyEvent.VK_RIGHT:
					if(direction != 'L') {
						direction = 'R';
					}
					break;
				case KeyEvent.VK_UP:
					if(direction != 'D') {
						direction = 'U';
					}
					break;
				case KeyEvent.VK_DOWN:
					if(direction != 'U') {
						direction = 'D';
					}
					break;
			}
		}
	}

}
