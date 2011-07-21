import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public class HighScoreThread extends GameThread {

	String[] scoreNames = new String[10];
	int[] scores = new int[10];
	
	BufferedImage titleImage;
	private long timeStarted;
	
	public HighScoreThread(Skeleton parent) {
		super(parent);
		titleImage = parent.fixedFont.getImageFromString("HIGH SCORES");
		isReady = true;
	}
	
	public void start(){
		super.start();
		timeStarted = System.currentTimeMillis();
	}

	public void stop(){
		//reset everything
		super.stop();
	}
	
	public void updateState(){
		super.updateState();
		//get new scores
		if(timeStarted + 2000 < System.currentTimeMillis()){
			//switch to high score
			parent.nextState();
		}
	}

	public void repaint(){
		if(isReady){
			Graphics2D g2 = (Graphics2D) bufferGraphics;
			g2.setColor(new Color(255,255,255));
			g2.fillRect(0, 0, 800, 600);
			g2.drawImage(titleImage, 100,100, null);
		}
		
	}
	
	public void handleInputEvent(int evt){
		//on any event start game
		System.out.println("START THE GAME BOY");
    }
	
}
