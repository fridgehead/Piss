import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.sound.sampled.UnsupportedAudioFileException;

import JMyron.JMyron;



public class MainGameThread extends GameThread {


	PlayerObject player;


	int playerProgress = 600;
	int playerSpeed = 10;
	int lastPlayerSpeed = 10;
	long lastHitTime = 0;
	long lastNetworkUpdate = 0;
	Point[] stars = new Point[40];
	Point[] backStars = new Point[40];
	BufferedImage fasterText, progressText, noMorePissText, pissMoreTimer, takePhotoText;
	long fasterTextDisplayTime = 0;
	long shakeTime = 0;
	long playerDeathTimeOut = 0;
	int recoverPosition = 0;
	boolean shake = false;
	boolean outOfPiss = false, gameOver = false;;
	long outOfPissTime = 0;
	ArrayList<PlayerObject> otherPlayers = new ArrayList<PlayerObject>();


	int[] levelData = new int[400];

	private AffineTransform screenTransform;
	BufferedImage bi = new BufferedImage(640, 480, BufferedImage.TYPE_INT_ARGB);

	private long gameOverTime;


	private JMyron jmyron;


	public MainGameThread(Skeleton parent){
		super(parent);
		fasterText = parent.fixedFont.getImageFromString("<< SPEED INCREASE");
		noMorePissText = parent.fixedFont.getImageFromString("PISS MORE");
		pissMoreTimer = parent.fixedFont.getImageFromString("5");
		takePhotoText = parent.fixedFont.getImageFromString("GAME OVER");
		progressText = parent.fixedFont.getImageFromString("SCORE " + playerProgress);
		player = new PlayerObject(650, -500, parent.spriteBank.getSpriteByName("player"));
		player.isActive = true;
		player.setAnimation(0);
		//cam = new Camera(new Point(0,0), new Point(800,600));
		for (int i = 0; i < 40; i++){
			stars[i] = new Point((int)(Math.random() * 800), (int)(Math.random() * 600));
			backStars[i] = new Point((int)(Math.random() * 800), (int)(Math.random() * 600));
		}

		//generate a level
		int curPos = 3;
		for(int i = 0; i < 400; i++){
			int newPos = (-1 +  (int)(Math.random() * 3));
			curPos += newPos;
			if(curPos < 0){
				curPos = 1;

			} else if(curPos > 5){
				curPos = 4;
			}
			levelData[i] = curPos;
			System.out.print(curPos + ",");

		}
		System.out.println();
		screenTransform = new AffineTransform();

		jmyron = new JMyron();
		jmyron.start(640,480);

	}
	public void newScoreFromNetwork(String name, String location, int score){
		parent.newScoreFromNetwork(name, location, score);
	}
	
	public void setOtherPlayerPos(int id, int xPos, int playerProgress){
		int ct = 0;
		for(PlayerObject p : otherPlayers){
			if(p.playerId == id){
				p.worldPosition.x = xPos;
				p.playerProgress = playerProgress;
			}
			ct++;
		}
		if(ct == 0){
			PlayerObject p = new PlayerObject(xPos, -500, parent.spriteBank.getSpriteByName("player"));
			p.playerId = id;
			p.playerProgress = playerProgress;
			otherPlayers.add(p);
			
		}
	}

	public void start(){
		super.start();
		lastHitTime = System.currentTimeMillis();
		//generate a level
		/*int curPos = 3;
		for(int i = 0; i < 400; i++){
			int newPos = (-1 +  (int)(Math.random() * 3));
			curPos += newPos;
			if(curPos < 0){
				curPos = 1;

			} else if(curPos > 5){
				curPos = 4;
			}
			levelData[i] = curPos;
			System.out.print(curPos + ",");

		}
		*/
		player.worldPosition.x = 50 + recoverPosition * 100;
		player.moveTo(player.worldPosition.x);
		

	}

	public void stop(){
		super.stop();
		soundManager.setBgm(null);
		playerProgress = 600;
		playerSpeed = 10;
		inputEngine.removeListener(this);
		outOfPiss = false;
		gameOver = false;

	}
	public void handleInputEvent(int evt){
		if(isRunning){
			if(outOfPiss == false){
				if (evt == InputEngine.KEY_ESC){
					parent.quit();
				} else if( evt == InputEngine.KEY_TRACK0) {
					player.moveTo(50);
				}else if( evt == InputEngine.KEY_TRACK1) {
					player.moveTo(150);
				} else if( evt == InputEngine.KEY_TRACK2) {
					player.moveTo(250);
				} else if( evt == InputEngine.KEY_TRACK3) {
					player.moveTo(350);
				}else if( evt == InputEngine.KEY_TRACK4) {
					player.moveTo(450);
				} else if( evt == InputEngine.KEY_TRACK5) {
					player.moveTo(550);
				}else if( evt == InputEngine.KEY_TRACK6) {
					player.moveTo(650);
				} else if (evt == InputEngine.KEY_NOMOREPISS){
					//the controller has determined there is no more piss
					outOfPiss = true;
					outOfPissTime = System.currentTimeMillis();
					lastPlayerSpeed = playerSpeed;
					playerSpeed = 0;

				}
			} else {
				if(evt != InputEngine.KEY_NOMOREPISS){
					playerSpeed = lastPlayerSpeed;
					outOfPiss = false;
				}
			}
		}
	}

	public void updateState(){	
		super.updateState(); // needed for sound manager to trigger
		player.think();
		playerProgress += playerSpeed;
		for(int i = 0; i < 40; i++){
			stars[i].y += playerSpeed;
			if(stars[i].y > 600){
				stars[i].y = 0;
				stars[i].x = (int)(Math.random()  * 800);
			}
			backStars[i].y += playerSpeed / 2;
			if(backStars[i].y > 600){
				backStars[i].y = 0;
				backStars[i].x = (int)(Math.random()  * 800);
			}
		}

		//we didnt hit anything, speed up
		if(lastHitTime + 1000 < System.currentTimeMillis() && outOfPiss == false && gameOver == false){
			playerSpeed += 5;
			lastHitTime = System.currentTimeMillis();
			fasterTextDisplayTime = lastHitTime;
		}
		progressText = parent.fixedFont.getImageFromString("SCORE " + playerProgress);

		if(shakeTime > System.currentTimeMillis() - 200){
			shake = true;
			screenTransform.setToTranslation(Math.random() * 20 - 10, Math.random() * 20 - 10);
		} else {
			shake = false;
			screenTransform.setToTranslation(0,0);
		}

		//check for collisions
		int currentLevelSegment = playerProgress / 200;

		//System.out.println(shiftLevel);


		if(currentLevelSegment > 5){
			int leftX = 50 + levelData[currentLevelSegment - 5] * 100;
			int rightX = leftX + 400;

			if(player.worldPosition.x < leftX || player.worldPosition.x + player.sprite.size.x > rightX){

				System.out.println("hit");
				lastHitTime = System.currentTimeMillis();
				shakeTime = System.currentTimeMillis();
				playerDeathTimeOut = System.currentTimeMillis();
				playerSpeed = 0;
				recoverPosition = levelData[currentLevelSegment - 5] + 1;

			}
		}

		if(playerDeathTimeOut > System.currentTimeMillis() - 200){
			player.worldPosition.x = 50 + recoverPosition * 100;
			player.moveTo(player.worldPosition.x);
			playerSpeed = 15;

		}

		if(outOfPissTime < System.currentTimeMillis() - 5000 && outOfPiss == true){
			//switch to high score mode. Pass the highscore back to parent
			//parent.finishedWithScore(playerProgress);
			gameOver = true;
			gameOverTime = System.currentTimeMillis();
			outOfPiss = false;
		}

		if(gameOverTime < System.currentTimeMillis() - 3000 && gameOver == true){
			//switch to high score mode. Pass the highscore back to parent
			BufferedImage snap = bi;
			parent.finishedWithScore(playerProgress,snap);;
		}

	}





	public void repaint()
	{
		if(isRunning){
			Graphics2D g2 = (Graphics2D) bufferGraphics;
			g2.setTransform(screenTransform);


			g2.setColor(new Color(0,0,0));
			g2.fillRect(0, 0, 800, 600);
			g2.setColor(new Color(230,230,230));
			//draw a starry BG
			for(int i = 0; i < 40; i++){
				g2.fillOval(backStars[i].x, backStars[i].y, 5, 5);
				g2.fillOval(stars[i].x, stars[i].y, 7, 7);
			}

			//draw level
			int currentLevelSegment = playerProgress / 200;
			int shiftLevel = 0;
			shiftLevel = ( playerProgress - (currentLevelSegment * 200)) / 2 - 100;
			//System.out.println(shiftLevel);
			g2.setColor(new Color(100,100,100));
			for(int i = 0; i < 7; i++){
				if(currentLevelSegment - i > 0){

					//left
					g2.fillRect(0,i * 100 + shiftLevel,50 + levelData[currentLevelSegment - i] * 100, 100);


					//right
					g2.fillRect(50 + levelData[currentLevelSegment - i] * 100 + 400, i * 100 + shiftLevel, 500, 100);
				}
			}
			//System.out.println(currentLevelSegment);


			//draw player
			player.draw(g2, camera);
			//System.out.print(camera.toScreenPosition(player.worldPosition));

			g2.setColor(new Color(200,200,200));
			for(int i = 0; i < 7; i++){
				g2.drawLine(50 + i * 100, 0, 50 + i * 100, 600);
			}


			if(fasterTextDisplayTime + 500 > System.currentTimeMillis()){
				AffineTransform f = g2.getTransform();
				g2.translate(60,-180);
				g2.rotate(Math.PI / 2);

				g2.drawImage(fasterText, 0, 0, fasterText.getWidth() * 2, fasterText.getHeight() * 2, null);
				g2.setTransform(f);

			}
			
			g2.drawImage(progressText, 50, 50, null);

			if(outOfPiss){
				int timer = (int) ( 5 - (System.currentTimeMillis() - outOfPissTime) / 1000 );
				pissMoreTimer = parent.fixedFont.getImageFromString("" + timer);
				g2.drawImage(pissMoreTimer, 150, 200,pissMoreTimer.getWidth() * 4, pissMoreTimer.getHeight() * 4, null);
				g2.drawImage(noMorePissText,-100,100,noMorePissText.getWidth() * 4, noMorePissText.getHeight() * 4, null);
			}

			if(gameOver){
				//drawCamera(g2);
				g2.drawImage(takePhotoText, -200, 200,takePhotoText.getWidth() * 4, takePhotoText.getHeight() * 4, null);
			}
			
			if(lastNetworkUpdate + 50 < System.currentTimeMillis()){
				parent.tcpClient.sendMessage(playerProgress + "," + player.worldPosition.x);
				lastNetworkUpdate = System.currentTimeMillis();
			}
			
			g2.setColor(new Color(0,255,0));
			for(PlayerObject p : otherPlayers){
				int yPos = playerProgress - p.playerProgress;
				g2.fillRect(p.worldPosition.x, 500 + yPos, 50,50);
			}

		}
	}

	public void setLevel(String[] elements) {
		int[] levelNew = new int[400];
		int count = 0;
		for(String s : elements){
			if(! s.equals("level") && !s.equals("")){
				levelNew[count] = Integer.parseInt(s);
				count++;
			}
		}
		levelData = levelNew;
	}
	
	private void drawCamera(Graphics2D g2){
        jmyron.update();//update the camera view
        int[] img = jmyron.image(); //get the normal image of the camera
        bi.setRGB(0,0,640,480,img,0,640);
        g2.drawImage(bi, 280, 60, 320, 240, null);
	}

	

}
