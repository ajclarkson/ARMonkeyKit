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

import jp.nyatla.nyartoolkit.NyARException;

import org.llama.jmf.ByteBufferRenderer;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;

import armonkeykit.core.app.ARMonkeyKitApp;
import armonkeykit.core.app.utils.NodeRotateTranslateListener;
import armonkeykit.core.markerprocessor.pattern.PatternMarkerProcessor;
import armonkeykit.core.markers.PatternMarker;

/**
 * ARVideoPlayer. An example program which loads in a video file, then plays it back in a player attached to a pattern marker.
 * This can then be treated like any other model.
 * 
 * @author Andrew Hatch, Adam Clarkson
 *
 */
public class ARVideoPlayer extends ARMonkeyKitApp{

	private VideoQuad videoQuad;
	
	private PatternMarkerProcessor markerProcessor;

	public ARVideoPlayer() {
		super();
	}
	
	@Override
	protected void simpleInitARSystem() {
		markerProcessor = initPatternProcessor();
		
	}
	
	@Override
	protected void addMarkers() {
		Node videoAffectedNode = new Node("Video Affected Node");
		ByteBufferRenderer.printframes = false;
		ByteBufferRenderer.useFOBSOptimization = true;
		ByteBufferRenderer.useFOBSPatch = true;
		videoQuad = new VideoQuad("VideoQuad");
		videoQuad.updateGeometry(180f, 90f);
		videoQuad.setVideoURL(ARVideoPlayer.class.getResource("net_content.mp4"));
		// for some reason, need to flip the video
		Quaternion qvq = new Quaternion();
		qvq = qvq.fromAngleAxis(FastMath.PI, new Vector3f(1f, 0f, 0f));
		videoQuad.setLocalRotation(qvq);
		videoAffectedNode.attachChild(videoQuad);
		rootNode.attachChild(videoAffectedNode);
		PatternMarker kanji = markerProcessor.createMarkerObject("kanji", 16, "ardata/patt.kanji", 80);
		markerProcessor.registerMarker(kanji);
		markerProcessor.finaliseMarkers();
		
		NodeRotateTranslateListener rtl = new NodeRotateTranslateListener();
		rtl.associate(kanji, videoAffectedNode);
		markerProcessor.registerEventListener(rtl);
	}

	@Override
	protected void callUpdates() {
		if(videoQuad != null) {
			videoQuad.update();
		}		
	}
	
	public static void main(String[] args) throws NyARException
	{
		ARVideoPlayer app = new ARVideoPlayer();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();
	}

	

}
