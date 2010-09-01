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

package armonkeykit.core.app.utils.eventlisteners;

import com.jme.scene.Node;

import armonkeykit.core.events.MarkerChangedEvent;
import armonkeykit.core.markers.Marker;

public interface IEventListener {
	public void associate(Marker m, Node n);
	public void markerAdded(Marker m);
	public void markerChanged(MarkerChangedEvent event);
	public void markerRemoved(Marker m);
}
