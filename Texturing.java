import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Texturing
{

	// create textures
	public static Texturing wood = new Texturing("assets/images/wood.png", 64);
	public static Texturing stone = new Texturing("assets/images/stone.png", 64);

	public int[] pixels; //pixel grid of the the image

	private String imagePath;
	public final int TEXTURE_SIZE;

	public Texturing(String path, int imageSize)
	{
		imagePath = path;
		TEXTURE_SIZE = imageSize;

		pixels = new int[TEXTURE_SIZE * TEXTURE_SIZE]; // accept ration like 64x64
		loadTexture();
	}

	private void loadTexture()
	{
		try {
			// read texture file
			BufferedImage imageBuffer = ImageIO.read(new File(imagePath));

			// get dimentions
			int width = imageBuffer.getWidth();
			int height = imageBuffer.getHeight();

			imageBuffer.getRGB(0, 0, width, height, pixels, 0, width);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
