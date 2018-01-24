import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.*;
import objectdraw.*;

public class Main extends WindowController implements KeyListener, MouseListener {

	private static final long serialVersionUID = 1L;
	
	private static final int FRAME_WIDTH = 500;
	private static final int FRAME_HEIGHT = 500;
	private final int RIGHT = 0;
	private final int DOWN = 1;
	private final int LEFT = 2;
	private final int UP = 3;
	
	
	private static boolean playMode;
	private static Text pressToPlay;
	private static Text snakeText;
	private static Text nextLevel;
	private static Text levelComplete;
	private static Text youBeatTheGame;
	private static Text gameOver;
	private static FramedRect playBox;
	private static int level = 1;
	
	private Snake snake;
	
	
	public static void main(String[] args) {
		new Acme.MainFrame(new Main(), args, FRAME_WIDTH, FRAME_HEIGHT);
	}
	
	public void begin() {
		((Component)canvas).setBackground(Color.BLACK);
		//create Snake text title
		snakeText = new Text("SNAKE", 0, 0, canvas);
		snakeText.moveTo(canvas.getWidth()/2 - snakeText.getWidth()/2,
				         canvas.getHeight()/2 - 150);
		//create play text
		pressToPlay = new Text("Press To Play", 0, 0, canvas);
		pressToPlay.moveTo((canvas.getWidth()/2) - (pressToPlay.getWidth()/2),
				            (canvas.getHeight()/2) - (pressToPlay.getHeight()/2));
		//Next Level text
		nextLevel = new Text("Next Level", 0, 0, canvas);
		nextLevel.moveTo((canvas.getWidth()/2) - (nextLevel.getWidth()/2),
						(canvas.getHeight()/2) - (nextLevel.getHeight()/2));
		nextLevel.hide();
		//Level complete text
		levelComplete = new Text("Level Complete!", 0, 0, canvas);
		levelComplete.moveTo(canvas.getWidth()/2 - levelComplete.getWidth()/2,
		         			canvas.getHeight()/2 - 150);
		levelComplete.hide();
		//Game Over text
		gameOver = new Text("GAME OVER", 0, 0, canvas);
		gameOver.moveTo(canvas.getWidth()/2 - gameOver.getWidth()/2,
     					canvas.getHeight()/2 - 150);
		gameOver.setColor(Color.RED);
		gameOver.hide();
		// playbox creation
		playBox = new FramedRect(pressToPlay.getX() - 50,
                pressToPlay.getY() - 50,
                pressToPlay.getWidth() + 100,
                pressToPlay.getHeight() + 100,
                canvas);
		canvas.addMouseListener(this);
		//you beat the game text
		youBeatTheGame = new Text("YOU BEAT THE GAME!", 0, 0, canvas);
		youBeatTheGame.moveTo(canvas.getWidth()/2 - youBeatTheGame.getWidth()/2,
				         canvas.getHeight()/2 - 150);
		youBeatTheGame.hide();
		youBeatTheGame.setColor(Color.GREEN);
		//set Colors
		pressToPlay.setColor(Color.GREEN);
		playBox.setColor(Color.GREEN);
		snakeText.setColor(Color.GREEN);
		nextLevel.setColor(Color.GREEN);
		levelComplete.setColor(Color.GREEN);;
		//this detects keyEvents
		canvas.addKeyListener(this);
	}

	public void mouseClicked(MouseEvent evt) {
		System.out.println("boobs");
		if(!playMode && playBox.contains(new Location(evt.getPoint()))) {
			System.out.println("Box Clicked");
			pressToPlay.hide();
			playBox.hide();
			snakeText.hide();
			nextLevel.hide();
			levelComplete.hide();
			youBeatTheGame.hide();
			gameOver.hide();
			playMode = true;
			System.out.println(playBox.isHidden());
			play();
		}
	}
	
	
	@Override
	public void mousePressed(MouseEvent evt) {
		if(playBox.contains(new Location(evt.getPoint()))) {
			playBox.setColor(Color.MAGENTA);
			pressToPlay.setColor(Color.MAGENTA);
		}
	}

	@Override
	public void mouseReleased(MouseEvent evt) {
		playBox.setColor(Color.GREEN);
		pressToPlay.setColor(Color.GREEN);
	}
	@Override
	public void keyPressed(KeyEvent evt) {
		if(playMode) {
			switch(evt.getKeyCode()) {
				case KeyEvent.VK_UP: 
					if (snake.getMoveQueue().size() > 0 &&
						snake.getMoveQueue().get(snake.getMoveQueue().size() - 1) != DOWN) {
						snake.getMoveQueue().add(UP);
					} else if(snake.getOrientation() != DOWN){
						snake.getMoveQueue().add(UP);
					}
					if(playMode && !snake.getPlaying()) {
						snake.setPlaying(true);
					}
					break;
				case KeyEvent.VK_DOWN:
					if (snake.getMoveQueue().size() > 0 &&
						snake.getMoveQueue().get(snake.getMoveQueue().size() - 1) != UP) {
						snake.getMoveQueue().add(DOWN);
					} else if (snake.getOrientation() != UP){
						snake.getMoveQueue().add(DOWN);
					}
					if(playMode && !snake.getPlaying()) {
						snake.setPlaying(true);
					}
					break;
				case KeyEvent.VK_RIGHT: 
					if (snake.getMoveQueue().size() > 0 &&
						snake.getMoveQueue().get(snake.getMoveQueue().size() - 1) != LEFT) {
						snake.getMoveQueue().add(RIGHT);
					} else if(snake.getOrientation() != LEFT){
						snake.getMoveQueue().add(RIGHT);
					}
					if(playMode && !snake.getPlaying()) {
						snake.setPlaying(true);
					}
					break;
				case KeyEvent.VK_LEFT: 
					if(!snake.getPlaying()) {
						snake.getMoveQueue().add(LEFT);
					} else if (snake.getMoveQueue().size() > 0 &&
						snake.getMoveQueue().get(snake.getMoveQueue().size() - 1) != RIGHT) {
						snake.getMoveQueue().add(LEFT);
					} else if(snake.getOrientation() != RIGHT){
						snake.getMoveQueue().add(LEFT);
					}
					if(playMode && !snake.getPlaying()) {
						snake.setPlaying(true);
					}
					break;
			}
		}
	}
	
	
	public void play() {
		snake = new Snake(240,240, level, canvas);
	}
	
	public static void endGame(boolean newLevel) {
		playMode = false;
		playBox.show();
		if(newLevel) {
			System.out.println("Level Complete");
			level++;
			if (level >= 5 ) {
				youBeatTheGame.show();
				Main.endGame(false);
			} else {
				System.out.println("MainLevel = " + level);
				levelComplete.show();
				nextLevel.show();
			}
		} else {
			level = 1;
			gameOver.show();
			System.out.println("End Game");
			pressToPlay.show();
		}
	}
	
	//unused implementations
	@Override
	public void keyReleased(KeyEvent arg0) {}
	@Override
	public void keyTyped(KeyEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
}//end of class
