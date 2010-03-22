package armonkeykit.core.markers;

import jp.nyatla.nyartoolkit.core.transmat.NyARTransMatResult;


public class NyIDMarker implements Marker {

	private String uid;
	private NyARTransMatResult transMatResult;

	public NyIDMarker(int id) {
		this.uid = "" + id;
	}

	public String getUniqueID() {
		return uid;
	}

	public void setTransMatResult(NyARTransMatResult transMatResult){
		this.transMatResult = transMatResult;
	}
	@Override
	public NyARTransMatResult getTransMatResult() {
		
		return transMatResult;
	}
}
