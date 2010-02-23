package armonkeykit.core.events;

import armonkeykit.core.markers.Marker;

public interface AREventListener {
	public void markerChanged(MarkerChangedEvent event);
	public void markerRemoved(Marker m);
}
