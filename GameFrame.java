import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import javax.swing.JFrame;

public class GameFrame extends JFrame implements Runnable
{
	private static final long serialVersionUID = 1L;

	// game map params
	public int mapWidth = 15;
	public int mapHeight = 15;

	private Thread gameEngineThread;
	private boolean running; // does engine runs
	private BufferedImage imageBuffer;

	public int[] pixels;

	/**
	 * Game map overview from top
	 *
	 * 0 - empty space
	 * >= 1 - texture on the wall
	 *
	 */
	public static int[][] gameMapOverview = {
		{1,1,1,1,1,1,1,1,2,2,2,2,2,2,2},
		{1,0,0,0,0,0,0,0,2,0,0,0,0,0,2},
		{1,0,3,3,3,3,3,0,0,0,0,0,0,0,2},
		{1,0,3,0,0,0,3,0,2,0,0,0,0,0,2},
		{1,0,3,0,0,0,3,0,2,2,2,0,2,2,2},
		{1,0,3,0,0,0,3,0,2,0,0,0,0,0,2},
		{1,0,3,3,0,3,3,0,2,0,0,0,0,0,2},
		{1,0,0,0,0,0,0,0,2,0,0,0,0,0,2},
		{1,1,1,1,1,1,1,1,4,4,4,0,4,4,4},
		{1,0,0,0,0,0,1,4,0,0,0,0,0,0,4},
		{1,0,0,0,0,0,1,4,0,0,0,0,0,0,4},
		{1,0,0,2,0,0,1,4,0,3,3,3,3,0,4},
		{1,0,0,0,0,0,1,4,0,3,3,3,3,0,4},
		{1,0,0,0,0,0,0,0,0,0,0,0,0,0,4},
		{1,1,1,1,1,1,1,4,4,4,4,4,4,4,4}
	};

	public GameFrame()
	{
		// Open a new empty gameEngineThread
		gameEngineThread = new Thread(this);
		imageBuffer = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB); // set the image buffer for texturing
		pixels = ((DataBufferInt)imageBuffer.getRaster().getDataBuffer()).getData(); // get image buffer data

		// start the engine
		setJframeConfigs();
		startEngine();
	}

	private void setJframeConfigs()
	{
		setSize(640, 480);
		setResizable(false);
		setTitle("3D Engine");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(Color.black);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void startEngine()
	{
		// set engine status as runnging
		running = true;
		gameEngineThread.start();
	}

	/**
	 * Stop thread and terminat engine
	 *
	 * must be - synchronized
	 */
	public synchronized void stop()
	{
		// set engine status as runnging
		running = true;

		try {
			// wait for thread complited
			gameEngineThread.join();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void render()
	{
		// set window buffer for smooth running when creating sequension
		BufferStrategy windowBuffer = getBufferStrategy();

		if (windowBuffer == null) {
			createBufferStrategy(3);
			return;
		}

		Graphics graphics = windowBuffer.getDrawGraphics();

		// get pre rendered image
		graphics.drawImage(
			imageBuffer,
			0,
			0,
			imageBuffer.getWidth(),
			imageBuffer.getHeight(),
			null
		);

		// render graphics
		windowBuffer.show();
	}

	public void run()
	{
		long lastTime = System.nanoTime();
		final double nanoseconds = 1000000000.0 / 60.0;//60 times per second

		double delta = 0;
		requestFocus();

		while(running) {
			long now = System.nanoTime();
			delta = delta + ((now - lastTime) / nanoseconds);
			lastTime = now;

			/**
			 * Make sure update is only happening 60 times a second
			 *
			 * Allows to atchive smooth 60fps
			 *
			 */
			while (delta >= 1) {
				//handles all of the logic restricted time
				delta--;
			}

			// displays to the screen unrestricted time
			render();
		}
	}
}
