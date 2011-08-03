
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;


import processing.core.PApplet;
import processing.serial.Serial;

public class InputEngine extends Thread implements SerialPortEventListener{ 

	public static final int KEY_TRACK0 = 1;
	public static final int KEY_TRACK1 = 2;
	public static final int KEY_TRACK2 = 4;
	public static final int KEY_TRACK3 = 8;
	public static final int KEY_TRACK4 = 16;
	public static final int KEY_TRACK5 = 32;
	public static final int KEY_TRACK6 = 64;
	public static final int KEY_ESC = 128;
	public static final int KEY_NOMOREPISS = 256;

	int lastControlMessage = -1;


	public InputStream input;
	public OutputStream output;

	byte buffer[] = new byte[32768];
	int bufferIndex;
	int bufferLast;

	//boolean bufferUntil = false;
	int bufferSize = 1;  // how big before reset or event firing
	boolean bufferUntil;
	int bufferUntilByte;




	int keyMask = 0;
	boolean running = false;

	private ArrayList<GameThread> listeners = new ArrayList<GameThread>(5);

	Skeleton parent;
	private SerialPort port;
	String iname = "/dev/tty.usbserial-A4007S2a";

	String dname = "COM1";
	int drate = 9600;
	char dparity = 'N';
	int ddatabits = 8;
	int dstopbits = 1;

	long lastPissTime = 0;

	public InputEngine(Skeleton parent, String serial){
		iname = serial;

		try {
			Enumeration<?> portList = CommPortIdentifier.getPortIdentifiers();
			while (portList.hasMoreElements()) {
				CommPortIdentifier portId =
					(CommPortIdentifier) portList.nextElement();

				if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
					//System.out.println("found " + portId.getName());
					if (portId.getName().equals(iname)) {
						port = (SerialPort)portId.open("serial madness", 2000);
						input = port.getInputStream();
						output = port.getOutputStream();
						port.setSerialPortParams(9600, 8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
						port.addEventListener(this);
						port.notifyOnDataAvailable(true);
						//System.out.println("opening, ready to roll");
					}
				}
			}

		} catch (Exception e) {
			//exception = e;
			e.printStackTrace();
			port = null;
			input = null;
			output = null;
		}



		running = true;
	}

	synchronized public void serialEvent(SerialPortEvent serialEvent) {
		if (serialEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				while (input.available() > 0) {
					synchronized (buffer) {
						if (bufferLast == buffer.length) {
							byte temp[] = new byte[bufferLast << 1];
							System.arraycopy(buffer, 0, temp, 0, bufferLast);
							buffer = temp;
						}
						buffer[bufferLast++] = (byte) input.read();

					}
				}
			} catch (IOException e) {
				System.out.println("serialEvent");
				e.printStackTrace();
			}
		}
	}


	public int available() {
		return (bufferLast - bufferIndex);
	}

	public int read() {
		if (bufferIndex == bufferLast) return -1;

		synchronized (buffer) {
			int outgoing = buffer[bufferIndex++] & 0xff;
			if (bufferIndex == bufferLast) {  // rewind
				bufferIndex = 0;
				bufferLast = 0;
			}
			return outgoing;
		}
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

	public void run(){
		while(true){
			while(running){
				if(available() > 0){
					int i = read();
					for (int b = 0; b < 7; b++){
						int re = (i & 1 << b);
						if(re > 0) {
							if(lastControlMessage != i ){

								System.out.println(i);
								lastControlMessage = i ;
								sendEvent(i );
							}
						}

					}
					
					if(i != 0 ){
						lastPissTime = System.currentTimeMillis();
					}
				}

				if(lastPissTime + 100000 < System.currentTimeMillis()){
					sendEvent(KEY_NOMOREPISS);
				}
			}
		}
	}

	public void keyPress(int kCode){
		//System.out.println(kCode);

		switch(kCode){
		case(49):	//left
			keyMask |= KEY_TRACK0;
		lastPissTime = System.currentTimeMillis();
		break;
		case(50):	//up
			keyMask |= KEY_TRACK1;lastPissTime = System.currentTimeMillis();
		break;
		case(51):	//up
			keyMask |= KEY_TRACK2;lastPissTime = System.currentTimeMillis();
		break;
		case(52):	//up
			keyMask |= KEY_TRACK3;lastPissTime = System.currentTimeMillis();
		break;
		case(53):	//A
			keyMask |= KEY_TRACK4;lastPissTime = System.currentTimeMillis();
		break;
		case(54):	//B
			keyMask |= KEY_TRACK5;lastPissTime = System.currentTimeMillis();
		break;
		case(55):	//COIN
			keyMask |= KEY_TRACK6;lastPissTime = System.currentTimeMillis();
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
			if(g!=null){
				g.handleInputEvent(evt);
			} else {
				System.out.println("null listener");
			}
		}
	}
}

