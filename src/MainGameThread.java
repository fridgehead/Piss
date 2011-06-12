import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.UnsupportedAudioFileException;


public class MainGameThread extends GameThread {


	private BufferedImage img;
	


	public MainGameThread(Skeleton parent){
		super(parent);
		
		try {
			img = ImageIO.read(new File("img/main.png"));
			

		} catch (IOException e) {
			System.out.println("fucked");
		}
		
		
		//soundManager.playSound(SoundClip.SONIC);
		
	}


	public void updateState(){	}
	
	public void repaint()
	{
		if(isRunning){
			//System.out.println("thread draw");
			Graphics2D g2 = (Graphics2D) bufferGraphics;
			g2.drawImage(img,0,0,800,600, null);
			
			

		}
	}


}
