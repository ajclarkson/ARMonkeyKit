package armonkeykit.core.events;

import jp.nyatla.nyartoolkit.core.transmat.NyARTransMatResult;
import armonkeykit.core.markers.Marker;

public class MarkerChangedEvent extends AREvent{
	private Marker marker;
	private NyARTransMatResult transMatResult;

	public MarkerChangedEvent(Marker m, NyARTransMatResult transMatResult) {
		this.marker = m;
		this.transMatResult = transMatResult;
	}

	public Marker getMarker() {
		return marker;
	}
	
	public NyARTransMatResult getTransMatResult() {
		return transMatResult;
	}
}
