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
