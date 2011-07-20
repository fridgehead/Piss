
public class QueueObject extends GameObject {

	private int destination;
	
	
	
	public QueueObject(int wx, int wy, Sprite s) {
		super(wx, wy, s);
		destination = -1;
		
	}
	
	public void setDestination(int wx){
		this.destination = wx;
	}
	
	public void think(){
		super.think();
		
	}
	
	

}
