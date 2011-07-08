
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.JFrame;



public class Skeleton extends JFrame implements KeyListener {

	ThreadPanel p;
	TitleThread titleThread;
	MainGameThread mainThread;
	PostTitleThread postTitleThread;
	InputEngine inputEngine;
	SoundManager soundManager;
	SpriteBank spriteBank;
	public int credits = 0;
	public int lives = 0;
	
	
	public static final int MODE_ATTRACT = 0;
	public static final int MODE_AFTERTITLE = 1;
	public static final int MODE_LEVEL0 = 2;
	
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
    	
    	titleThread = new TitleThread(this);
    	mainThread = new MainGameThread(this);
    	postTitleThread = new PostTitleThread(this);
    	
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
    	if(state == MODE_ATTRACT){
    		System.out.println("setting to post title");
    		p.changeThread(postTitleThread);
    		state = MODE_AFTERTITLE;
    	} else if (state == MODE_AFTERTITLE){
    		System.out.println("setting to level 0");
    		p.changeThread(mainThread);
    		state = MODE_LEVEL0;
    		
    	}
    }
    
    public void insertCoin(){
    	credits++;
    	lives = credits * 3;
    	System.out.println("Coins: " + credits);
    	
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

