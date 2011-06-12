
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.JFrame;



public class Skeleton extends JFrame implements KeyListener {

	ThreadPanel p;
	TitleThread titleThread;
	MainGameThread mainThread;
	InputEngine inputEngine;
	SoundManager soundManager;
	
    public Skeleton()  {
    	System.out.println("starting.....");
    	
    	soundManager = new SoundManager(); 
    	
        addKeyListener(this);
        inputEngine = new InputEngine();
    	
    	titleThread = new TitleThread(this);
    	mainThread = new MainGameThread(this);
    	
    	
    	
    	p = new ThreadPanel(titleThread);
    	
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
        titleThread.start();
        
    }
    
    
    
    public static void main(String[] args) {
        new Skeleton();
    }



	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}



	public void keyPressed(KeyEvent e) {
		inputEngine.keyPress(e.getKeyCode());
		//p.changeThread(mainThread);
		
	}



	public void keyReleased(KeyEvent e) {
		inputEngine.keyRelease(e.getKeyCode());
		
	}




}

