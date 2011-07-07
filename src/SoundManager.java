import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.*;
import javax.sound.*;

/** 
 *
 * @author tomwyatt
 * stores samples and is responsible for playing them
 */
public class SoundManager {
	
	Clip clip;
	AudioInputStream loader;
	ArrayList<SoundClip> queue;
	
	ArrayList<SoundClip> clips;
	
	Clip bgmLoop;
	
	
	//create some methods to load "sound sets" for each thread, can be switched to save memory
	//create some callbacks to send back "i'm ready" to the main thread.
	public SoundManager(){
		System.out.println("Starting SoundManager");
		
        // Get the clip length in microseconds and convert to milliseconds

        queue = new ArrayList<SoundClip>();
        clips = new ArrayList<SoundClip>();
        loadClips();
	}
	
	private void loadClips(){
		File f = new File("sfx/list.cfg");
		try {
			FileInputStream fIn = new FileInputStream(f);
			
			int a = fIn.read();
			String buffer = "";
			while(a != -1){
				
				if(a == 10){
					System.out.println("Loading sound: " + buffer + "...");
					String[] vals = buffer.split("=");
					if(vals.length > 0){
						String enumName = vals[0];
						String fileName = vals[1];
						SoundClip s = SoundClip.fromString(enumName);
						if(s != null){
							
							Clip b = loadClip("sfx/" + fileName);
							if(b != null){
								s.clip = b;
								clips.add(s);
								System.out.println("...ok");

							} else {
								System.out.println("...fail");
							}

						}
					}
					
					
					buffer = "";
					
				} else {
					buffer += (char)a;
					
				}
				a = fIn.read();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("sound file load fail");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private Clip loadClip(String fileName){
		AudioInputStream ain;
        try {
    		ain = AudioSystem.getAudioInputStream(new File(fileName));

            DataLine.Info info =
                new DataLine.Info(Clip.class,ain.getFormat( ));
            clip = (Clip) AudioSystem.getLine(info);
            clip.open(ain);
            return clip;
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public void setBgm(SoundClip snd){
		if(bgmLoop != null){
			bgmLoop.stop();			
		} 
		if(snd != null){
			bgmLoop = snd.clip;
			bgmLoop.loop(Clip.LOOP_CONTINUOUSLY);
		} else {
			bgmLoop.stop();
			
		}
		
	}
	
	public void playSound(SoundClip sndClip){
		queue.add(sndClip);	
		//
	
	}
	
	public void stop(){
		for (int i = 0; i < clips.size(); i++){
			SoundClip a = clips.get(i);
			a.clip.close();
			
		}
	}
	
	public void think(){
		//look in the sound queue for things to play
		for(int i = 0; i < queue.size(); i++){
			queue.get(i).clip.setFramePosition(0);
			queue.get(i).clip.start();
			
		}
		queue.clear();
		
	}

}
