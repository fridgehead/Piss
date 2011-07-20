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
	QueueObject q;

	public MainGameThread(Skeleton parent){
		super(parent);

		try {
			bgimg = ImageIO.read(new File("img/main.png"));
			rainTileImage = ImageIO.read(new File("img/rainTile.png"));
			

		} catch (IOException e) {
			System.out.println("fucked");
		}
		player = new PlayerObject(0,-260,parent.spriteBank.getSpriteByName("Player")) ;		
		player.isActive = true;
		
		q = new QueueObject(200,-260,parent.spriteBank.getSpriteByName("OldWoman"));
		q.isActive = true;
		//soundManager.playSound(SoundClip.SONIC);

	}

	public void start(){
		super.start();
		soundManager.setBgm(SoundClip.RAINBGM);
	}

	public void stop(){
		super.stop();
		soundManager.setBgm(null);

	}
	public void handleInputEvent(int evt){
		if(isRunning){
			if(evt == InputEngine.KEY_COIN){
				parent.insertCoin();
				soundManager.playSound(SoundClip.COININSERT);
				
			} else if (evt == InputEngine.KEY_ESC){
				parent.quit();
			} else {
				q.setDestination(q.worldPosition.x + 100);
			}
		}
	}

	public void updateState(){	
		super.updateState(); // needed for sound manager to trigger
		
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
		player.think();
		q.think();
	}

	public void repaint()
	{
		if(isRunning){
			//System.out.println("thread draw");
			Graphics2D g2 = (Graphics2D) bufferGraphics;
			g2.setColor(new Color(0,0,0));
			g2.clearRect(0, 0, 800, 600);
			Point bgPos = camera.toScreenPosition(new Point(0,0));
			
			g2.drawImage(bgimg,bgPos.x,bgPos.y,800,600, null);
			
			
			player.draw(bufferGraphics, camera);
			q.draw(bufferGraphics, camera);

			//draw the rain pattern

			for(int x = 0; x < 896 / 64; x++){
				for(int y = 0; y < 640 / 64; y++){
					g2.drawImage(rainTileImage, x * 64 + rainX, y * 64 + rainY, 64, 64, null);

				}
			}
			

		}
	}


}
