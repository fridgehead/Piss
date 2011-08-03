
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.swing.JFrame;

import org.yaml.snakeyaml.Yaml;



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
	int playerId = -1;
	String location = "";


	public static final int MODE_TITLE = 0;
	public static final int MODE_HIGHSCORE = 1;
	public static final int MODE_GAME = 2;

	private int state = 0;

	public static final int WINDOW_WIDTH = 800;
	public static final int WINDOW_HEIGHT = 600;
	public TCPClient tcpClient;

	public Skeleton()  {
		System.out.println("starting.....");
		//load settings
		Yaml yaml = new Yaml();			
		InputStream input;
		String portName = "";
		try {
			input = new FileInputStream("settings.yaml");


			for(Object obj : yaml.loadAll(input)){
				Sprite tempSprite = new Sprite();
				Map<String, Object> objMap = (Map<String, Object>)obj;
				playerId = ((Integer)(objMap.get("playerId"))).intValue();
				location = (String)objMap.get("location");
				portName = (String)objMap.get("serialport");
			}	
		} catch (Exception e){
			e.printStackTrace();
		}
		inputEngine = new InputEngine(this, portName);
		inputEngine.start();
		spriteBank = new SpriteBank();
		int c = spriteBank.loadSprites("test.yaml");
		System.out.println("loaded " + c + " Sprites");
		soundManager = new SoundManager(); 

		addKeyListener(this);
		
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
		
		

		
		tcpClient = new TCPClient(playerId, mainThread);
		tcpClient.connect("127.0.0.1", 4444);
		
		tcpClient.start();
		//spawn off a gamestate thread
		//start it
		//gamestate responsible for IO passing, 
		p.start();
		titleThread.start();

		
	}
	
	public void newScoreFromNetwork(String name, String location, int score, long randId){
		highScoreThread.newScoreFromNetwork(name, location, score, randId);
	}


	public void finishedWithScore(int score, BufferedImage snap){
		if(state != MODE_HIGHSCORE){
			highScoreThread.newScore(score, snap);
			p.changeThread(highScoreThread);
			state = MODE_HIGHSCORE;

		}
	}

	public void startGame(){
		if(state != MODE_GAME){
			p.changeThread(mainThread);
			state = MODE_GAME;
		}
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

	public void requestScoreUpdate() {
		System.out.println("getting scores");
		
		tcpClient.requestScores();
		
	}




}

