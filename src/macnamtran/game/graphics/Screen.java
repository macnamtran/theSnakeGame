package macnamtran.game.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import macnamtran.game.entities.Apple;
import macnamtran.game.entities.BodyPart;

public class Screen extends JPanel implements Runnable {
    public static final int WIDTH = 800, HEIGHT = 800;
    private Thread thread;
    private boolean running = false;
    private boolean gameOver = false;
    

    private BodyPart b;
    private ArrayList<BodyPart> snake;

    private Apple apple;
    private ArrayList<Apple> apples;

    private List<String> funFact = new ArrayList<>();

    private Random r;

    private int xCoor = 10, yCoor = 10;
    private int size = 5;

    private boolean right = true, left = false, up = false, down = false;
    private int ticks = 0;

    private Key key;
    private JFrame funFactFrame;
    private FunFactPanel funFactPanel;

    public Screen() {
        setFocusable(true);
        key = new Key();
        addKeyListener(key);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        r = new Random();

        snake = new ArrayList<BodyPart>();
        apples = new ArrayList<Apple>();

        funFact.add("The snake game gained popularity in the 1990s with Nokia phones.");
        funFact.add("The game's concept originated from an arcade game called \"Blockade\" in 1976.");
        funFact.add("Taneli Armanto created the snake game for Nokia phones in 1997.");
        funFact.add("The game was named \"Snake\" due to its snake-like movement.");
        funFact.add("The highest achievable score is 9,999 points.");
        funFact.add("The game has been adapted for various platforms beyond mobile phones.");
        funFact.add("The snake game inspired popular multiplayer mobile games like \"Slither.io\"");
        funFact.add("The snake game has appeared in movies and TV shows, including \"Pixels\" and \"Stranger Things.\"");
        funFact.add("The snake game is often hidden as an Easter egg in software and websites."); 

        funFactFrame = new JFrame("Fun Fact");
        funFactFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        funFactFrame.setSize(800, 600);
        funFactPanel = new FunFactPanel();
        funFactFrame.add(funFactPanel);

        
        start();
    }

    //game logic
    public void tick() {
    	// check if the snake has been created
        if (snake.size() == 0) {
            b = new BodyPart(xCoor, yCoor, 10);
            snake.add(b);
        }
        //check if an apple need to be generated
        if (apples.size() == 0) {
            int xCoor = r.nextInt(79);
            int yCoor = r.nextInt(79);

            apple = new Apple(xCoor, yCoor, 10);
            apples.add(apple);
        }
        //check for collision between snake and apple
        for (int i = 0; i < apples.size(); i++) {
            if (xCoor == apples.get(i).getxCoor() && yCoor == apples.get(i).getyCoor()) {
                size++;
                apples.remove(i);
                i--;
            }
        }
        // check for collision between snake and itself
        for (int i = 0; i < snake.size(); i++) {
            if (xCoor == snake.get(i).getxCoor() && yCoor == snake.get(i).getyCoor()) {
                if (i != snake.size() - 1) {
                    stop();
                    showFunFact();
                }
            }
        }
        //check for collision between boundaries
        if (xCoor < 0 || xCoor > 79 || yCoor < 0 || yCoor > 79) {
        	stop();
            showFunFact();
        }

        try {
			Thread.sleep(90);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // wait for 100 milliseconds
        	//move the snake in appropriate direction
            if (right) xCoor++;
            if (left) xCoor--;
            if (up) yCoor--;
            if (down) yCoor++;

            ticks = 0;

            b = new BodyPart(xCoor, yCoor, 10);
            snake.add(b);
            //remove the tail of the snake if it exceeds the desired size
            if (snake.size() > size) {
                snake.remove(0);
            }
        
        if (gameOver) {
            stop();
            showFunFact();
        }
    }

    public void paint(Graphics g) {
        g.clearRect(0, 0, WIDTH, HEIGHT);
        //draw grid line
        g.setColor(Color.BLACK);
        for (int i = 0; i < WIDTH / 10; i++) {
            g.drawLine(i * 10, 0, i * 10, HEIGHT);
        }
        for (int i = 0; i < HEIGHT / 10; i++) {
            g.drawLine(0, i * 10, WIDTH, i * 10);
        }
        //draw the snake
        for (int i = 0; i < snake.size(); i++) {
            snake.get(i).draw(g);
        }
        //draw the apples
        for (int i = 0; i < apples.size(); i++) {
            apples.get(i).draw(g);
        }
        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            String gameOverText = "Game Over";
            g.drawString(gameOverText, WIDTH / 2 - g.getFontMetrics().stringWidth(gameOverText) / 2,
                    HEIGHT / 2);

            g.setColor(Color.BLUE);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            String funFactText = "Fun Fact: " + getRandomFunFact();
            g.drawString(funFactText, WIDTH / 2 - g.getFontMetrics().stringWidth(funFactText) / 2,
                    HEIGHT / 2 + 30);
        }
    }

    public void start() {
        running = true;
        thread = new Thread(this, "Game Loop");
        thread.start();
    }

    public void stop() {
        running = false;
        /*try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } */
    }

    public void run() {
        while (running) {
            tick();
            repaint();
        }
    }

    private String getRandomFunFact() {
        Random random = new Random();
        int index = random.nextInt(funFact.size());
        return funFact.get(index);
    }

    private void showFunFact() {
        String funFact = getRandomFunFact();
        funFactPanel.setFunFact(funFact);
        funFactFrame.setVisible(true);
    }

    private class Key implements KeyListener {
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_RIGHT && !left) {
                up = false;
                down = false;
                right = true;
            }
            if (key == KeyEvent.VK_LEFT && !right) {
                up = false;
                down = false;
                left = true;
            }
            if (key == KeyEvent.VK_UP && !down) {
                left = false;
                right = false;
                up = true;
            }
            if (key == KeyEvent.VK_DOWN && !up) {
                left = false;
                right = false;
                down = true;
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }
    //Displaying the fun fact panel
    private class FunFactPanel extends JPanel {
        private String funFact;

        public void setFunFact(String funFact) {
            this.funFact = funFact;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.clearRect(0, 0, getWidth(), getHeight());

            g.setColor(Color.BLUE);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            String funFactText = "Fun Fact: " + funFact;
            g.drawString(funFactText, getWidth() / 2 - g.getFontMetrics().stringWidth(funFactText) / 2,
                    getHeight() / 2);
        }
    }
}

