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


	PlayerObject player;
	

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
		
		
		
	}

	public void repaint()
	{
		if(isRunning){
			//System.out.println("thread draw");
			Graphics2D g2 = (Graphics2D) bufferGraphics;
			g2.setColor(new Color(250,0,0));
			g2.clearRect(0, 0, 800, 600);
			
						
		}
	}


}
