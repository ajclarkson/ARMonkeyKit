package armonkeykit.core.markerprocessor;

import jp.nyatla.nyartoolkit.core.raster.rgb.INyARRgbRaster;
import armonkeykit.core.events.IEventListener;
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
	public void registerEventListener(IEventListener listener);
	
	/**
	 * Register a marker for use with the system. The marker will not be detected unless
	 * it is within this list. Marker is not guaranteed to be detected until
	 * finaliseMarkers is called.
	 * @param m Marker to add to the list
	 */
	public void registerMarker(Marker m);
	
	/**
	 * Any changes to registered markers need to be committed by calling this method.
	 * Any changes to registered markers are not guaranteed to be effective until
	 * this method is called.
	 */
	public void finaliseMarkers();
	
	/**
	 * Remove a marker from the system. The changes are not guaranteed to be made
	 * until finaliseMarkers is called.
	 * @param m Marker to remove
	 */
	public void deregisterMarker(Marker m);
	
	public void update(INyARRgbRaster raster);

}
