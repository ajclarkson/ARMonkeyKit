/*******************************************************************************
*    This file is part of ARMonkeyKit.
*
*    ARMonkeyKit is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    ARMonkeyKit is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with ARMonkeyKit.  If not, see <http://www.gnu.org/licenses/>.
******************************************************************************/
package armonkeykit.core.markerprocessor;

import jp.nyatla.nyartoolkit.core.raster.rgb.INyARRgbRaster;
import armonkeykit.core.app.utils.eventlisteners.IEventListener;
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
