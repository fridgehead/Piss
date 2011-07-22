import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;

import javax.imageio.ImageIO;


public class HighScoreThread extends GameThread {

	ScoreItem[] scores = new ScoreItem[10];
	String[] locations = new String[10];

	Camera cam;

	BufferedImage titleImage,pissToStartImage;
	private long titleLastBlink;
	private boolean titleDisplay = false;

	private long timeStarted;
	int scrollY = 300;

	public HighScoreThread(Skeleton parent) {
		super(parent);
		titleImage = parent.fixedFont.getImageFromString("HIGH SCORES");
		pissToStartImage = parent.fixedFont.getImageFromString("Wee to start");

		isReady = true;
		cam = new Camera(new Point(0,0), new Point(800,600));

		try{
			for (int i = 0; i < 10; i++){
				BufferedImage t = ImageIO.read(new File("img/face1.png"));
				t = resize(t, 64,48);
				int s = 100 * i;
				scores[i] = new ScoreItem(t, s);


			}
		} catch (Exception e){
			e.printStackTrace();
		}
		Arrays.sort(scores);
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
	}

	public void stop(){
		//reset everything
		super.stop();

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
			g2.setColor(new Color(255,255,255));
			g2.fillRect(0, 0, 800, 600);
			Point titlePos = cam.toScreenPosition(new Point(-30,scrollY));
			g2.drawImage(titleImage, titlePos.x,titlePos.y, titleImage.getWidth() * 3, titleImage.getHeight() * 3, null);

			int baseY = titlePos.y + 260 ;
			//big image at the top for the CHAMP
			g2.drawImage(scores[0].faceImage, titlePos.x + 130, baseY - 160 , 256,192, null);
			g2.drawImage(scores[0].scoreImage, titlePos.x + 230, baseY - 130, scores[0].scoreImage.getWidth() * 4, scores[0].scoreImage.getHeight() * 4, null);
			
			//draw the lower list
			for(int i = 1; i < 10; i++){
				g2.drawImage(scores[i].faceImage, titlePos.x + 130, baseY +  (60*i), 64,48, null);
				g2.drawImage(scores[i].scoreImage, titlePos.x + 230, baseY + (60*i), scores[i].scoreImage.getWidth() * 2, scores[i].scoreImage.getHeight() * 2, null);
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
		} else {
			System.out.println("START THE GAME BOY");
		}
	}

	/*
	 * Inner class for score items
	 */
	public class ScoreItem implements Comparable<ScoreItem>{
		public int score = 0;
		public BufferedImage scoreImage, faceImage;

		public ScoreItem(BufferedImage bufIn, int score){
			this.score = score;
			this.faceImage = bufIn;		
			scoreImage = parent.fixedFont.getImageFromString("" + score);
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
