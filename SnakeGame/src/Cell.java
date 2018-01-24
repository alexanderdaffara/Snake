import java.awt.Color;
import objectdraw.*;

public class Cell {

	private final double BACK_WIDTH = 20;
	private final double BACK_HEIGHT = BACK_WIDTH;
	private final double FRONT_WIDTH = 18;
	private final double FRONT_HEIGHT = FRONT_WIDTH;
	private final double SHIFT = 1;
	
	private FilledRect backRect;
	private FilledRect frontRect;
	
	public Cell(double xLoc, double yLoc, DrawingCanvas canvas, Color c) {
		backRect = new FilledRect(xLoc, yLoc, BACK_WIDTH, BACK_HEIGHT, canvas);
		frontRect = new FilledRect(xLoc + SHIFT, yLoc + SHIFT, FRONT_WIDTH, FRONT_HEIGHT, canvas);
		backRect.setColor(Color.BLACK);
		frontRect.setColor(c);
	}
	
	public void removeFromCanvas() {
		backRect.removeFromCanvas();
		frontRect.removeFromCanvas();
	}
	
	public void setColor(Color c) {
		frontRect.setColor(c);
	}
	
	public void moveTo(double x, double y) {
		backRect.moveTo(x,y);
		frontRect.moveTo(x + SHIFT,y + SHIFT);
	}
	
	public void move(double dx, double dy) {
		backRect.move(dx,dy);
		frontRect.move(dx,dy);
	}
	
	public double getX() {
		return backRect.getX();
	}
	
	public double getY() {
		return backRect.getY();
	}
}
