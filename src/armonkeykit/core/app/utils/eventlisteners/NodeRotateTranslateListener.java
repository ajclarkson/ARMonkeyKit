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

import jp.nyatla.nyartoolkit.core.transmat.NyARTransMatResult;

import com.jme.math.Matrix3f;
import com.jme.math.Matrix4f;
import com.jme.scene.Node;

import armonkeykit.core.app.utils.MatrixSmoother;
import armonkeykit.core.events.MarkerChangedEvent;
import armonkeykit.core.markers.Marker;

/**
 * NodeRotateTranslateListener. An AREventListener implementation which allows
 * association of markers to nodes, and then allows for the translation of those
 * nodes based on the marker translation.
 * 
 * @author Adam Clarkson
 * 
 */
public class NodeRotateTranslateListener implements IEventListener {

	Map<String, Node> markerToNode = new HashMap<String, Node>();
	boolean matrixSmoothing = false;
	
	public NodeRotateTranslateListener() {
		
	}
	
	public NodeRotateTranslateListener(boolean matrixSmoothing){
		this.matrixSmoothing = matrixSmoothing;
	}
	
	

	
	public void associate(Marker m, Node n) {
		markerToNode.put(m.getUniqueID(), n);
	}

	private Node getNodeForMarker(Marker marker) {
		return markerToNode.get(marker.getUniqueID());
	}

	public void markerChanged(MarkerChangedEvent event) {
		Node model = getNodeForMarker(event.getMarker());
		if(model == null) return;
		
		NyARTransMatResult transMatResult = event.getTransMatResult();
		
		
		if (matrixSmoothing){
			MatrixSmoother matSmooth = event.getMarker().getMatrixSmoother();
			
			matSmooth.add(transMatResult);
			Matrix4f averageMatrix = matSmooth.getAverageMatrix();
			
			model.setLocalRotation(new Matrix3f((float) averageMatrix.m00, (float) averageMatrix.m01,(float) averageMatrix.m02,
					(float) averageMatrix.m10, (float) averageMatrix.m11, (float) averageMatrix.m12, (float) averageMatrix.m20,
					(float) averageMatrix.m21, (float) averageMatrix.m22));
			//TODO sort out the translation problem here using average matrix
			
			model.setLocalTranslation((float) averageMatrix.m03/10, (float) averageMatrix.m13/10, (float) averageMatrix.m23/10);
			
			System.out.println("TRANS: [" + -transMatResult.m03 + ", " + -transMatResult.m13 + ", " + -transMatResult.m23 + "], AVG: [" +
						averageMatrix.get(0,3)/10 + ", " + averageMatrix.get(1,3)/10 + ", " + averageMatrix.get(2,3)/10 + "]");
		}else{
		
			model.setLocalRotation(new Matrix3f((float) -transMatResult.m00,
				(float) -transMatResult.m01, (float) transMatResult.m02,
				(float) -transMatResult.m10, (float) -transMatResult.m11,
				(float) transMatResult.m12, (float) -transMatResult.m20,
				(float) -transMatResult.m21, (float) transMatResult.m22));
		
			model.setLocalTranslation((float) -transMatResult.m03,
				(float) -transMatResult.m13, (float) -transMatResult.m23);
		
		}
	}

	public void markerRemoved(Marker m) {
		Node n = getNodeForMarker(m);
		if(n != null) n.setLocalTranslation(0f, 0f, -100000);
	}


	@Override
	public void markerAdded(Marker m) {
		
	}

}
