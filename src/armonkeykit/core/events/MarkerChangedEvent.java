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
package armonkeykit.core.events;

import jp.nyatla.nyartoolkit.core.transmat.NyARTransMatResult;
import armonkeykit.core.markers.Marker;

public class MarkerChangedEvent extends AREvent{
	private Marker marker;
	private NyARTransMatResult transMatResult;

	public MarkerChangedEvent(Marker m, NyARTransMatResult transMatResult) {
		this.marker = m;
		this.transMatResult = transMatResult;
		
		this.marker.setTransMatResult(transMatResult);
	}

	public Marker getMarker() {
		return marker;
	}
	
	public NyARTransMatResult getTransMatResult() {
		return transMatResult;
	}
}
