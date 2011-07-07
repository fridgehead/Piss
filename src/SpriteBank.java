import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

import javax.imageio.ImageIO;

import org.yaml.snakeyaml.*;

/*
 * stores all of the images for the sprites
 */
public class SpriteBank {
	
	public ArrayList<Sprite> spriteData;
	Yaml yaml;
	
	
	public SpriteBank(){
		spriteData = new ArrayList<Sprite>();
		yaml = new Yaml();
	}
	
	/*
	 * returns number of sprites loaded
	 */
	
	public int loadSprites(String spriteFile){
		try{
			InputStream input = new FileInputStream("yaml/" + spriteFile);
			
			for(Object obj : yaml.loadAll(input)){
				Sprite tempSprite = new Sprite();
				Map<String, Object> objMap = (Map<String, Object>)obj;
				tempSprite.name = (String)objMap.get("name");
				
				tempSprite.imageData = ImageIO.read(new File("img/" + (String)objMap.get("image")));
				int width = ((Integer)objMap.get("frameWidth")).intValue();
				int height =((Integer)objMap.get("frameHeight")).intValue();
				
				tempSprite.size = new Point(width,height);
				
				//animations
				ArrayList animList= (ArrayList)objMap.get("Animations");
				tempSprite.numAnims = animList.size();
				tempSprite.animSpeeds = new int[tempSprite.numAnims];
				tempSprite.maxFrames = new int[tempSprite.numAnims];
				int ct = 0;
				for(Object anim: animList){
					Map<String, Object> an = (Map<String, Object>)anim;
					tempSprite.animSpeeds[ct] = ((Integer)an.get("speed")).intValue();
					tempSprite.maxFrames[ct] = ((Integer)an.get("frames")).intValue();
					ct++;
				}
				
				spriteData.add(tempSprite);
			
			}
		} catch( Exception e){
			e.printStackTrace();
		}
		
		return spriteData.size();
	}
	
	public Sprite getSpriteByName(String name){
		for(Sprite s : spriteData){
			if(s.name.equals(name)){
				return s;
				
			}
		}
		return null;
	}
	

}
