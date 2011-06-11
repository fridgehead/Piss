import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.UnsupportedAudioFileException;

import wiiremotej.WiiRemote;
import wiiremotej.WiiRemoteExtension;
import wiiremotej.event.WRAccelerationEvent;
import wiiremotej.event.WRButtonEvent;
import wiiremotej.event.WRCombinedEvent;
import wiiremotej.event.WRExtensionEvent;
import wiiremotej.event.WRIREvent;
import wiiremotej.event.WRStatusEvent;
import wiiremotej.event.WiiRemoteListener;

/*
 * Isnt the main thread, its just the title "get a wii controller" thread
 */
public class MainThread extends GameThread implements RemoteFinderEvent, WiiRemoteListener{


	private BufferedImage img;
	private BufferedImage spaceImage;
	private BufferedImage foundImage;
	private BufferedImage waitImage;

	//state machine
	private int state = STATE_PRESEARCH;

	public static final int STATE_PRESEARCH = 0;
	public static final int STATE_SEARCHING = 1;
	public static final int STATE_FOUND = 2;
	public static final int STATE_WAITING = 4;

	private long waitTimer = 0;
	private long titleTimer = 0;

	RemoteFinder f;
	SoundManager s;

	public MainThread(ThreadPanel parent){
		super(parent);
		s = new SoundManager();
		try {
			img = ImageIO.read(new File("img/bg.png"));
			spaceImage = ImageIO.read(new File("img/space.png"));
			foundImage = ImageIO.read(new File("img/found.png"));
			waitImage = ImageIO.read(new File("img/wait.png"));

		} catch (IOException e) {
			System.out.println("fucked");
		}
		f = new RemoteFinder(this);
		f.start();
		waitTimer = System.currentTimeMillis();

		f.findRemotes();
		s.playSound();
		
	}


	public void updateState(){
		//System.out.println("t: " + waitTimer);
		if(state == STATE_PRESEARCH){
			if(waitTimer + 3000 < System.currentTimeMillis()){
				state = STATE_SEARCHING;
				System.out.println("state change");

			}
		} else if (state == STATE_FOUND){
			if(waitTimer + 1000 < System.currentTimeMillis()){
				state = STATE_WAITING;
				
			}
		}


	}

	public void setState(int stateIn){
		state = stateIn;

	}





	public void repaint()
	{
		if(isRunning){
			//System.out.println("thread draw");
			Graphics2D g2 = (Graphics2D) bufferGraphics;
			g2.drawImage(img,0,0,800,600, null);
			if(state != STATE_WAITING){
				g2.setColor(new Color(0,0,0,0.8f));
				g2.fillRect(0,0,800,600);
			}
			if(state == STATE_PRESEARCH){
				g2.drawImage(waitImage, 200,200, waitImage.getWidth() * 2, waitImage.getHeight() * 2, null);
			} else if (state == STATE_FOUND){
				g2.drawImage(foundImage, 200,200, foundImage.getWidth() * 2, foundImage.getHeight() * 2, null);

			} else if (state == STATE_SEARCHING){
				g2.drawImage(spaceImage, 200,200, spaceImage.getWidth() * 2, spaceImage.getHeight() * 2, null);

			}
			

		}
	}

	public void foundRemote(WiiRemote r) {
		// TODO Auto-generated method stub
		System.out.println("got one");
		state = STATE_FOUND;
		waitTimer = System.currentTimeMillis();
		
		parent.setRemote(r);
		r.addWiiRemoteListener(this);
		try {
			r.requestStatus();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		

		try {
			r.setLEDIlluminated(3, true);			
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("aids");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	public void gaveUp() {
		// TODO Auto-generated method stub
		System.out.println("gave up");

	}


	public void IRInputReceived(WRIREvent arg0) {
		// TODO Auto-generated method stub
		
	}


	public void accelerationInputReceived(WRAccelerationEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	public void buttonInputReceived(WRButtonEvent evt) {
		// TODO Auto-generated method stub
		System.out.println(evt.isPressed(WRButtonEvent.A));
		
	}


	public void combinedInputReceived(WRCombinedEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	public void disconnected() {
		// TODO Auto-generated method stub
		//PANIC AND DO SOMETHING CLEVER
		System.out.println("disconnected....why?");
		state = STATE_PRESEARCH;
	}


	public void extensionConnected(WiiRemoteExtension arg0) {
		// TODO Auto-generated method stub
		
	}


	public void extensionDisconnected(WiiRemoteExtension arg0) {
		// TODO Auto-generated method stub
		
	}


	public void extensionInputReceived(WRExtensionEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	public void extensionPartiallyInserted() {
		// TODO Auto-generated method stub
		
	}


	public void extensionUnknown() {
		// TODO Auto-generated method stub
		
	}


	public void statusReported(WRStatusEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("status report--------");
		System.out.println("batt: " + arg0.getBatteryLevel());
		
		
	}

}
