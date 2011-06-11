import java.io.IOException;

import wiiremotej.*;
import wiiremotej.event.*;



/* find wii remotes and report back a wiiremote object */
public class RemoteFinder extends Thread{
	
	private RemoteFinderEvent listener;
	private int startSearch = 0;
	private boolean running = false;
	
	public RemoteFinder(RemoteFinderEvent list){
		this.listener = list;
	}
	
	public void findRemotes(){
		
		running = true;
	}
	
	
	public void run(){
		if(running){
			System.out.println("started remote search");
			WiiRemote rem = null;
			startSearch ++;
			try {
				while(rem == null && startSearch < 10){
					rem = WiiRemoteJ.findRemote();
				}
				if(rem != null){
					//got one, pass it back to a listener
					listener.foundRemote(rem);
					running = false;
				} else {
					listener.gaveUp();
					running = false;
				}
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
