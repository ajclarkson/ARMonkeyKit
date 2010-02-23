package armonkeykit.examples.idmarker;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.shape.Teapot;

import armonkeykit.core.app.ARMonkeyIDApp;
import armonkeykit.core.app.utils.NodeRotateTranslateListener;
import armonkeykit.core.markers.NyIDMarker;

public class ARIDTeapot extends ARMonkeyIDApp {

	@Override
	protected void addMarkers() {
	
		NyIDMarker marker = new NyIDMarker(262);
		Node teapotAffectedNode = new Node("Affected Teapot Node");
		Teapot tp = new Teapot("ShinyTeapot");
		tp.setLocalScale(10f);
		// rotate our teapot so its base sits on the marker
		Quaternion q = new Quaternion();
		q = q.fromAngleAxis(-FastMath.PI/2,new Vector3f(1f,0f,0f));
		tp.setLocalRotation(q);
		teapotAffectedNode.attachChild(tp);
		rootNode.attachChild(teapotAffectedNode);
		markerProcessor.registerMarker(marker);
		
		NodeRotateTranslateListener rtl = new NodeRotateTranslateListener();
		rtl.associate(marker, teapotAffectedNode);
		
		markerProcessor.registerEventListener(rtl);
		
	}

	@Override
	protected void callUpdates() {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args){
		ARIDTeapot app = new ARIDTeapot();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();
	}

}
