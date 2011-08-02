
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





public class TCPClient implements Runnable {
	Socket socket;
	boolean ready = false;
	PrintWriter out;
	int playerId = -1;
	private BufferedReader inFromServer;
	private boolean running = false;
	
	public  TCPClient(int playerId){
		this.playerId = playerId;
		


	}
	
	public void start(){
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
			try {
				String message = "score," + s.score + ",london,";
				out.print(message);
			
				Base64.OutputStream out2 = new Base64.OutputStream(socket.getOutputStream());
				FileInputStream fIn = new FileInputStream(s.file);
				out2.write(fIn.read());
				out.println();
				
				fIn.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
			
		
	}
	
	public void sendMessage(String mess){
		if(ready){
			String mess2 = "P," + playerId + "," + mess;
			
			out.println(mess2);
		}
	}

	public void run() {
		while(running ){
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

	private void processMessage(String serverSentence) {
		String[] elements = serverSentence.split(",");

		if(elements.length > 0) {
			if (elements[0].equals("P")){
				//read the player ID and positions
				System.out.println(serverSentence);
			}
		}
	}

}


