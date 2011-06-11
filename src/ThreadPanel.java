import java.awt.Graphics;

import javax.swing.JPanel;

import wiiremotej.WiiRemote;


/*
 * A Threaded panel
 */
public class ThreadPanel extends JPanel{
	
	GameThread currentThread;
	UpdateThread updater;
	WiiRemote remote;

	
	public ThreadPanel(GameThread startThread){
		currentThread = startThread;
		updater = new UpdateThread(this);
		updater.start();
	}
	
	public void stop(){
		System.out.println("stopped jpanel");
		remote.disconnect();
		
	}
	
	public void setRemote(WiiRemote in){
		remote = in;
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
