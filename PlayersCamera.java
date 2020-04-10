import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PlayersCamera implements KeyListener
{
	// camera axis
	public double xPos, yPos, xDir, yDir, xPlane, yPlane;

	// moving directions
	public boolean right, left, forward, back;
	// movment speed
	public final double MOVE_SPEED = .08;
	public final double ROTATION_SPEED = .045;

}
