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

import com.jme.scene.CameraNode;
import com.jme.scene.Node;

import armonkeykit.core.app.ARMonkeyKitApp;
import armonkeykit.core.app.utils.eventlisteners.NodeRotateTranslateListener;
import armonkeykit.core.markerprocessor.pattern.PatternMarkerProcessor;
import armonkeykit.core.markers.PatternMarker;

public class CamControl extends ARMonkeyKitApp {

	PatternMarkerProcessor markerProcessor;
	NodeRotateTranslateListener rtl;
	@Override
	protected void addMarkers() {
		PatternMarker kanji = markerProcessor.createMarkerObject("kanji", 16, "ardata/patt.kanji", 80);
		markerProcessor.registerMarker(kanji);
		
		PatternMarker hiro = markerProcessor.createMarkerObject("hiro", 16, "ardata/patt.hiro", 80);
		markerProcessor.registerMarker(hiro);
		
		Node teapot = createTestTeapot();
		rtl.associate(hiro,teapot);
		rootNode.attachChild(teapot);
		
		CameraNode cameraNode = new CameraNode("cam", cam);
		rtl.associate(kanji, cameraNode);
		rootNode.attachChild(cameraNode);
		
//		cam.setDirection
		
		markerProcessor.finaliseMarkers();
	}

	@Override
	protected void callUpdates() {
		
	}

	@Override
	protected void simpleInitARSystem() {
		markerProcessor = initPatternProcessor();
		rtl = new NodeRotateTranslateListener();
		markerProcessor.registerEventListener(rtl);
		
	}

	public static void main(String[] args){
		CamControl app = new CamControl();
		app.start();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
	}

	@Override
	protected void configOptions() {
		showCameraFeedAsHUD = true;
	}
}
