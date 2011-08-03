
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;





public class TCPClient extends Thread {
	Socket socket;
	boolean ready = false;
	PrintWriter out;
	int playerId = -1;
	private BufferedReader inFromServer;
	private boolean running = false;
	MainGameThread parent;

	public  TCPClient(int playerId, MainGameThread parent){
		this.playerId = playerId;
		this.parent = parent;


	}

	public void start(){
		super.start();
		running = true;

	}

	public void disconnect(){
		try {
			socket.close();
			ready = false;

		} catch (IOException e) {
			// TODO Auto-generated catch block
		}

	}

	public void connect(String ip, int port){
		try {
			InetAddress serverAddr = InetAddress.getByName(ip);

			socket = new Socket(serverAddr, port);
			String message = "reg," + playerId;

			try {

				out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true); 
				out.println(message);
				out.println("getlevel");


				ready = true;
			} catch(Exception e) {

			}
		} catch (Exception e){


		}
		try {
			inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			System.out.println("error getting server input stream");
			e.printStackTrace();
		}
	}
	public void sendScore(HighScoreThread.ScoreItem s){
		if(ready){
			//try {
				String message = "newscore," + playerId + "," + s.score + ",london";
				out.println(message);
				System.out.println("sending score: " + message);
/*
				Base64.OutputStream out2 = new Base64.OutputStream(socket.getOutputStream());
				FileInputStream fIn = new FileInputStream(s.file);
				out2.write(fIn.read());
				out.println();

				fIn.close();*/

			
		}


	}
	
	

	public void sendMessage(String mess){
		if(ready){
			String mess2 = "P," + playerId + "," + mess;

			out.println(mess2);
		}
	}

	public void run() {
		while(true ){
			while(running){
				try {
					
					String serverSentence = inFromServer.readLine();
					if(serverSentence != null){
						processMessage(serverSentence);
					}
				} catch (Exception e){
					e.printStackTrace();

				}
			}
		}

	}

	private void processMessage(String serverSentence) {
		String[] elements = serverSentence.split(",");

		if(elements.length > 0) {
			
			if (elements[0].equals("P")){
				//read the player ID and positions
				parent.setOtherPlayerPos(Integer.parseInt(elements[1]), Integer.parseInt(elements[3]),  Integer.parseInt(elements[2]));
			} else if (elements[0].equals("level")){
				System.out.println("got level data: " + serverSentence);
				parent.setLevel(elements);
			} else if (elements[0].equals("newscore")){
				parent.newScoreFromNetwork(elements[1], elements[3], Integer.parseInt(elements[2]) );
				
			}
		} 
	}

}


