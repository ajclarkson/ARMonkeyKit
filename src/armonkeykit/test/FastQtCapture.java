package armonkeykit.test;

import jp.nyatla.nyartoolkit.NyARException;

import com.jme.app.SimpleGame;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;

public class FastQtCapture extends SimpleGame {

	private CaptureQuad cq;

	public FastQtCapture() throws NyARException, NyARException {
		super();
	}

	public static void main(String[] args) {
		FastQtCapture app;
		try {
			app = new FastQtCapture();
			app.start();
		} catch (NyARException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void simpleInitGame() {
		cq = new CaptureQuad("qt capture", 640, 480, 60f);
		cq.updateGeometry(640/5f, 480/5f);
		rootNode.attachChild(cq);	
		Quaternion q = new Quaternion();
		q = q.fromAngleAxis(FastMath.PI, new Vector3f(0f, 0f, 1f));
		cq.setLocalRotation(q);
	}

	@Override
	protected void simpleUpdate() {
		if(cq != null) cq.update();
	}

}
