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
package armonkeykit.examples.patternmarkers.basic;

import jp.nyatla.nyartoolkit.NyARException;

import org.llama.jmf.ByteBufferRenderer;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;

import armonkeykit.core.app.ARMonkeyKitApp;
import armonkeykit.core.app.utils.eventlisteners.NodeRotateTranslateListener;
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

	@Override
	protected void configOptions() {
		// TODO Auto-generated method stub
		
	}

	

}
