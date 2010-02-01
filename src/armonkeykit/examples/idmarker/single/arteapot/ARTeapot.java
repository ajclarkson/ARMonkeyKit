package armonkeykit.examples.idmarker.single.arteapot;

import jp.nyatla.nyartoolkit.NyARException;

import com.jme.scene.Node;
import com.jme.scene.shape.Teapot;

import armonkeykit.examples.idmarker.single.SingleIDMarkerBase;

public class ARTeapot extends SingleIDMarkerBase {

	public ARTeapot() throws NyARException {
		super();
	}
	@Override
	public void addARContent(Node arAffectedNode, Node rootNode) {
		Teapot tp = new Teapot("ShinyTeapot");
		tp.setLocalScale(10f);
		tp.setLocalTranslation(36f, 0f, 0f);
		
		
		arAffectedNode.attachChild(tp);
		
	}

	@Override
	public void callUpdates() {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) throws NyARException
	{
		ARTeapot app = new ARTeapot();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();
	}

}
