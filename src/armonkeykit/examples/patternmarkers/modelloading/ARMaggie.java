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
package armonkeykit.examples.patternmarkers.modelloading;

import com.jme.bounding.BoundingSphere;
import com.jme.scene.CameraNode;
import com.jme.scene.Node;

import armonkeykit.core.app.ARMonkeyKitApp;
import armonkeykit.core.app.utils.ObjectLoader;
import armonkeykit.core.app.utils.Rotate;
import armonkeykit.core.app.utils.eventlisteners.NodeRotateTranslateListener;
import armonkeykit.core.markerprocessor.pattern.PatternMarkerProcessor;
import armonkeykit.core.markers.PatternMarker;

/**
 * ARTeapotTorus. A simple example program which uses two Pattern Markers to
 * display a teapot and torus model
 * 
 * @author Adam Clarkson
 * 
 */
public class ARMaggie extends ARMonkeyKitApp {
	// TODO update documentation

	// marker processor to be used for this application.
	private PatternMarkerProcessor markerProcessor;
	// event listener to use with the system
	private NodeRotateTranslateListener rtl;
	
	PatternMarker kanji;

	public ARMaggie() {
	
		
		
	}

	/**
	 * This must be called in order to state what type of marker processor you
	 * want to use. This should be initPatternProcessor for Pattern markers, or
	 * initIDMarkerProcessor for NyID Model 2 markers.
	 * 
	 * Also registers EventListeners to be used by the system.
	 */
	@Override
	protected void simpleInitARSystem() {
		markerProcessor = initPatternProcessor();
		rtl = new NodeRotateTranslateListener();
		markerProcessor.registerEventListener(rtl);
		
		
		
	}
	@Override
	protected void configOptions() {
		showSceneViewer = true; // enable or disable SceneMonitor
		showCameraFeedAsHUD = true;
	}

	@Override
	protected void addMarkers() {
		/**
		 * This creates the marker objects and registers them to the system
		 */
		PatternMarker kanji = markerProcessor.createMarkerObject("kanji", 16,
				"ardata/patt.kanji", 80);
		markerProcessor.registerMarker(kanji);

		PatternMarker hiro = markerProcessor.createMarkerObject("hiro", 16,
				"ardata/patt.hiro", 80);
		markerProcessor.registerMarker(hiro);

		/**
		 * Create some content to attach to the markers
		 * 
		 */
		Node arAffectedNode = new Node("hiroAffectedARNode");
		rootNode.attachChild(arAffectedNode);

		/**
		 * Make use of the ObjectLoader to load Maggie from an obj file.
		 * 
		 */
		Node maggie = ObjectLoader.loadObjectFromFile("maggie", this.getClass()
				.getResource("maggie.obj"));
		maggie.setLocalScale(.2f);
		maggie.setModelBound(new BoundingSphere());
		maggie.updateModelBound();
		maggie.setLocalTranslation(0, 0, -30);
		maggie.setLocalRotation(Rotate.PITCH270);

		arAffectedNode.attachChild(maggie);

		/**
		 * Use the associate method of the event listener to create a
		 * relationship between a marker object and the ARContentNode we created
		 * for that marker.
		 */
		rtl.associate(hiro, arAffectedNode);
		CameraNode camNode = new CameraNode("cam", cam);
		rtl.associate(kanji, camNode);

		/**
		 * This method must be called after adding markers, to ensure that the
		 * detection list is up to date
		 */
		markerProcessor.finaliseMarkers();

	}

	@Override
	protected void callUpdates() {
		
	}

	public static void main(String[] args) {
		ARMaggie app = new ARMaggie();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();
	}

}
