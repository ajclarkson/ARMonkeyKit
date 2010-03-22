package armonkeykit.core.app.utils;

import com.jme.scene.Node;

import armonkeykit.core.markers.Marker;

/**
 * Stores references to two objects which the user wishes to track the distance between. 
 * @author Adam Clarkson
 *
 */
public class MarkerDistanceRelationship {
	
	private Marker source;
	private Marker target;
	private Node node;
	
	public MarkerDistanceRelationship(Marker source, Marker target, Node n){
		this.node = n;
		this.source = source;
		this.target = target;
	}
	
	public void setSource(Marker m){
		this.source = m;
	}
	
	public Marker getSource(){
		return source;
	}
	
	public void setTarget(Marker m){
		this.target = m;
	}
	
	public Marker getTarget(){
		return target;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}
	
	

}
