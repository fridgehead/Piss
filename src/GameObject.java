import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;


public abstract class GameObject {
	Sprite sprite;
	Point worldPosition;
	Point boundingBox;

	int currentAnimation;
	int currentFrame;
	long lastFrameTime;
	public boolean isActive = false;
	BufferedImage thisFrame;

	public GameObject(int wx, int wy, Sprite s){
		lastFrameTime = 0;
		currentFrame = 0;
		currentAnimation = 1;
		sprite = s;
		worldPosition = new Point(wx,wy);
	}

	public void setAnimation(int id){
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
	public abstract void draw(Graphics g);

}
