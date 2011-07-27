
import java.util.ArrayList;

public class InputEngine{

	public static final int KEY_TRACK0 = 1;
	public static final int KEY_TRACK1 = 2;
	public static final int KEY_TRACK2 = 4;
	public static final int KEY_TRACK3 = 8;
	public static final int KEY_TRACK4 = 16;
	public static final int KEY_TRACK5 = 32;
	public static final int KEY_TRACK6 = 64;
	public static final int KEY_ESC = 128;
	public static final int KEY_NOMOREPISS = 256;



	int keyMask = 0;

	private ArrayList<GameThread> listeners = new ArrayList<GameThread>(5);


	public InputEngine(){

	}

	public int getKeyMask(){
		return keyMask;

	}
	public void addListener(GameThread gt){
		listeners.add(gt);
	}

	public void removeListener(GameThread gt){
		listeners.remove(gt);
	}

	public void keyPress(int kCode){
		//System.out.println(kCode);

		switch(kCode){
		case(49):	//left
			keyMask |= KEY_TRACK0;
		break;
		case(50):	//up
			keyMask |= KEY_TRACK1;
		break;
		case(51):	//up
			keyMask |= KEY_TRACK2;
		break;
		case(52):	//up
			keyMask |= KEY_TRACK3;
		break;
		case(53):	//A
			keyMask |= KEY_TRACK4;
		break;
		case(54):	//B
			keyMask |= KEY_TRACK5;
		break;
		case(55):	//COIN
			keyMask |= KEY_TRACK6;
		break;
		case(27):
			sendEvent(KEY_ESC);
			break;
		case(32):
			sendEvent(KEY_NOMOREPISS);
			
		break;
		}


	}

	public void keyRelease(int kCode){
		switch(kCode){
		case(49):	//left
			keyMask &= ~KEY_TRACK0;
		sendEvent(KEY_TRACK0);

		break;
		case(50):	//up
			keyMask &= ~KEY_TRACK1;
		sendEvent(KEY_TRACK1);

		break;
		case(51):	//up
			keyMask &= ~KEY_TRACK2;
		sendEvent(KEY_TRACK2);

		break;
		case(52):	//up
			keyMask &= ~KEY_TRACK3;
		sendEvent(KEY_TRACK3);

		break;
		case(53):	//A
			keyMask &= ~KEY_TRACK4;
		sendEvent(KEY_TRACK4);
		break;
		case(54):	//B
			keyMask &= ~KEY_TRACK5;
		sendEvent(KEY_TRACK5);
		break;
		case(55):	//COIN
			keyMask &= ~KEY_TRACK6;
		sendEvent(KEY_TRACK6);
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

