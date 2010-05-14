/*******************************************************************************
 * Copyright (c) 2010, ARMonkeyKit
 *  All rights reserved.
 *  
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are
 *  met:
 *  
 *  Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *  
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  
 *  Neither the name of 'ARMonkeyKit' nor the names of its contributors
 *  may be used to endorse or promote products derived from this software
 *  without specific prior written permission.
 *  
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 *  TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 *  PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 *  EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 *  PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 *  PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 *  LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
