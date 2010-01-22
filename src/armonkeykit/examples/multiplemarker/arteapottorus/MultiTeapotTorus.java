package armonkeykit.examples.multiplemarker.arteapottorus;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.shape.Teapot;
import com.jme.scene.shape.Torus;

import jp.nyatla.nyartoolkit.NyARException;
import armonkeykit.examples.multiplemarker.MultipleMarkerExampleBase;

public class MultiTeapotTorus extends MultipleMarkerExampleBase {

	public MultiTeapotTorus() throws NyARException {
		super();
		
	}

	@Override
	public void callUpdates() {
	}
	
	/*
	 * Add the AR content, hardcoded at present to a list of Nodes, to be 
	 * transformed by the AR Handling.
	 */
	public void addARContent(Node[] arAffectedNodes){
	
		arAffectedNodes[0] = new Node("Affected Teapot Node");
		
		Teapot tp = new Teapot("ShinyTeapot");
		tp.setLocalScale(10f);
		
		// rotate our teapot so its base sits on the marker
		Quaternion q = new Quaternion();
		q = q.fromAngleAxis(-FastMath.PI/2,new Vector3f(1f,0f,0f));
		tp.setLocalRotation(q);
		
		arAffectedNodes[0].attachChild(tp);
		
		arAffectedNodes[1]= new Node("Affected Torus Node");
		
		Torus torus = new Torus("Torus", 12, 40, 1.5f, 3f);
		torus.setLocalScale(10f);
		
		arAffectedNodes[1].attachChild(torus);
		
	}

	
	
	public static void main(String[] args) throws NyARException{
		MultiTeapotTorus app = new MultiTeapotTorus();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();
	}
	
}
