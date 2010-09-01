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
package armonkeykit.examples.patternmarkers;

import com.jme.scene.Node;

import armonkeykit.core.app.ARMonkeyKitApp;
import armonkeykit.core.app.utils.eventlisteners.MarkerRelationshipListener;
import armonkeykit.core.app.utils.eventlisteners.NodeRotateTranslateListener;
import armonkeykit.core.markerprocessor.pattern.PatternMarkerProcessor;
import armonkeykit.core.markers.PatternMarker;

/**
 * AR Related Teapots. This example program introduces a new event listener, the MarkerRelationshipListener.
 * Here a user can specify a relationship between two markers, and the system will track the distance between them and draw
 * a line as feedback showing the tie.
 * 
 * The basic structure of the app is the same as ARTeapotTorus, so comments about the simple constructs can be found in that code
 * the comments here just concern the application of the new event listener.
 * 
 * @author Adam Clarkson
 *
 */
public class ARRelatedTeapots extends ARMonkeyKitApp {

	
	PatternMarkerProcessor markerProcessor;
	NodeRotateTranslateListener rtl;
	MarkerRelationshipListener mrl;
	
	@Override
	protected void addMarkers() {
		PatternMarker houses = markerProcessor.createMarkerObject("houses", 64, "ardata/patt.houses75", 75);
		markerProcessor.registerMarker(houses);
		
		PatternMarker leaf = markerProcessor.createMarkerObject("leaf", 64, "ardata/patt.leaf75", 75);
		markerProcessor.registerMarker(leaf);
		
		PatternMarker house = markerProcessor.createMarkerObject("house", 64, "ardata/patt.house75", 75);
		markerProcessor.registerMarker(house);
		
		Node housesAffectedTeapotNode = createTestTeapot();
		rootNode.attachChild(housesAffectedTeapotNode);
		
		Node leafTeapotAffectedNode = createTestTeapot();
		rootNode.attachChild(leafTeapotAffectedNode);

		Node houseTeapotAffectedNode = createTestTeapot();
		rootNode.attachChild(houseTeapotAffectedNode);
		
		rtl.associate(houses,housesAffectedTeapotNode);
		rtl.associate(leaf,leafTeapotAffectedNode);
		rtl.associate(house,houseTeapotAffectedNode);
		
		
		/*
		 * A new node must be created for each relationship. It is this node which will be used in order to display
		 * the visual feedback of the link (in this case, a line)
		 */
		Node housesLeafRel =  new Node("Houses>Leaf Relationship");
		rootNode.attachChild(housesLeafRel);
//		
		Node houseLeafRel = new Node("House > Leaf Relationship");
		rootNode.attachChild(houseLeafRel);
//		
		Node housesHouseRel = new Node("Houses >House Relationship");
		rootNode.attachChild(housesHouseRel);
		
		/**
		 * The distance relationship method takes two markers as parameters, along with the node which is to be associated with this
		 * relationship.
		 * 
		 * Note: you only have to specify the relationship one way, the system automatically reverses it for you. So here the system
		 * will also create a relationship for hiro > kanji
		 */
		mrl.createDistanceRelationship(houses, leaf, housesLeafRel);
		mrl.createDistanceRelationship(house, leaf, houseLeafRel);
		mrl.createDistanceRelationship(houses, house, housesHouseRel);
		markerProcessor.finaliseMarkers();
		
	}

	@Override
	protected void callUpdates() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void simpleInitARSystem() {
//		showCamera = false;
		markerProcessor = initPatternProcessor(0.3);
		rtl = new NodeRotateTranslateListener();
		mrl = new MarkerRelationshipListener();
		markerProcessor.registerEventListener(rtl);
		markerProcessor.registerEventListener(mrl);
		
	}
	
	public static void main(String[] args){
		ARRelatedTeapots app = new ARRelatedTeapots();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();
	}

	@Override
	protected void configOptions() {
		// TODO Auto-generated method stub
		
	}

}
