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
import armonkeykit.videocapture.video.VideoQuad;

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
