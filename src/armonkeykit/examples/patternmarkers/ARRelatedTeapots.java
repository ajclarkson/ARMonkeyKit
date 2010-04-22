/*
 * Copyright (c) 2010, ARMonkeyKit
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'ARMonkeyKit' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package armonkeykit.examples.patternmarkers;

import com.jme.scene.Node;

import armonkeykit.core.app.ARMonkeyKitApp;
import armonkeykit.core.app.utils.MarkerRelationshipListener;
import armonkeykit.core.app.utils.NodeRotateTranslateListener;
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

}
