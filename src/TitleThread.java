import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.UnsupportedAudioFileException;


public class TitleThread extends GameThread {

	private BufferedImage titleText, pissToStartImage;
	private long titleLastBlink;
	private boolean titleDisplay = false;
	
	private long timeStarted;

	public TitleThread(Skeleton parent){
		super(parent);
		titleText = parent.fixedFont.getImageFromString("WEE WII");
		pissToStartImage = parent.fixedFont.getImageFromString("Wee to start");
		isReady = true;

	}
	public void start(){
		System.out.println("starting title...");
		super.start();
		timeStarted = System.currentTimeMillis();
		
		
	}

	public void stop(){
		super.stop();
		
	}

	public void updateState(){	
		super.updateState(); // needed for sound manager to trigger
		int keyMask = inputEngine.getKeyMask();
		if(keyMask != 0){


		}
		if(titleLastBlink + 500 < System.currentTimeMillis()){
			titleLastBlink = System.currentTimeMillis();
			titleDisplay = !titleDisplay;
		}
		
		if(timeStarted + 10000 < System.currentTimeMillis()){
			//switch to high score
			parent.nextState();
		}
		
	}

	public void handleInputEvent(int evt){
		if(isRunning){
			if(evt == InputEngine.KEY_ESC){
				parent.quit();
			} else if (evt != InputEngine.KEY_NOMOREPISS){
				

				parent.startGame();
			}
		}
	}

	public void repaint()
	{
		if(isRunning){
			//System.out.println("thread draw");
			Graphics2D g2 = (Graphics2D) bufferGraphics;
			g2.setColor(new Color(255,255,255));
			g2.fillRect(0, 0, 800, 600);
			if(titleDisplay){
				g2.drawImage(pissToStartImage, 230, 540,null);
			}
			g2.drawImage(titleText, -20,200, titleText.getWidth() * 4, titleText.getHeight() * 4,null);
		}
	}


}
