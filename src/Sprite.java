import java.awt.Point;
import java.awt.image.BufferedImage;


/*
 * Sprite is just a container for a list of animations, their speeds and frame lengths
 *  
 */
	
public class Sprite {

	public String name;
	public BufferedImage imageData;	//index of sprite image data in spritebank
	public int numAnims;	//number of animations
	public int[] animSpeeds;	//speed of animations
	public int[] maxFrames;
	public Point size;
	
	public Sprite(){
	
	}
	
	
}
