import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.imageio.ImageIO;


public class HighScoreThread extends GameThread {

	//ScoreItem[] scores = new ScoreItem[10];
	ArrayList<ScoreItem> scores = new ArrayList<ScoreItem>(10);
	String[] locations = new String[10];

	Camera cam;

	BufferedImage titleImage,pissToStartImage, barnImage;
	private long titleLastBlink;
	private boolean titleDisplay = false;

	private long timeStarted;
	int scrollY = 300;
	
	boolean newHighScore = false;
	long lastLocalScore;
	
	

	public HighScoreThread(Skeleton parent) {
		super(parent);
		titleImage = parent.fixedFont.getImageFromString("HIGH SCORES");
		pissToStartImage = parent.fixedFont.getImageFromString("Pee to start");
		
		isReady = true;
		cam = new Camera(new Point(0,0), new Point(800,600));
		try{
			for (int i = 0; i < 10; i++){
				BufferedImage t = ImageIO.read(new File("img/face1.png"));
				t = resize(t, 64,48);
				int s = 100 * i;
				scores.add(new ScoreItem(t, s, "" + parent.playerId));


			}
			barnImage = ImageIO.read(new File("img/barn.png"));
		} catch (Exception e){
			e.printStackTrace();
		}
		Collections.sort(scores);
		lastLocalScore = 0;

	}
	
	public void newScoreFromNetwork(String name, String location, int score, long randId){
		ScoreItem s = new ScoreItem(null, score, name);
		s.loc = location;
		s.name = name;
		s.networkId = randId;
			if(lastLocalScore == randId){
				s.newScore = true;
			}
		
		scores.add(s);
		Collections.sort(scores);
		//scores.remove(scores.size() - 1);
		
	}
	
	public void newScore(int score, BufferedImage snap){
		BufferedImage t;
		
		
			if(score > scores.get(0).score){
				newHighScore = true;
			}
			t = snap;
			t = resize(t, 64,48);
			ScoreItem s = new ScoreItem(t,score, "" + parent.playerId);
			
			s.newScore = true;
			parent.tcpClient.sendScore(s);
			lastLocalScore = s.networkId;
			scores.add(s);
			Collections.sort(scores);
			scores.remove(scores.size() - 1);
			s.loc  = "London";
		
		
	}

	public  BufferedImage resize(BufferedImage img, int newW, int newH) {  
		int w = img.getWidth();  
		int h = img.getHeight();  
		BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);  
		Graphics2D g = dimg.createGraphics();  
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);  
		g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);  
		g.dispose();  
		return dimg;  
	}  

	public void start(){
		super.start();
		timeStarted = System.currentTimeMillis();
		scrollY = -600;
		newHighScore = false;
		scores = new ArrayList<HighScoreThread.ScoreItem>();
		parent.requestScoreUpdate();
	}

	public void stop(){
		//reset everything
		super.stop();
		for(ScoreItem s : scores){
			s.newScore = false;
		}

	}

	public void updateState(){
		super.updateState();
		//get new scores
		if(scrollY > 1100){
			//switch to high score
			parent.nextState();
		}
		scrollY+=3;
		if(titleLastBlink + 500 < System.currentTimeMillis()){
			titleLastBlink = System.currentTimeMillis();
			titleDisplay = !titleDisplay;
		}
	}

	public void repaint(){
		if(isReady){
			Graphics2D g2 = (Graphics2D) bufferGraphics;
			g2.setColor(new Color(0,0,0));
			g2.fillRect(0, 0, 800, 600);
			Point titlePos = cam.toScreenPosition(new Point(-30,scrollY));
			g2.drawImage(titleImage, titlePos.x,titlePos.y, titleImage.getWidth() * 3, titleImage.getHeight() * 3, null);

			int baseY = titlePos.y + 260 ;
			
			//big image at the top for the CHAMP
			if(scores.size() > 0){
			ScoreItem s = scores.get(0);
			if(s.newScore){
				g2.setColor (new Color ((int)(Math.random() * 255),(int)(Math.random() * 255),(int)(Math.random() * 255)));
				g2.fillRect(0, baseY - 160, 800, 192);
				//System.out.println("oer 900");
			}
			g2.drawImage(s.faceImage, titlePos.x + 130, baseY - 160 , 128,96, null);
			g2.drawImage(s.scoreImage, titlePos.x + 180, baseY - 130, s.scoreImage.getWidth() * 2, scores.get(0).scoreImage.getHeight() * 2, null);

			//draw the lower list
			for(int i = 1; i < scores.size(); i++){
				s = scores.get(i);
				if(s.newScore){
					g2.setColor (new Color ((int)(Math.random()) * 255,(int)(Math.random() * 255),(int)(Math.random() * 255)));
					g2.fillRect(0, baseY +  (60*i), 800, 48);
				}
				g2.drawImage(s.faceImage, titlePos.x + 130, baseY +  (60*i), 64,48, null);
				g2.drawImage(s.scoreImage, titlePos.x + 180, baseY + (60*i), s.scoreImage.getWidth() * 2, s.scoreImage.getHeight() * 2, null);
			}
			}



			if(titleDisplay){
				g2.drawImage(pissToStartImage, 230, 540,null);
			}
		}

	}

	public void handleInputEvent(int evt){
		//on any event start game
		if(evt == InputEngine.KEY_ESC){
			parent.quit();
		} else if (evt != InputEngine.KEY_NOMOREPISS){
		
			if(newHighScore == false){
				parent.startGame();
			}

		}
	}

	/*
	 * Inner class for score items
	 */
	public class ScoreItem implements Comparable<ScoreItem>{
		public String loc = "", name = "";
		public int score = 0;
		public BufferedImage scoreImage, faceImage;
		public boolean newScore = false;
		public long networkId = 0;
		
		public String file = "";

		public ScoreItem(BufferedImage bufIn, int score, String name){
			this.score = score;
			this.faceImage = bufIn;		
			scoreImage = parent.fixedFont.getImageFromString("Player " + name + " " + score);
			if(bufIn == null){
				faceImage = resize(barnImage, 64,48);
			}
			networkId = System.currentTimeMillis();
		}
		
		public void setImageFromFile(String in){
			file = in;
			BufferedImage t;
			try {
				t = ImageIO.read(new File("img/" + file));
				t = resize(t, 64,48);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}

		public int compareTo(ScoreItem o) {

			ScoreItem a = (ScoreItem)o;
			if(a.score > score){
				return 1;
			} else if (a.score < score){
				return -1;

			}else {
				return 0;
			}


		}

	}

}
