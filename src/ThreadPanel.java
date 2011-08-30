
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
	private Object lock = new Object();
int x= 0;
	
	public ThreadPanel(GameThread startThread){
		currentThread = startThread;
		updater = new UpdateThread(this);
		
		//currentThread.start();
		
	}
	
	public void start(){
		System.out.println("starting panel");
		updater.start();
	}
	
	public void changeThread(GameThread nextThread){
		currentThread.stop();
		currentThread = nextThread;
		currentThread.start();
	}
	
	public void stop(){
		System.out.println("stopped jpanel");
		updater.end();
		try {
			updater.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
