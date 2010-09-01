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
package armonkeykit.core.markers;

import armonkeykit.core.app.utils.MatrixSmoother;
import jp.nyatla.nyartoolkit.core.transmat.NyARTransMatResult;

/**
 * Interface defining common properties of different marker types which can be used
 * with the system.
 * @author Adam Clarkson
 *
 */
public interface Marker {
	
	/**
	 * Used to obtain a string ID which can be used to identify a marker to the system or user.
	 * @return String Identifier
	 */
	public String getUniqueID();
	
	public void setTransMatResult(NyARTransMatResult transMatResult);
	public NyARTransMatResult getTransMatResult();
	public MatrixSmoother getMatrixSmoother();
	public void resetLossCounter();
	public void incrementLossCounter();
	public int getLossCounterValue();
	public void setMarkerInScene(boolean markerInScene);
	public boolean getMarkerInScene();
	/**
	 * Used to obtain the Node which is attached the marker. This node contains models which are
	 * attached to the marker and therefore need to be translated with it.
	 * 
	 * @return Node
	 */
//	public Node getModelNode();
	
	/**
	 * Set the node which is associated with this marker. Assigns content to markers.
	 * @param modelNode The node which is to be used.
	 */
//	public void setModelNode(Node modelNode);
}
