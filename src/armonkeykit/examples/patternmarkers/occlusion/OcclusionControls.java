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

package armonkeykit.examples.patternmarkers.occlusion;

import com.jme.scene.Node;

import armonkeykit.core.app.ARMonkeyKitApp;
import armonkeykit.core.app.utils.eventlisteners.NodeRotateTranslateListener;
import armonkeykit.core.app.utils.eventlisteners.OcclusionControlListener;
import armonkeykit.core.markerprocessor.pattern.PatternMarkerProcessor;
import armonkeykit.core.markers.PatternMarker;

public class OcclusionControls extends ARMonkeyKitApp {

	PatternMarkerProcessor markerProcessor;
	NodeRotateTranslateListener rtl;
	OcclusionControlListener ocl;
	@Override
	protected void addMarkers() {
	
		PatternMarker kanji = markerProcessor.createMarkerObject("kanji", 16, "ardata/kanji", 80);
		markerProcessor.registerMarker(kanji);
		
		Node teapotAffectedNode = createTestTeapot();
		teapotAffectedNode.setLocalScale(1.0f);
		rootNode.attachChild(teapotAffectedNode);
		
		rtl.associate(kanji, teapotAffectedNode);
		
		
		PatternMarker monkey = markerProcessor.createMarkerObject("monkey", 16, "ardata/patterns/patt.monkey", 80);
		markerProcessor.registerMarker(monkey);
		
		ocl.associate(monkey,teapotAffectedNode);
		
		markerProcessor.finaliseMarkers();
		
		
	}

	@Override
	protected void callUpdates() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void configOptions() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void simpleInitARSystem() {
		markerProcessor = initPatternProcessor();
		rtl = new NodeRotateTranslateListener();
		markerProcessor.registerEventListener(rtl);
		
		ocl = new OcclusionControlListener();
		markerProcessor.registerEventListener(ocl);
	}
	
	public static void main(String[] args){
		OcclusionControls app = new OcclusionControls();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();
	}

}
