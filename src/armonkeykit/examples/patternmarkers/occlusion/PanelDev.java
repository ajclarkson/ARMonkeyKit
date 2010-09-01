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
import armonkeykit.core.app.utils.eventlisteners.OcclusionControlListener;
import armonkeykit.core.markerprocessor.pattern.PatternMarkerProcessor;
import armonkeykit.core.markers.PatternMarker;

public class PanelDev extends ARMonkeyKitApp {

	PatternMarkerProcessor markerProcessor;
	OcclusionControlListener ocl;
	@Override
	protected void addMarkers() {
		PatternMarker one = markerProcessor.createMarkerObject("one", 16, "ardata/patterns/numerals/patt.one", 80);
		markerProcessor.registerMarker(one);
		
		PatternMarker two = markerProcessor.createMarkerObject("two", 16, "ardata/patterns/numerals/patt.two", 80);
		markerProcessor.registerMarker(two);
		
		PatternMarker three = markerProcessor.createMarkerObject("three", 16, "ardata/patterns/numerals/patt.three", 80);
		markerProcessor.registerMarker(three);
		
		PatternMarker four = markerProcessor.createMarkerObject("four", 16, "ardata/patterns/numerals/patt.four", 80);
		markerProcessor.registerMarker(four);
		
		PatternMarker five = markerProcessor.createMarkerObject("five", 16, "ardata/patterns/numerals/patt.five", 80);
		markerProcessor.registerMarker(five);
		
		PatternMarker six = markerProcessor.createMarkerObject("six", 16, "ardata/patterns/numerals/patt.six", 80);
		markerProcessor.registerMarker(six);
		
		ocl.associate(one,new Node("one"));
		ocl.associate(two,new Node("two"));
		ocl.associate(three,new Node("three"));
		ocl.associate(four,new Node("four"));
		ocl.associate(five,new Node("five"));
		ocl.associate(six,new Node("six"));
	
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
		ocl = new OcclusionControlListener();
		markerProcessor.registerEventListener(ocl);
	}
	
	public static void main(String[] args){
		PanelDev app = new PanelDev();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();
	}

}
