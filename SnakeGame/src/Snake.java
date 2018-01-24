import java.util.ArrayList;
import java.awt.Color;
import java.awt.Point;

import objectdraw.*;
import java.util.Random;

public class Snake extends ActiveObject {

	private final int MOVE_DISTANCE = 20;
	private final int RIGHT = 0;
	private final int DOWN = 1;
	private final int LEFT = 2;
	private final int UP = 3;
	private final int INCREMENT = 10;
	
	private int speedSnakeSizeThreshold = 10;
	private int levelSnakeSizeThreshold = 21;
	private int movesLeft = 10;
	private int numFoodsLeft = 20;
	private int delay = 100;
	
	private int orientation;
	
	private ArrayList<Cell> snakeArr;
	private ArrayList<Integer> moveQueue;
	private ArrayList<Point> snakeLocations;
	private ArrayList<Cell> mines;
	private Cell firstCell;
	private Cell food;
	private DrawingCanvas canvas;
	private int initialCapacity = 25 * 25;
	private boolean playing;
	private boolean foodEaten;
	private boolean tailEaten;
	private boolean minesActive;
	private Point[][] grid;
	private boolean locApproved;
	private Point chosenLoc;
	private Text levelText;
	private Text foodsLeft;
	private Text speedIncreasedText;
	
	private Random rand;
	
	public Snake(double xLoc,double yLoc, int level, DrawingCanvas canvas) {
		this.canvas = canvas;
		snakeArr = new ArrayList<Cell>(initialCapacity);
		moveQueue = new ArrayList<Integer>(100);
		snakeLocations = new ArrayList<Point>(initialCapacity);
		mines = new ArrayList<Cell>(50);
		firstCell = new Cell(xLoc, yLoc, canvas, Color.GREEN);
		snakeArr.add(firstCell);
		snakeLocations.add(new Point((int)firstCell.getX(),(int)firstCell.getY()));
		rand = new Random();
		grid = new Point[25][25];
		for(int x = 0; x < 25; x++) {
			for(int y = 0; y < 25; y++) {
				grid[x][y] = new Point(x*MOVE_DISTANCE, y*MOVE_DISTANCE);
			}
		}
		System.out.println("level = " + level);
		switch (level) {
		case 1: 
			addMines(5);
			break;
		case 2: 
			addMines(15);
			break;
		case 3: 
			addMines(40);
			break;
		case 4: 
			addMines(80);
			break;
		default:	
			addMines(5);
		}
		newFood();
		speedIncreasedText = new Text("Speed Increased", 0, 0, canvas);
		speedIncreasedText.moveTo(canvas.getWidth()/2 - speedIncreasedText.getWidth()/2, canvas.getHeight()/2 - speedIncreasedText.getHeight()/2);
		speedIncreasedText.setColor(Color.MAGENTA);
		speedIncreasedText.hide();
		levelText = new Text("Level " + level, 0, 0, canvas);
		levelText.moveTo(canvas.getWidth()/2 - levelText.getWidth()/2, canvas.getHeight()/2 - levelText.getHeight()/2 - 30);
		levelText.setColor(Color.ORANGE);
		levelText.sendToFront();
		foodsLeft = new Text("Foods Left: " + numFoodsLeft, 0, 0, canvas);
		foodsLeft.moveTo(canvas.getWidth()/2 - foodsLeft.getWidth()/2, canvas.getHeight()/2 - foodsLeft.getHeight()/2);
		foodsLeft.setColor(Color.ORANGE);
		foodsLeft.sendToFront();
		minesActive = false;
		start();
	}
	
	private void newFood() {
		while(!locApproved) {
			chosenLoc = grid[rand.nextInt(25)][rand.nextInt(25)];
			for (int i = 0; i < snakeArr.size(); i++) {
				if (chosenLoc.getX() == snakeArr.get(i).getX() &&
					chosenLoc.getY() == snakeArr.get(i).getY()){
					locApproved = false;
					break;
				}
				locApproved = true;
			}
			for (int i = 0; i < mines.size(); i++) {
				if (chosenLoc.getX() == mines.get(i).getX() &&
					chosenLoc.getY() == mines.get(i).getY()){
						locApproved = false;
						break;
					}
				locApproved = true;
			}
		}
		food = new Cell(chosenLoc.getX(), 
			    		    chosenLoc.getY(), 
			    			canvas,
			    			Color.MAGENTA);
		locApproved = false;
	}
	
	private void addMines(int n) {
		Point mineLoc = new Point();
		boolean mineLocApproved = false;
		for (int i = 0; i < n; i++) {
			mineLocApproved  = false;
			//make sure its their not created on top of each other
			if(mines.size() > 0) {
				while(!mineLocApproved) {
					mineLoc = grid[rand.nextInt(25)][rand.nextInt(25)];
					double x = mineLoc.getX();
					double y = mineLoc.getY();
					for (int j = 0; j < mines.size(); j++) {
						if ((x == mines.get(j).getX() &&
							y == mines.get(j).getY()) ||
							((x == ((mines.get(j).getX() + MOVE_DISTANCE) % (25 * MOVE_DISTANCE)) &&
							y == ((getYTest(j)) - MOVE_DISTANCE) % (25 * MOVE_DISTANCE))) ||//top right* requires test
							((x == ((mines.get(j).getX() + MOVE_DISTANCE) % (25 * MOVE_DISTANCE)) &&
							y == ((mines.get(j).getY()) + MOVE_DISTANCE) % (25 * MOVE_DISTANCE))) ||//bottom right
							((x == ((getXTest(j) - MOVE_DISTANCE) % (25 * MOVE_DISTANCE)) &&// requires test
							y == ((mines.get(j).getY()) + MOVE_DISTANCE) % (25 * MOVE_DISTANCE))) ||//bottom left
							((x == ((getXTest(j) - MOVE_DISTANCE) % (25 * MOVE_DISTANCE)) &&//* requires test
							y == ((getYTest(j)) - MOVE_DISTANCE) % (25 * MOVE_DISTANCE)))){//top left* requires test
							mineLocApproved = false;
							System.out.println("corner");
							break;
						}
						mineLocApproved = true;
					}
				}
			} else {
				mineLoc = grid[rand.nextInt(25)][rand.nextInt(25)];
			}
			Cell newMine = new Cell(mineLoc.getX(), mineLoc.getY(), canvas, Color.GRAY);
			mines.add(newMine);
		}
	}
	
	private double getXTest(int j) {
		if (mines.get(j).getX() == 0) {
			return 500;
		} else {
			return mines.get(j).getX();
		}
	}
	
	private double getYTest(int j) {
		if (mines.get(j).getY() == 0) {
			return 500;
		} else {
			return mines.get(j).getY();
		}
	}
	
	private void endGame(boolean newLevel) {
		for (int i = 0; i < snakeArr.size(); i++) {
			snakeArr.get(i).removeFromCanvas();
		}
		for (int i = 0; i < mines.size(); i++) {
			mines.get(i).removeFromCanvas();
		}
		foodsLeft.hide();
		food.removeFromCanvas();
		snakeArr.clear();
		mines.clear();
		levelText.hide();
		playing = false;
		if(newLevel) {
			Main.endGame(true);
		} else {
			Main.endGame(false);
		}
	}
	
	public int getOrientation() {
		return this.orientation;
	}
	
	public boolean getPlaying() {
		return this.playing;
	}
	
	public void setPlaying(boolean boo) {
		this.playing = boo;
	}
	
	public ArrayList<Integer> getMoveQueue() {
		return this.moveQueue;
	}
	
	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}
	
	public void run() {
		System.out.println(canvas.getWidth() + " " + canvas.getHeight());
		while(true) {
			pause(delay);
			while(playing) {
				pause(delay);
				
				//buffer time before mines are active,  and then hide text as well
				if(movesLeft > 0) {
					movesLeft--;
				} else if (movesLeft <= 0) {
					if(!minesActive) {
						for (int i = 0; i < mines.size(); i++) {
							mines.get(i).setColor(Color.RED);
						}
						levelText.hide();
						foodsLeft.setText(numFoodsLeft);
						foodsLeft.moveTo(canvas.getWidth() - foodsLeft.getWidth() - 10, 10);
						minesActive = true;
					} else {
						speedIncreasedText.hide();
					}
					
				}
				
				// define location for new snake part to be created before snake is moved
				double oldButtCellX = snakeArr.get(0).getX();
				double oldButtCellY = snakeArr.get(0).getY();
				
				//move all snake cells, but firstCell
				for (int i = 0; i < snakeArr.size() - 1; i++) {
					snakeArr.get(i).moveTo(snakeArr.get(i + 1).getX(), snakeArr.get(i + 1).getY());
					//snakeLocations.get(i).setLocation(snakeArr.get(i).getX(),snakeArr.get(i).getY());
				}
				
				// handle movement commands
				if (moveQueue.size() > 0) {
					this.orientation = moveQueue.get(0);
					moveQueue.remove(0);
				}
				
				//move firstCell
				switch(orientation){
					case RIGHT: 
						firstCell.move(MOVE_DISTANCE, 0);
						if (firstCell.getX() >= canvas.getWidth()) {
							firstCell.moveTo(0, firstCell.getY());
						}
						break;
					case DOWN:
						firstCell.move(0, MOVE_DISTANCE);
						if (firstCell.getY() >= canvas.getHeight()) {
							firstCell.moveTo(firstCell.getX(), 0);
						}
						break;
					case LEFT:
						firstCell.move(-MOVE_DISTANCE, 0);
						if (firstCell.getX() <= -MOVE_DISTANCE) {
							if (canvas.getWidth() != 500) {
								firstCell.moveTo(480, firstCell.getY());
							} else {
								firstCell.moveTo(canvas.getWidth() - MOVE_DISTANCE, firstCell.getY());
							}
						}
						break;
					case UP:
						firstCell.move(0, -MOVE_DISTANCE);
						if (firstCell.getY() <= -MOVE_DISTANCE) {
							if (canvas.getHeight() != 500) {
								firstCell.moveTo(firstCell.getX(), 480);
							} else {
								firstCell.moveTo(firstCell.getX(), canvas.getHeight() - MOVE_DISTANCE);
							}
						}
						break;
				}
				
				//check if player hit a mine
				if(minesActive) {
					for(int i = 0; i < mines.size(); i++) {
						if (firstCell.getX() == mines.get(i).getX() &&
							firstCell.getY() == mines.get(i).getY()) {
							endGame(false);
							return;
						}
					}
				}
				
				//create new snake part when food has just been eaten
				if (foodEaten) {
					snakeArr.add(0, new Cell(oldButtCellX, oldButtCellY, canvas, Color.GREEN));
					foodEaten = false;
				}
				
				//check if tail has been eaten
				for(int i = 0; i < snakeArr.size() - 1; i++) {
					if(firstCell.getX() == snakeArr.get(i).getX() &&
					   firstCell.getY() == snakeArr.get(i).getY()) {
						tailEaten = true;
					}
				}
				
				// handle an eaten tail
				if(tailEaten) {
					tailEaten = false;
					System.out.println("Snake Size = " + snakeArr.size());
					for(int i = 0; i < snakeArr.size(); i++) {
						snakeArr.get(i).removeFromCanvas();
					}
					food.removeFromCanvas();
					endGame(false);
					if(!playing) {
						return;
					}
				}
				
				// handle food consumption
				if(firstCell.getX() == food.getX() &&
				   firstCell.getY() == food.getY()) {
					food.removeFromCanvas();
					numFoodsLeft--;
					foodsLeft.setText(numFoodsLeft);
					foodsLeft.show();
					movesLeft = 10;
					newFood();
					// complete level condition
					if(snakeArr.size() + 1 >= levelSnakeSizeThreshold) {
						endGame(true);
						return;
					}
					if(delay > 30 && snakeArr.size() == speedSnakeSizeThreshold) {
						delay -= INCREMENT;
						speedSnakeSizeThreshold += INCREMENT;
						speedIncreasedText.show();
						System.out.println("Speed Increased");
					}
					foodEaten = true;
				}
			}//end of while playing
		}//end of while true
	}// end of run()
}//end of class


/*
 * when food is eaten,
 * 	 iterate each location in 25x25 grid and compare it to each snake cell location
 *   	if the snake isn't there, append that location to an ArrayList
 *   get random index of new ArrayList to decide new food location
 *   
 * __________________________________________________________________________________
 *   
 * have an ArrayList of all snake locations which updates every snake movement
 * when food is eaten
 * 		
 * 		get random location from managed Array of grid locations
 * 		
 * __________________________________________________________________________________
 * 
 * have a matrix of booleans and a matrix of Points
 * 
 *   
 * 
 *   
 */
