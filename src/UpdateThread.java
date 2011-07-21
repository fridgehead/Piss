
public class UpdateThread extends Thread{

	private long tickTime = 0;
	private long updateTicks = 21;

	ThreadPanel target;
	private boolean isRunning = false;
	private Object lock = new Object();

	public UpdateThread(ThreadPanel p){
		target = p;
	}
	
	public void start(){
		isRunning = true;

		super.start();
		System.out.println("Started updater..");
		tickTime = System.currentTimeMillis();
	}

	public void end(){
		isRunning = false;
	}

	public void run(){			
		while(isRunning){
			//System.out.println("x");
			if(tickTime + updateTicks < System.currentTimeMillis()){
				

				//synchronized(lock){
					target.updateState();
					target.repaint();
				//}
				tickTime = System.currentTimeMillis();
				

			}
		}
	}

}
