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
package armonkeykit.examples.idmarker;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.shape.Teapot;
import com.jme.scene.shape.Torus;

import armonkeykit.core.app.ARMonkeyKitApp;
import armonkeykit.core.app.utils.NodeRotateTranslateListener;
import armonkeykit.core.markers.NyIDMarker;

/**
 * ARIDTeapot - example program demonstrating the use of NyARModel2 marker
 * format. At present the processor for these markers only supports one marker
 * in the scene at any given time.
 * 
 * @author Adam Clarkson
 * 
 */
public class ARIDTeapot extends ARMonkeyKitApp {

	/**
	 * addMarkers - this overrides the abstract method in ARMonkeyKitApp. In
	 * order to specify the use of an NyIDMarker, a marker object must be
	 * created, with the relative ID given as a parameter. From then on it is
	 * the same as registering pattern markers for a basic app
	 */
	@Override
	protected void addMarkers() {

		NyIDMarker marker = new NyIDMarker(262);
		Node teapotAffectedNode = new Node("Affected Teapot Node");
		Teapot tp = new Teapot("ShinyTeapot");
		tp.setLocalScale(10f);
		// rotate our teapot so its base sits on the marker
		Quaternion q = new Quaternion();
		q = q.fromAngleAxis(-FastMath.PI / 2, new Vector3f(1f, 0f, 0f));
		tp.setLocalRotation(q);
		teapotAffectedNode.attachChild(tp);
		rootNode.attachChild(teapotAffectedNode);
		markerProcessor.registerMarker(marker);

		NyIDMarker marker2 = new NyIDMarker(304);
		Node torusAffectedNode = new Node("Affected Torus Node");
		Torus torus = new Torus("Torus", 12, 40, 1.5f, 3f);
		torus.setLocalScale(10f);
		torusAffectedNode.attachChild(torus);
		rootNode.attachChild(torusAffectedNode);
		markerProcessor.registerMarker(marker2);
		
		NodeRotateTranslateListener rtl = new NodeRotateTranslateListener();
		rtl.associate(marker, teapotAffectedNode);
		rtl.associate(marker2, torusAffectedNode);

		markerProcessor.registerEventListener(rtl);

	}

	@Override
	protected void callUpdates() {
	}

	public static void main(String[] args) {
		ARIDTeapot app = new ARIDTeapot();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();
	}

	/**
	 * It is important to ensure that the initIDProcessor method is called here,
	 * to give you the correct marker processor for your program. You also need
	 * to give the width of the marker as a parameter.
	 */
	@Override
	protected void simpleInitARSystem() {
		markerProcessor = initIDProcessor(46);

	}

}
