import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.UnsupportedAudioFileException;


public class PostTitleThread extends GameThread {


	private BufferedImage img;

	private BufferedImage startImage;

	private long lastBlinkTime = 0;
	private boolean coinDisplay = false;


	public PostTitleThread(Skeleton parent){
		super(parent);

		try {
			img = ImageIO.read(new File("img/postTitle.png"));
			startImage = ImageIO.read(new File("img/pressStart.png"));


		} catch (IOException e) {
			System.out.println("fucked");
		}
		isReady = true;

	}
	public void start(){
		super.start();

	}


	public void updateState(){	
		super.updateState(); // needed for sound manager to trigger

		if(lastBlinkTime + 1000 < System.currentTimeMillis()){
			lastBlinkTime = System.currentTimeMillis();
			coinDisplay = !coinDisplay;
		} 


	}

	public void handleInputEvent(int evt){
		if(isRunning){
			if(evt == InputEngine.KEY_COIN){
				parent.insertCoin();
				soundManager.playSound(SoundClip.COININSERT);
			} else if (evt == InputEngine.KEY_ESC){
				parent.quit();
			} else if (evt == InputEngine.KEY_START ){
				if(parent.credits > 0){
					System.out.println("LOL");
					soundManager.setBgm(null);
					parent.nextState();
				} 
			}
		}
	}

	public void repaint()
	{
		if(isRunning){
			//System.out.println("thread draw");
			Graphics2D g2 = (Graphics2D) bufferGraphics;
			g2.drawImage(img,0,0,800,600, null);



			if(coinDisplay ){

				g2.drawImage(startImage, 280, 480, startImage.getWidth() * 4, startImage.getHeight() * 4, null);

			}


		}
	}


}
