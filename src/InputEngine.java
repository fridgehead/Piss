import java.util.ArrayList;

public class InputEngine{

  public static final int KEY_LEFT = 1;
  public static final int KEY_RIGHT = 2;
  public static final int KEY_UP = 4;
  public static final int KEY_DOWN = 8;
  public static final int KEY_BUTTONA = 16;
  public static final int KEY_BUTTONB = 32;
  public static final int KEY_START = 64;
  public static final int KEY_COIN = 128;

  int keyMask = 0;

  private ArrayList<GameThread> listeners = new ArrayList(5);
  

  public InputEngine(){

  }

  public int getKeyMask(){
	  return keyMask;
	  
  }
  public void addListener(GameThread gt){
    listeners.add(gt);
  }

  public void keyPress(int kCode){
    System.out.println(kCode);
    
    switch(kCode){
    case(37):	//left
    	keyMask |= KEY_LEFT;
    	break;
    case(38):	//up
    	keyMask |= KEY_UP;
    	break;
    case(39):	//up
    	keyMask |= KEY_RIGHT;
    	break;
    case(40):	//up
    	keyMask |= KEY_DOWN;
    	break;
    case(65):	//A
    	keyMask |= KEY_BUTTONA;
    	break;
    case(83):	//B
    	keyMask |= KEY_BUTTONB;
    	break;
    case(49):	//COIN
    	keyMask |= KEY_COIN;
    	break;
    case(32):	//START
    	keyMask |= KEY_START;
    	break;
    }
    
  }

  public void keyRelease(int kCode){
	  switch(kCode){
	    case(37):	//left
	    	keyMask &= ~KEY_LEFT;
	    	break;
	    case(38):	//up
	    	keyMask &= ~KEY_UP;
	    	break;
	    case(39):	//up
	    	keyMask &= ~KEY_RIGHT;
	    	break;
	    case(40):	//up
	    	keyMask &= ~KEY_DOWN;
	    	break;
	    case(65):	//A
	    	keyMask &= ~KEY_BUTTONA;
	    	break;
	    case(83):	//B
	    	keyMask &= ~KEY_BUTTONB;
	    	break;
	    case(49):	//COIN
	    	keyMask &= ~KEY_COIN;
	    	break;
	    case(32):	//START
	    	keyMask &= ~KEY_START;
	    	break;
	    }
  }

  private void sendEvent(int evt){
    for(int i = 0; i < listeners.size(); i++){
      GameThread g = (GameThread)listeners.get(i);
      g.handleInputEvent(evt);
    }
  }
}

