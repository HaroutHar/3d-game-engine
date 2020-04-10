import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PlayersCamera implements KeyListener
{
	// camera axis
	public double xPos, yPos, xDir, yDir, xPlane, yPlane;

	/**
	 * Directions of movement
	 *
	 * xPos and yPos are the location of the player on the 2D map that was created in the Game class.
	 * xDir and yDir are the x and y components of a vector that points in the direction the player is facing.
	 */
	public boolean right, left, forward, back;
	// movment speed
	public final double MOVE_SPEED = .08;
	public final double ROTATION_SPEED = .045;

	public PlayersCamera(double x, double y, double xd, double yd, double xp, double yp)
	{
		xPos = x;
		yPos = y;
		xDir = xd;
		yDir = yd;
		xPlane = xp;
		yPlane = yp;
	}

	// init players' movement
	// Parrent Class fucntions
	public void keyPressed(KeyEvent key)
	{
		if ((key.getKeyCode() == KeyEvent.VK_LEFT))
			left = true;
		if ((key.getKeyCode() == KeyEvent.VK_RIGHT))
			right = true;
		if ((key.getKeyCode() == KeyEvent.VK_UP))
			forward = true;
		if ((key.getKeyCode() == KeyEvent.VK_DOWN))
			back = true;
	}

	// init players' movement
	// Parrent Class fucntions
	public void keyReleased(KeyEvent key)
	{
		if ((key.getKeyCode() == KeyEvent.VK_LEFT))
			left = false;
		if ((key.getKeyCode() == KeyEvent.VK_RIGHT))
			right = false;
		if ((key.getKeyCode() == KeyEvent.VK_UP))
			forward = false;
		if ((key.getKeyCode() == KeyEvent.VK_DOWN))
			back = false;
	}

	/**
	 * Player movement
	 *
	 * Actions on every key-actions
	 * update possitions
	 * check if camera is inside the wall
	 *
	 */
	public void update(int[][] map)
	{
		// move camera forward
		if (forward) {
			if (map[(int)(xPos + xDir * MOVE_SPEED)][(int)yPos] == 0) {
				xPos += xDir*MOVE_SPEED; // update coord
			}
			if (map[(int)xPos][(int)(yPos + yDir * MOVE_SPEED)] == 0)
				yPos += yDir*MOVE_SPEED; // update coord
		}
		// move camera back
		if (back) {
			if (map[(int)(xPos - xDir * MOVE_SPEED)][(int)yPos] == 0)
				xPos -= xDir*MOVE_SPEED; // update coord
			if (map[(int)xPos][(int)(yPos - yDir * MOVE_SPEED)] == 0)
				yPos -= yDir*MOVE_SPEED; // update coord
		}
		// move camera right
		if (right) {
			double oldxDir = xDir;
			xDir = xDir*Math.cos(-ROTATION_SPEED) - yDir*Math.sin(-ROTATION_SPEED);
			yDir = oldxDir*Math.sin(-ROTATION_SPEED) + yDir*Math.cos(-ROTATION_SPEED);

			double oldxPlane = xPlane;
			xPlane = xPlane*Math.cos(-ROTATION_SPEED) - yPlane*Math.sin(-ROTATION_SPEED);
			yPlane = oldxPlane*Math.sin(-ROTATION_SPEED) + yPlane*Math.cos(-ROTATION_SPEED);
		}
		// move camera left
		if (left) {
			double oldxDir = xDir;
			xDir = xDir*Math.cos(ROTATION_SPEED) - yDir*Math.sin(ROTATION_SPEED);
			yDir = oldxDir*Math.sin(ROTATION_SPEED) + yDir*Math.cos(ROTATION_SPEED);

			double oldxPlane = xPlane;
			xPlane = xPlane*Math.cos(ROTATION_SPEED) - yPlane*Math.sin(ROTATION_SPEED);
			yPlane = oldxPlane*Math.sin(ROTATION_SPEED) + yPlane*Math.cos(ROTATION_SPEED);
		}
	}

	public void keyTyped(KeyEvent e)
	{

	}
}
