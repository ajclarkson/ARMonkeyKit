package armonkeykit.examples.singlemarker.arteapot;

import armonkeykit.examples.singlemarker.SingleMarkerExampleBase;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.shape.Teapot;
import com.jme.scene.shape.Torus;

import jp.nyatla.nyartoolkit.NyARException;

public class ARTeapotTorus extends SingleMarkerExampleBase {

	public ARTeapotTorus() throws NyARException, NyARException {
		super();
	}

	@Override
	public void callUpdates() {
	}

	@Override
	public void addARContent(Node arAffectedNode, Node rootNode) {
		display.setTitle("AR Teapot and Torus");
		
		Teapot tp = new Teapot("ShinyTeapot");
		tp.setLocalScale(10f);
		tp.setLocalTranslation(36f, 0f, 0f);
		
		// rotate our teapot so its base sits on the marker
		Quaternion q = new Quaternion();
		q = q.fromAngleAxis(-FastMath.PI/2,new Vector3f(1f,0f,0f));
		tp.setLocalRotation(q);
		
		Torus torus = new Torus("Torus", 12, 40, 1.5f, 3f);
		torus.setLocalScale(10f);
		torus.setLocalTranslation(-36f, 0f, 0f);
		
		arAffectedNode.attachChild(tp);
		arAffectedNode.attachChild(torus);
	}
	
	public static void main(String[] args) throws NyARException
	{
		ARTeapotTorus app = new ARTeapotTorus();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();
	}
}
