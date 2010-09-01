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

import java.util.HashMap;
import java.util.Map;

import com.jme.scene.Node;

import armonkeykit.core.events.MarkerChangedEvent;
import armonkeykit.core.markers.Marker;

//needs improved documentation, but only experimental, basically returns the name of the button which has been "pressed" (occluded)
public class OcclusionControlListener implements IEventListener {

	
	Map<String, Node> markerToNode = new HashMap<String, Node>();
	@Override
	public void associate(Marker m, Node n) {
		markerToNode.put(m.getUniqueID(), n);
		
	}
	

	@Override
	public void markerChanged(MarkerChangedEvent event) {
		
	}

	@Override
	public void markerRemoved(Marker m) {
		Node content = markerToNode.get(m.getUniqueID());
		if (content != null)
		content.setLocalScale(5.0f);
		if (m.getUniqueID().equals("three")){
			System.err.println("THREE PRESSED");
		}else{
		System.out.println("Button Press: " + m.getUniqueID());
		}
		
	}


	@Override
	public void markerAdded(Marker m) {
		Node content = markerToNode.get(m.getUniqueID());
		if (content != null)
		content.setLocalScale(1.0f);
		
	}

}
