import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;


public abstract class GameThread{

		  boolean isRunning = false;
		  boolean isReady = false;
		  BufferedImage buffer;
		  Graphics bufferGraphics;
		  Skeleton parent;
		  SoundManager soundManager;
		  InputEngine inputEngine;
		  Camera camera;
		  
		  public GameThread(Skeleton parent){
			  buffer = new BufferedImage(800,600, BufferedImage.TYPE_INT_ARGB);
			  bufferGraphics = buffer.getGraphics();
			  this.parent = parent;
			  inputEngine = parent.inputEngine;
			  soundManager = parent.soundManager;
			  camera = new Camera(new Point(0,0), new Point(Skeleton.WINDOW_WIDTH, Skeleton.WINDOW_HEIGHT));
			  
			  
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
				inputEngine.addListener(this);

			  
		  }
		  public void stop(){
			  isRunning = false;
				inputEngine.removeListener(this);

		  }

		  public void handleInputEvent(int evt){
			  
		  }
		  
}


