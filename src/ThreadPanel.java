import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;




/*
 * A Threaded panel
 */
public class ThreadPanel extends JPanel{
	
	GameThread currentThread;
	UpdateThread updater;


	
	public ThreadPanel(GameThread startThread){
		currentThread = startThread;
		updater = new UpdateThread(this);
		updater.start();
		currentThread.start();
		
	}
	
	public void changeThread(GameThread nextThread){
		currentThread.stop();
		currentThread = nextThread;
		currentThread.start();
	}
	
	public void stop(){
		System.out.println("stopped jpanel");
		
	}
	
	public void updateState(){
		currentThread.updateState();
	}
	
	public void paint(Graphics g){
		super.paint(g);
		currentThread.repaint();

		g.drawImage(currentThread.getBuffer(), 0,0, null);
		
	}

	

}
