package armonkeykit.core.events;

import com.jme.scene.Node;

import armonkeykit.core.markers.Marker;

public interface IEventListener {
	public void associate(Marker m, Node n);
	public void markerChanged(MarkerChangedEvent event);
	public void markerRemoved(Marker m);
}
