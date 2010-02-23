package armonkeykit.examples.patternmarkers;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.shape.Teapot;
import com.jme.scene.shape.Torus;

import armonkeykit.core.app.ARMonkeyPatternApp;
import armonkeykit.core.app.utils.NodeRotateTranslateListener;
import armonkeykit.core.markers.PatternMarker;

/**
 * ARTeapotTorus. A simple example program which uses two Pattern Markers to display a teapot and torus model
 * @author Adam Clarkson
 *
 */
public class ARTeapotTorus extends ARMonkeyPatternApp {

	
	public ARTeapotTorus() {
		super();
	}
	
	@Override
	protected void addMarkers() {
		Node teapotAffectedNode = new Node("Affected Teapot Node");
		Teapot tp = new Teapot("ShinyTeapot");
		tp.setLocalScale(10f);
		// rotate our teapot so its base sits on the marker
		Quaternion q = new Quaternion();
		q = q.fromAngleAxis(-FastMath.PI/2,new Vector3f(1f,0f,0f));
		tp.setLocalRotation(q);
		teapotAffectedNode.attachChild(tp);
		rootNode.attachChild(teapotAffectedNode);
		
		Node torusAffectedNode = new Node("Affected Torus Node");
		Torus torus = new Torus("Torus", 12, 40, 1.5f, 3f);
		torus.setLocalScale(10f);
		torusAffectedNode.attachChild(torus);
		rootNode.attachChild(torusAffectedNode);
	
		PatternMarker kanji = markerProcessor.createMarkerObject("kanji", 16, "ardata/patt.kanji");
		markerProcessor.registerMarker(kanji);
		
		PatternMarker hiro = markerProcessor.createMarkerObject("hiro", 16, "ardata/patt.hiro");
		markerProcessor.registerMarker(hiro);
		
		NodeRotateTranslateListener rtl = new NodeRotateTranslateListener();
		rtl.associate(kanji, teapotAffectedNode);
		rtl.associate(hiro, torusAffectedNode);
		markerProcessor.registerEventListener(rtl);
		
	}
	
	@Override
	protected void callUpdates() {}
	
	public static void main (String[] args) {
		ARTeapotTorus app = new ARTeapotTorus();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();
	}
	

}
