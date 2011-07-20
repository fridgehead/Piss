import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/*
 * class for creating an image from a string of text and a fixed font sprite
 */
public class FixedFontImage {
	Sprite sprite;
	public FixedFontImage(Sprite s){
		sprite = s;
		
	}
	
	public BufferedImage getImageFromString(String str){
		String in = str.toUpperCase();
		BufferedImage ret = new BufferedImage(str.length() * 32, 32, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) ret.getGraphics();
		char[] s = in.toCharArray();
		for(int i = 0; i < str.length(); i++){
			
			int val = Character.getNumericValue(s[i]);
			int xpos = 0;
			if(val >= 10){
				xpos = (val - 10) * 16;
				BufferedImage letter = sprite.imageData.getSubimage(xpos, 0, 16, 16);
				
				g.drawImage(letter, i * 25, 0,32,32,null);
				
			} else if (val < 10 && val >= 0) {
				xpos = (val + 26) * 16;
				BufferedImage letter = sprite.imageData.getSubimage(xpos, 0, 16, 16);
				
				g.drawImage(letter, i * 25, 0,32,32,null);
				
			} else {
				
			}
			
			
		}
		
		return ret;
	}
	
	
}
