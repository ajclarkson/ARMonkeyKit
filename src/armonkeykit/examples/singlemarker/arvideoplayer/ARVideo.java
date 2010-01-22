package armonkeykit.examples.singlemarker.arvideoplayer;

import org.llama.jmf.ByteBufferRenderer;

import armonkeykit.core.video.VideoQuad;
import armonkeykit.examples.singlemarker.SingleMarkerExampleBase;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;


import jp.nyatla.nyartoolkit.NyARException;

public class ARVideo extends SingleMarkerExampleBase {

	private VideoQuad videoQuad;

	public ARVideo() throws NyARException, NyARException {
		super();
	}

	@Override
	public void callUpdates() {
		if(videoQuad != null) {
			videoQuad.update();
		}		
	}

	@Override
	public void addARContent(Node arAffectedNode, Node rootNode) {
		display.setTitle("AR Video");
		ByteBufferRenderer.printframes = false;
		ByteBufferRenderer.useFOBSOptimization = true;
		ByteBufferRenderer.useFOBSPatch = true;
		videoQuad = new VideoQuad("VideoQuad");
		videoQuad.updateGeometry(180f, 90f);
		videoQuad.setVideoURL(ARVideo.class.getResource("net_content.mp4"));
		// for some reason, need to flip the video
		Quaternion qvq = new Quaternion();
		qvq = qvq.fromAngleAxis(FastMath.PI, new Vector3f(1f, 0f, 0f));
		videoQuad.setLocalRotation(qvq);
		arAffectedNode.attachChild(videoQuad);
	}
	
	public static void main(String[] args) throws NyARException
	{
		ARVideo app = new ARVideo();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();
	}
}
