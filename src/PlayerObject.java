import java.awt.Graphics;
import java.awt.image.BufferedImage;


public class PlayerObject extends GameObject {
	
	int direction = 0;
	int destinationX = 0;
	boolean moving = false;

	public PlayerObject(int wx, int wy, Sprite s) {
		super(wx, wy, s);
		
	}
	
	//move to worldx
	public void moveTo(int wx){
		destinationX = wx;
		moving = true;
	}
	
	public void move(int direction){
		this.direction = direction;
		if(direction > 0){
			setDirection(GameObject.DIRECTION_RIGHT);
			setAnimation(1);
		} else if(direction < 0){
			setDirection(GameObject.DIRECTION_LEFT);
			setAnimation(1);
		} else {
			setAnimation(0);
		}
	}
	
	public void draw(Graphics b, Camera c){
		super.draw(b, c);
		
	}
	
	public void think(){
		super.think();
		if(moving){
			if(destinationX == worldPosition.x){
				moving = false;
			} else {
				worldPosition.x += destinationX < worldPosition.x ? -10 : 10;
				
			}
		}
		
	}

}
