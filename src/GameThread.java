import java.awt.Graphics;
import java.awt.image.BufferedImage;


public abstract class GameThread{

		  boolean isRunning = false;
		  BufferedImage buffer;
		  Graphics bufferGraphics;
		  ThreadPanel parent;
		  SoundManager soundManager;
		  
		  public GameThread(ThreadPanel parent){
			  buffer = new BufferedImage(800,600, BufferedImage.TYPE_INT_ARGB);
			  bufferGraphics = buffer.getGraphics();
			  this.parent = parent;
		  }

		  public void setSoundManager(SoundManager s){
			  soundManager = s;
		  }
		  
		  //called whenever FPS is ready, repaints to buffer
		  public void repaint(){}
		  public BufferedImage getBuffer(){
			  return buffer;
		  }
		  //called periodically
		  public void updateState(){}

		  /*public void handleInputEvent(int code){
		  }*/

		  public void start(){
			  isRunning = true;
			  
		  }
		  public void stop(){
			  isRunning = false;
		  }

}


