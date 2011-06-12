import java.awt.Graphics;
import java.awt.image.BufferedImage;


public abstract class GameThread{

		  boolean isRunning = false;
		  BufferedImage buffer;
		  Graphics bufferGraphics;
		  Skeleton parent;
		  SoundManager soundManager;
		  InputEngine inputEngine;
		  
		  public GameThread(Skeleton parent){
			  buffer = new BufferedImage(800,600, BufferedImage.TYPE_INT_ARGB);
			  bufferGraphics = buffer.getGraphics();
			  this.parent = parent;
			  inputEngine = parent.inputEngine;
			  soundManager = parent.soundManager;
			  
		  }
		  
		 
		  //called whenever FPS is ready, repaints to buffer
		  public void repaint(){}
		  public BufferedImage getBuffer(){
			  return buffer;
		  }
		  //called periodically
		  public void updateState(){
			  soundManager.think();
			  
		  }

		  /*public void handleInputEvent(int code){
		  }*/

		  public void start(){
			  isRunning = true;
			  
		  }
		  public void stop(){
			  isRunning = false;
		  }

		  public void handleInputEvent(int evt){
			  
		  }
		  
}


