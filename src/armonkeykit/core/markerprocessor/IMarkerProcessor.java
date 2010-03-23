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
	 * it is within this list.
	 * @param m Marker to add to the list
	 */
	public void registerMarker(Marker m);
	
	/**
	 * Remove a marker from the system.
	 * @param m Marker to remove
	 */
	public void deregisterMarker(Marker m);
	
	public void update(INyARRgbRaster raster);
}
