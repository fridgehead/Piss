
public class UpdateThread extends Thread{

	private long tickTime = 0;
	private int updateTicks = 41;

	ThreadPanel target;
	boolean isRunning = false;

	public UpdateThread(ThreadPanel p){
		target = p;

	}
	public void start(){
		super.start();
		System.out.println("Started updater..");
		isRunning = true;
		tickTime = System.currentTimeMillis();
	}


	public void run(){			
		System.out.print(".");

		while(isRunning){
			if(tickTime + updateTicks < System.currentTimeMillis()){
				target.updateState();
				target.repaint();			
			}
		}
	}
}
