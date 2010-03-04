package armonkeykit.core.app.utils;

import java.util.HashMap;
import java.util.Map;

import jp.nyatla.nyartoolkit.core.transmat.NyARTransMatResult;

import com.jme.math.Matrix3f;
import com.jme.scene.Node;

import armonkeykit.core.events.AREventListener;
import armonkeykit.core.events.MarkerChangedEvent;
import armonkeykit.core.markers.Marker;

/**
 * NodeRotateTranslateListener. An AREventListener implementation which allows association of markers to nodes, and then allows for the 
 * translation of those nodes based on the marker translation.
 * 
 * @author Adam Clarkson
 *
 */
public class NodeRotateTranslateListener implements AREventListener {

	Map<String, Node> markerToNode = new HashMap<String,Node>();

	public NodeRotateTranslateListener() {}

	/**
	 * Interface Method: See AREventListener
	 */
	public void associate(Marker m, Node n) {
		markerToNode.put(m.getUniqueID(), n);
	}

	private Node getNodeForMarker(Marker marker) {
		return markerToNode.get(marker.getUniqueID());
	}

	public void markerChanged(MarkerChangedEvent event) {
		Node model = getNodeForMarker(event.getMarker());
		NyARTransMatResult transMatResult = event.getTransMatResult();
		model.setLocalRotation(new Matrix3f((float) -transMatResult.m00,
				(float) -transMatResult.m01, (float) transMatResult.m02,
				(float) -transMatResult.m10, (float) -transMatResult.m11,
				(float) transMatResult.m12, (float) -transMatResult.m20,
				(float) -transMatResult.m21, (float) transMatResult.m22));
		model.setLocalTranslation((float) -transMatResult.m03,
				(float) -transMatResult.m13, (float) -transMatResult.m23);
		model.setLocalScale(1.0f);


	}

	public void markerRemoved(Marker m) {
		getNodeForMarker(m).setLocalTranslation(0f,0f,-100000);		
	}


}
