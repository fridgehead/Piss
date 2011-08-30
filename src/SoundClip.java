import javax.sound.sampled.Clip;



public enum SoundClip {
	LIGHTNING("lightning"),
	SONIC("sonic"),
	RAINBGM("rainbgm"),
	COININSERT("coininsert");
	
	

	private String text;
	public Clip clip;
	

	SoundClip(String text){
		this.text = text;
		
	}
	
	

	public String getText() {
		return this.text;
	}

	public static SoundClip fromString(String text) {
		if (text != null) {
			for (SoundClip b : SoundClip.values()) {
				if (text.equalsIgnoreCase(b.text)) {
					return b;
				}
			}
			return null;
		}
		return null;
	}
}
