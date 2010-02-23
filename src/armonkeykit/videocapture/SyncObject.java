package armonkeykit.videocapture;

// TODO: this is a quick fix to allow sync of the image
// buffer used in capture, display and AR detect

public class SyncObject {
	static Object obj = new Integer(3);
	
	public static Object getSyncObject() {
		return obj;
	}
}
