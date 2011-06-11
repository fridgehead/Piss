
import java.io.IOException;

import javax.swing.JFrame;



public class Skeleton extends JFrame {

	ThreadPanel p;
	MainThread mainThread;
	
    public Skeleton() {
    	
    	mainThread = new MainThread(null);
    	
    	p = new ThreadPanel(mainThread);
    	mainThread.parent = p;
        add(p);
        setTitle("Test");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
        
        //spawn off a gamestate thread
        //start it
        //gamestate responsible for IO passing, 
        mainThread.start();
      
            
    }
    
    
    
    public static void main(String[] args) {
        new Skeleton();
    }



}

