
public class PlayerObject extends GameObject {
	
	int direction = 0;

	public PlayerObject(int wx, int wy, Sprite s) {
		super(wx, wy, s);
		
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
	
	public void think(){
		super.think();
		this.worldPosition.x += direction;
		
	}

}
