import java.util.ArrayList;
import java.awt.Color;

public class EngineScreen
{
	// game map and dimentions
	public int[][] gameMap;
	public int mapWidth, mapHeight, screenWidth, screenHeight;
	public ArrayList<Texturing> textures;

	public EngineScreen(int[][] gameMapArray, int mapW, int mapH, ArrayList<Texturing> texturesArray, int width, int height)
	{
		gameMap = gameMapArray;
		textures = texturesArray;
		screenWidth = width;
		screenHeight = height;
		mapWidth = mapW;
		mapHeight = mapH;
	}

	public int[] update(PlayersCamera player, int[] pixels) {
		// create wall, floor and sealing
		for(int n = 0; n < pixels.length/2; n++) {
			if(pixels[n] != Color.DARK_GRAY.getRGB()) pixels[n] = Color.DARK_GRAY.getRGB();
		}
		for(int i = pixels.length/2; i < pixels.length; i++) {
			if(pixels[i] != Color.gray.getRGB()) pixels[i] = Color.gray.getRGB();
		}

		for(int x = 0; x < screenWidth; x = x+1) {
			double cameraX = 2 * x / (double)(screenWidth) -1;
			double rayDirX = player.xDir + player.xPlane * cameraX;
			double rayDirY = player.yDir + player.yPlane * cameraX;
			// Map position
			int mapX = (int)player.xPos;
			int mapY = (int)player.yPos;
			// Length of ray from current position to next x or y-side
			double sideDistX;
			double sideDistY;
			// Length of ray from one side to next in map
			double deltaDistX = Math.sqrt(1 + (rayDirY*rayDirY) / (rayDirX*rayDirX));
			double deltaDistY = Math.sqrt(1 + (rayDirX*rayDirX) / (rayDirY*rayDirY));
			double perpWallDist;
			// Direction to go in x and y
			int stepX, stepY;
			// colision detection
			boolean hit = false;// was a wall hit
			int side=0;// was the wall vertical or horizontal

			//Figure out the step direction and initial distance to a side
			if (rayDirX < 0) {
				stepX = -1;
				sideDistX = (player.xPos - mapX) * deltaDistX;
			}
			else {
				stepX = 1;
				sideDistX = (mapX + 1.0 - player.xPos) * deltaDistX;
			}

			if (rayDirY < 0) {
				stepY = -1;
				sideDistY = (player.yPos - mapY) * deltaDistY;
			}
			else {
				stepY = 1;
				sideDistY = (mapY + 1.0 - player.yPos) * deltaDistY;
			}

			//Loop to find where the ray hits a wall
			while(!hit) {
				//Jump to next square
				if (sideDistX < sideDistY) {
					sideDistX += deltaDistX;
					mapX += stepX;
					side = 0;
				}
				else {
					sideDistY += deltaDistY;
					mapY += stepY;
					side = 1;
				}
				//Check if ray has hit a wall
				if(gameMap[mapX][mapY] > 0) hit = true;
			}

			//Calculate distance to the point of impact
			if(side == 0) {
				perpWallDist = Math.abs((mapX - player.xPos + (1 - stepX) / 2) / rayDirX);
			}
			else {
				perpWallDist = Math.abs((mapY - player.yPos + (1 - stepY) / 2) / rayDirY);
			}

			//Now calculate the height of the wall based on the distance from the camera
			int lineHeight;
			if(perpWallDist > 0) {
				lineHeight = Math.abs((int)(screenHeight / perpWallDist));
			}
			else {
				lineHeight = screenHeight;
			}

			//calculate lowest and highest pixel to fill in current stripe
			int drawStart = -lineHeight/2+ screenHeight/2;
			if(drawStart < 0) {
				drawStart = 0;
			}

			int drawEnd = lineHeight/2 + screenHeight/2;

			if(drawEnd >= screenHeight) {
				drawEnd = screenHeight - 1;
			}

			//add a texture
			int texNum = gameMap[mapX][mapY] - 1;
			double wallX;//Exact position of where wall was hit

			if(side == 1) {//If its a y-axis wall
				wallX = (player.xPos + ((mapY - player.yPos + (1 - stepY) / 2) / rayDirY) * rayDirX);
			}
			else {//X-axis wall
				wallX = (player.yPos + ((mapX - player.xPos + (1 - stepX) / 2) / rayDirX) * rayDirY);
			}

			wallX -= Math.floor(wallX);
			//x coordinate on the texture
			int texX = (int)(wallX * (textures.get(texNum).TEXTURE_SIZE));

			if(side == 0 && rayDirX > 0) texX = textures.get(texNum).TEXTURE_SIZE - texX - 1;
			if(side == 1 && rayDirY < 0) texX = textures.get(texNum).TEXTURE_SIZE - texX - 1;

			//calculate y coordinate on texture
			for(int y = drawStart; y < drawEnd; y++) {
				int texY = (((y*2 - screenHeight + lineHeight) << 6) / lineHeight) / 2;
				int color;
				if( side == 0)  {
					color = textures.get(texNum).pixels[texX + (texY * textures.get(texNum).TEXTURE_SIZE)];
				}
				else {
					color = (textures.get(texNum).pixels[texX + (texY * textures.get(texNum).TEXTURE_SIZE)]>>1) & 8355711;//Make y sides darker
				}
				pixels[x + y*(screenWidth)] = color;
			}
		}

		return pixels;
	}
}
