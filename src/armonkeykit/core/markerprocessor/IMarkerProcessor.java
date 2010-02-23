package armonkeykit.core.markerprocessor;

import armonkeykit.core.events.AREventListener;
import armonkeykit.core.markers.Marker;

/**
 * Interface determining what methods must be implemented in the underlying MarkerProcessors.
 * @author Adam Clarkson
 *
 */
public interface IMarkerProcessor {
	
	/**
	 * Registering an Event Listener for the system. 
	 */
	public void registerEventListener(AREventListener listener);
	
	/**
	 * Register a marker for use with the system. The marker will not be detected unless
	 * it is within this list.
	 * @param m Marker to add to the list
	 */
	public void registerMarker(Marker m);
	
	/**
	 * Remove a marker from the system.
	 * @param m Marker to remove
	 */
	public void deregisterMarker(Marker m);
	
	/**
	 * Creates a relationship between a marker and a node. The Node already contains a model
	 * set in the application. Here the node is registered so transformations of a marker
	 * can be applied to the related node.
	 * 
	 * @param m Marker which is to be related.
	 * @param modelNode Node which contains the content model to assign to marker m.
	 */
//	public abstract void createMarkerModelRelationship(Marker marker, Node modelNode);
	
}
