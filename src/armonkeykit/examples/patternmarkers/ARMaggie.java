package armonkeykit.examples.patternmarkers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;


import com.jme.bounding.BoundingSphere;
import com.jme.scene.Node;
import com.jme.util.export.binary.BinaryImporter;
import com.jmex.model.converters.FormatConverter;
import com.jmex.model.converters.ObjToJme;

import armonkeykit.core.app.ARMonkeyKitApp;
import armonkeykit.core.app.utils.NodeRotateTranslateListener;
import armonkeykit.core.markerprocessor.pattern.PatternMarkerProcessor;
import armonkeykit.core.markers.PatternMarker;

/**
 * ARTeapotTorus. A simple example program which uses two Pattern Markers to
 * display a teapot and torus model
 * 
 * @author Adam Clarkson
 * 
 */
public class ARMaggie extends ARMonkeyKitApp {
	//TODO update documentation

	// marker processor to be used for this application.
	private PatternMarkerProcessor markerProcessor;
	// event listener to use with the system
	private NodeRotateTranslateListener rtl;

	public ARMaggie() {
		super();
		//showCamera = false; // enable or disable camera feed
		showSceneViewer = true; // enable or disable SceneMonitor
	}

	/**
	 * This must be called in order to state what type of marker processor you
	 * want to use. This should be initPatternProcessor for Pattern markers, or
	 * initIDMarkerProcessor for NyID Model 2 markers.
	 * 
	 * Also registers EventListeners to be used by the system.
	 */
	@Override
	protected void simpleInitARSystem() {
		markerProcessor = initPatternProcessor();
		rtl = new NodeRotateTranslateListener();
		markerProcessor.registerEventListener(rtl);
	}

	@Override
	protected void addMarkers() {
		/**
		 * This creates the marker objects and registers them to the system
		 */
		PatternMarker kanji = markerProcessor.createMarkerObject("kanji", 16,
				"ardata/patt.kanji", 80);
		markerProcessor.registerMarker(kanji);

		PatternMarker hiro = markerProcessor.createMarkerObject("hiro", 16,
				"ardata/patt.hiro", 80);
		markerProcessor.registerMarker(hiro);

		/**
		 * Create some content to attach to the markers
		 * 
		 */
		Node arAffectedNode = new Node("hiroAffectedARNode");
		rootNode.attachChild(arAffectedNode);
		URL model=this.getClass().getResource("maggie.obj");

		// Create something to convert .obj format to .jme
		FormatConverter converter=new ObjToJme();
		// Point the converter to where it will find the .mtl file from
		converter.setProperty("mtllib", model);

		// This byte array will hold my .jme file
		ByteArrayOutputStream BO=new ByteArrayOutputStream();
		try {
			// Use the format converter to convert .obj to .jme
			converter.convert(model.openStream(), BO);
			Node maggie=(Node)BinaryImporter.getInstance().load(new ByteArrayInputStream(BO.toByteArray()));
			// shrink this baby down some
			maggie.setLocalScale(.2f);			
			maggie.setModelBound(new BoundingSphere());
			maggie.updateModelBound();
			maggie.setLocalTranslation(0, 0, -30);
			// Put her on the scene graph

			arAffectedNode.attachChild(maggie);
		} catch (IOException e) {   // Just in case anything happens
			System.exit(1);
		}

		/**
		 * Use the associate method of the event listener to create a
		 * relationship between a marker object and the ARContentNode we created
		 * for that marker.
		 */
		rtl.associate(hiro, arAffectedNode);

		/**
		 * This method must be called after adding markers, to ensure that the
		 * detection list is up to date
		 */
		markerProcessor.finaliseMarkers();

	}

	@Override
	protected void callUpdates() {
	}

	public static void main(String[] args) {
		ARMaggie app = new ARMaggie();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();
	}

}
