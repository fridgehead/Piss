
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.JFrame;



public class Skeleton extends JFrame implements KeyListener {

	ThreadPanel p;
	TitleThread titleThread;
	MainGameThread mainThread;
	HighScoreThread highScoreThread;
	InputEngine inputEngine;
	SoundManager soundManager;
	SpriteBank spriteBank;
	FixedFontImage fixedFont;
	public int credits = 0;
	public int lives = 0;
	
	
	public static final int MODE_TITLE = 0;
	public static final int MODE_HIGHSCORE = 1;
	public static final int MODE_GAME = 2;
	
	private int state = 0;
	
	public static final int WINDOW_WIDTH = 800;
	public static final int WINDOW_HEIGHT = 600;
	
	
    public Skeleton()  {
    	System.out.println("starting.....");
    	spriteBank = new SpriteBank();
        int c = spriteBank.loadSprites("test.yaml");
        System.out.println("loaded " + c + " Sprites");
    	soundManager = new SoundManager(); 
    	
        addKeyListener(this);
        inputEngine = new InputEngine();
    	
        fixedFont = new FixedFontImage(spriteBank.getSpriteByName("FixedFontSmall"));
        
    	titleThread = new TitleThread(this);
    	
    	mainThread = new MainGameThread(this);
    	highScoreThread = new HighScoreThread(this);
    	
    	
    	//mainThread = new MainGameThread(this);
    	
    	
    	
    	p = new ThreadPanel(titleThread);
    	
        add(p);
        
        
        setTitle("Test");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
        
        //spawn off a gamestate thread
        //start it
        //gamestate responsible for IO passing, 
        p.start();
        titleThread.start();
        
        
    }
    
    public void nextState(){
    	if(state == MODE_TITLE){
    		
    		p.changeThread(highScoreThread);
    		state = MODE_HIGHSCORE;
    		
    	} else if(state == MODE_HIGHSCORE){
    		
    		
    		p.changeThread(titleThread);
    		state = MODE_TITLE;
    		
    	}
    }
    
    
    
    public void quit(){
    	soundManager.stop();
    	p.stop();
    	System.exit(0);
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

