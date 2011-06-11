import wiiremotej.WiiRemote;

public interface RemoteFinderEvent {
	public void foundRemote(WiiRemote r);
	public void gaveUp();
	
}
