import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.UnsupportedAudioFileException;


public class MainGameThread extends GameThread {


	private BufferedImage bgimg,rainTileImage;
	private int rainX = 0, rainY = 0;

	PlayerObject player;
	private BufferedImage coinAmount;
	private BufferedImage overlay;
	

	public MainGameThread(Skeleton parent){
		super(parent);
		

		
	}

	public void start(){
		super.start();
	
	}

	public void stop(){
		super.stop();
		soundManager.setBgm(null);

	}
	public void handleInputEvent(int evt){
		if(isRunning){
		if (evt == InputEngine.KEY_ESC){
				parent.quit();
		}
		}
	}

	public void updateState(){	
		super.updateState(); // needed for sound manager to trigger
		camera.position.x = player.worldPosition.x - (parent.getWidth() / 2) + 100;
		int keyState = inputEngine.getKeyMask();
		if((keyState & InputEngine.KEY_RIGHT ) > 0){
			player.move(8);
		} else if ((keyState & InputEngine.KEY_LEFT )> 0){
			player.move(-8);
		} else {
			player.move(0);
		}

		//update rain pos
		rainX += -4;
		rainX %= 32;
		rainY += 9;
		rainY %= 32;
		
		
	}

	public void repaint()
	{
		if(isRunning){
			//System.out.println("thread draw");
			Graphics2D g2 = (Graphics2D) bufferGraphics;
			g2.setColor(new Color(0,0,0));
			g2.clearRect(0, 0, 800, 600);
			
						
		}
	}


}
