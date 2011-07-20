import java.awt.Graphics;
import java.awt.image.BufferedImage;


public class QueueObject extends GameObject {

	private int destination;
	private boolean moving = false;
	
	
	
	public QueueObject(int wx, int wy, Sprite s) {
		super(wx, wy, s);
		destination = -1;
		setDirection(GameObject.DIRECTION_RIGHT);
		setAnimation(0);
		
	}
	
	public void setDestination(int wx){
		this.destination = wx;
		moving = true;
		setAnimation(1);
	}
	
	public void think(){
		super.think();
		if(worldPosition.x >= destination ){
			if( moving == true){
				moving = false;
				setAnimation(0);
			}
		} else {
			
			//moving = true;
			if(worldPosition.x < destination){
				worldPosition.x +=2;
			} else {
				worldPosition.x -=2 ;
			}
		}
		
		
	}
	public void draw(Graphics buf, Camera c){
		super.draw(buf, c);
		
		
	}
	
	

}
