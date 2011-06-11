import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

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
	
	
	
	//create some methods to load "sound sets" for each thread, can be switched to save memory
	//create some callbacks to send back "i'm ready" to the main thread.
	public SoundManager(){
		AudioInputStream ain;
        try {
    		ain = AudioSystem.getAudioInputStream(new File("sfx/sonicshootout.wav"));

            DataLine.Info info =
                new DataLine.Info(Clip.class,ain.getFormat( ));
            clip = (Clip) AudioSystem.getLine(info);
            clip.open(ain);
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        // Get the clip length in microseconds and convert to milliseconds
        int audioLength = (int)(clip.getMicrosecondLength( )/1000);

	
	}
	
	public void playSound(){
		clip.start();
	
	}

}
