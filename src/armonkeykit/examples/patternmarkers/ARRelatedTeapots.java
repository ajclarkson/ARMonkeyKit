package armonkeykit.examples.patternmarkers;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.shape.Teapot;

import armonkeykit.core.app.ARMonkeyKitApp;
import armonkeykit.core.app.utils.MarkerRelationshipListener;
import armonkeykit.core.app.utils.NodeRotateTranslateListener;
import armonkeykit.core.markerprocessor.pattern.PatternMarkerProcessor;
import armonkeykit.core.markers.PatternMarker;

/**
 * AR Related Teapots. This example program introduces a new event listener, the MarkerRelationshipListener.
 * Here a user can specify a relationship between two markers, and the system will track the distance between them and draw
 * a line as feedback showing the tie.
 * 
 * The basic structure of the app is the same as ARTeapotTorus, so comments about the simple constructs can be found in that code
 * the comments here just concern the application of the new event listener.
 * 
 * @author Adam Clarkson
 *
 */
public class ARRelatedTeapots extends ARMonkeyKitApp {

	
	PatternMarkerProcessor markerProcessor;
	NodeRotateTranslateListener rtl;
	MarkerRelationshipListener mrl;
	
	@Override
	protected void addMarkers() {
		PatternMarker kanji = markerProcessor.createMarkerObject("kanji", 16, "ardata/patt.kanji");
		markerProcessor.registerMarker(kanji);
		
		PatternMarker hiro = markerProcessor.createMarkerObject("hiro", 16, "ardata/patt.hiro");
		markerProcessor.registerMarker(hiro);
		
		Node kanjiTeapotAffectedNode = new Node(" Kanji Affected Teapot Node");
		Teapot tp = new Teapot("ShinyTeapot");
		tp.setLocalScale(10f);
		// rotate our teapot so its base sits on the marker
		Quaternion q = new Quaternion();
		q = q.fromAngleAxis(-FastMath.PI/2,new Vector3f(1f,0f,0f));
		tp.setLocalRotation(q);
		
		kanjiTeapotAffectedNode.attachChild(tp);
		rootNode.attachChild(kanjiTeapotAffectedNode);
		
		Node hiroTeapotAffectedNode = new Node("Affected Teapot Node");
		Teapot htp = new Teapot("ShinyTeapot");
		htp.setLocalScale(10f);
		// rotate our teapot so its base sits on the marker
		Quaternion qt = new Quaternion();
		qt = qt.fromAngleAxis(-FastMath.PI/2,new Vector3f(1f,0f,0f));
		htp.setLocalRotation(qt);
		
		hiroTeapotAffectedNode.attachChild(htp);
		rootNode.attachChild(hiroTeapotAffectedNode);

		rtl.associate(kanji,kanjiTeapotAffectedNode);
		rtl.associate(hiro,hiroTeapotAffectedNode);
		
		/*
		 * A new node must be created for each relationship. It is this node which will be used in order to display
		 * the visual feedback of the link (in this case, a line)
		 */
		Node hiroKanjiRel =  new Node("Kanji>Hiro Relationship");
		rootNode.attachChild(hiroKanjiRel);
		
		/**
		 * The distance relationship method takes two markers as parameters, along with the node which is to be associated with this
		 * relationship.
		 * 
		 * Note: you only have to specify the relationship one way, the system automatically reverses it for you. So here the system
		 * will also create a relationship for hiro > kanji
		 */
		mrl.createDistanceRelationship(kanji, hiro, hiroKanjiRel);
		
		markerProcessor.finaliseMarkers();
		
	}

	@Override
	protected void callUpdates() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void simpleInitARSystem() {
		markerProcessor = initPatternProcessor();
		rtl = new NodeRotateTranslateListener();
		mrl = new MarkerRelationshipListener();
		markerProcessor.registerEventListener(rtl);
		markerProcessor.registerEventListener(mrl);
		
	}
	
	public static void main(String[] args){
		ARRelatedTeapots app = new ARRelatedTeapots();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();
	}

}
