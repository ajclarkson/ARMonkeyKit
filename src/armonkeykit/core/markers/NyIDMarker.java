package armonkeykit.core.markers;


public class NyIDMarker implements Marker {

	private String uid;

	public NyIDMarker(int id) {
		this.uid = "" + id;
	}

	public String getUniqueID() {
		return uid;
	}
}
