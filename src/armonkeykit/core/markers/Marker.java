package armonkeykit.core.markers;

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
