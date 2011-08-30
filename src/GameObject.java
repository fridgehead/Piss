import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;


public abstract class GameObject {
	
	public static final int DIRECTION_LEFT = 0;
	public static final int DIRECTION_RIGHT = 1;
	
	
	Sprite sprite;
	Point worldPosition;
	Point boundingBox;

	int currentAnimation;
	int currentFrame;
	long lastFrameTime;
	public boolean isActive = false;
	BufferedImage thisFrame;
	boolean flipVertical = false;
	boolean flipHorizontal = false;
	
	
	public GameObject(int wx, int wy, Sprite s){
		lastFrameTime = 0;
		currentFrame = 0;
		currentAnimation = 1;
		sprite = s;
		worldPosition = new Point(wx,wy);
	}

	public void setAnimation(int id){
		if(id == currentAnimation){ return; }
		if(id > sprite.numAnims || id < 0){
			id = 0;

		}
		currentAnimation = id;
		currentFrame = 0;
		lastFrameTime = System.currentTimeMillis();
		int x = currentFrame * sprite.size.x;
		int y = currentAnimation * sprite.size.y;
		int w = sprite.size.x;
		int h = sprite.size.y;
		thisFrame = sprite.imageData.getSubimage(x, y, w, h);
	}
	
	public void setDirection(int direction){
		if (direction == DIRECTION_LEFT){
			flipHorizontal = true;
		} else if (direction == DIRECTION_RIGHT){
			flipHorizontal = false;
		}
	}

	/*
	 * basic think only deals with animations
	 */
	public void think(){
		if(isActive){
			if(sprite.animSpeeds[currentAnimation] > 0){	//static anims dont count


				if(lastFrameTime + sprite.animSpeeds[currentAnimation] < System.currentTimeMillis()){
					lastFrameTime = System.currentTimeMillis();
					currentFrame ++;
					currentFrame %= sprite.maxFrames[currentAnimation];

					int x = currentFrame * sprite.size.x;
					int y = currentAnimation * sprite.size.y;
					int w = sprite.size.x;
					int h = sprite.size.y;
					
					thisFrame = sprite.imageData.getSubimage(x, y, w, h);
					
					
				}
			}

		}
	}
	/*
	 * Draw the sprite at worldpos
	 */
	public void draw(Graphics g, Camera cam){
		Point screenPos = cam.toScreenPosition(worldPosition);
		
		if(flipHorizontal == false){
			g.drawImage(thisFrame,screenPos.x, screenPos.y, sprite.size.x * 4, sprite.size.y * 4, null);
		} else {
			g.drawImage(thisFrame,screenPos.x +( thisFrame.getWidth() * 4), screenPos.y, thisFrame.getWidth() * -4,thisFrame.getHeight() * 4, null);
		}
	}

}
