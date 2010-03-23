package armonkeykit.core.app;

import jp.nyatla.nyartoolkit.NyARException;
import jp.nyatla.nyartoolkit.core.param.NyARPerspectiveProjectionMatrix;
import jp.nyatla.nyartoolkit.qt.sample.JmeNyARParam;
import armonkeykit.core.markerprocessor.IMarkerProcessor;
import armonkeykit.core.markerprocessor.id.IDMarkerDetector;
import armonkeykit.core.markerprocessor.id.IDMarkerProcessor;
import armonkeykit.core.markerprocessor.pattern.PatternMarkerProcessor;
import armonkeykit.videocapture.CaptureQuad;
import armonkeykit.videocapture.SyncObject;

import com.acarter.scenemonitor.SceneMonitor;
import com.jme.app.SimpleGame;
import com.jme.input.MouseInput;
import com.jme.input.NodeHandler;
import com.jme.light.DirectionalLight;
import com.jme.light.PointLight;
import com.jme.math.FastMath;
import com.jme.math.Matrix3f;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Teapot;
import com.jme.scene.shape.Torus;
/**
 * ARMonkeyKitApp is the abstract base class for all applications using the ARMonkeyKit framework.
 * This class stores information regarding:
 * <ul>
 * <li>The camera which is used to detect markers
 * <li>Configuration variables as to whether the camera feed is visible or scene debugging mode is on
 * <li>General Camera Configuration data
 * <li>Default Scene and Lighting Construction
 * </ul>
 * 
 * ARMonkeyKit app provides access to a number of underlying functions from the NyARToolkit.
 * 
 * @author Adam Clarkson
 * @author Andrew Hatch
 * 
 *
 */
public abstract class ARMonkeyKitApp extends SimpleGame {

	/**
	 * The Quad which provides the background texture for showing live camera feed in application.
	 */
	protected CaptureQuad cameraBG;
	/**
	 * Tells the application whether or not to render the camera stream to background texture.
	 * Defaults to true. 
	 */
	protected boolean showCamera = true;
	
	/**
	 * Sets the status of SceneMonitor (a useful tool for debugging scene graph problems visually).
	 * Defaults to false as it is primarily a debugging tool.
	 */
	protected boolean showSceneViewer = false; // can be overridden in individual apps to show the scene monitor for debugging.

	/**
	 * NyARParameters for jMonkeyEngine from the NyARToolkit QuickTime Samples. 
	 */
	protected JmeNyARParam jmeARParameters;
	
	/**
	 * The marker processor which is used to perform detection and update tasks. 
	 */
	protected IMarkerProcessor markerProcessor;
	
	private static final int CAMERA_WIDTH = 640;
	private static final int CAMERA_HEIGHT = 480;
	private final String PARAM_FILE = "ardata/camera_para.dat";

	/**
	 * Default Constructor deals with camera configuration for jME.
	 */
	public ARMonkeyKitApp() {		
		jmeARParameters = new JmeNyARParam();
		try {
			jmeARParameters.loadARParamFromFile(PARAM_FILE);
		} catch (NyARException e) {
			e.printStackTrace();
			//TODO: think about this a bit
		}
		jmeARParameters.changeScreenSize(CAMERA_WIDTH, CAMERA_HEIGHT);
		
		
	}
	/**
	 * Initialises a new PatternMarkerProcessor using the camera configuration established in the class constructor.
	 * The processor will be initialised with all default settings.
	 * This method must be called if the application is to detect Pattern Markers.
	 * 
	 * @see PatternMarkerProcessor
	 * @return markerProcessor instance of PatternMarkerProcessor
	 */
	protected PatternMarkerProcessor initPatternProcessor(){
		
		PatternMarkerProcessor processor = new PatternMarkerProcessor(jmeARParameters, cameraBG);
		markerProcessor = processor;
		return processor;
	}
	
	/**
	 * Initialises a new PatternMarkerProcessor using the default system settings, but allowing the user to specify a confidence
	 * rating for the marker detection process.
	 * 
	 * @see PatternMarkerProcessor
	 * @param confidenceRating confidence rating which will be used. Must be a value between 0 and 1
	 * @return markerProcessor instance of PatternMarkerProcessor
	 */
	protected PatternMarkerProcessor initPatternProcessor(double confidenceRating){
		PatternMarkerProcessor processor = new PatternMarkerProcessor(jmeARParameters, cameraBG, confidenceRating);
		markerProcessor = processor;
		return processor;
	}
	
	/**
	 * Initialises a new IDMarkerProcessor using default system settings.
	 * This method must be called if the application is to make use of NyIDModel2 marker
	 * 
	 * @param markerWidth the width of the ID Marker which will be detected. 
	 * @return markerProcessor instance of IDMarkerProcessor
	 */
	protected IDMarkerProcessor initIDProcessor(int markerWidth) {
		IDMarkerProcessor processor = new IDMarkerProcessor(new IDMarkerDetector(jmeARParameters, markerWidth, cameraBG.getRaster().getBufferType()));
		markerProcessor = processor;
		return processor;
	}
	
	/**
	 * Augments the default scene graph with a basic lighting setup. 
	 */
	protected void lightSetup(){

		PointLight pl = new PointLight();
		pl.setAmbient(new ColorRGBA(0.75f, 0.75f, 0.75f, 1));
		pl.setDiffuse(new ColorRGBA(1, 1, 1, 1));
		pl.setLocation(new Vector3f(50, 0, 0));
		pl.setEnabled(true);

		DirectionalLight dl = new DirectionalLight();
		dl.setAmbient(new ColorRGBA(0.5f, 0.5f, 0.5f, 0.5f));
		pl.setDiffuse(new ColorRGBA(0.5f, 0.5f, 0.5f, 0.5f));
		dl.setEnabled(true);
		dl.setDirection(new Vector3f(0, -1, 1));

		lightState.attach(pl);
		lightState.attach(dl);
	}

	/**
	 * Creates the default camera configuration. This method specifies the texture which will accept the camera feed
	 * and attaches it to the scene graph. It also configures a basic position and orientation for the camera.
	 */
	protected void cameraSetup() {
		NyARPerspectiveProjectionMatrix m2 = jmeARParameters.getPerspectiveProjectionMatrix();

		cameraBG = new CaptureQuad("Background", CAMERA_WIDTH, CAMERA_HEIGHT, 60f);
		cameraBG.updateGeometry(CAMERA_WIDTH *4 , CAMERA_HEIGHT *4);
		cameraBG.setCastsShadows(false);
		Matrix3f m = new Matrix3f();
		m.fromAngleAxis((float) Math.toRadians(180), new Vector3f(0, 0, 1));
		cameraBG.setLocalRotation(m);
		cameraBG.setLocalTranslation(new Vector3f(0, 0, (float) -m2.m00 * 4));
		//set queue mode code - attempting to fix openGL error
		//cameraBG.setRenderQueueMode(Renderer.QUEUE_ORTHO);
		if (showCamera == true)
		{
			rootNode.attachChild(cameraBG);
		}
		
		input = new NodeHandler(new Torus(), 10, 2);//this seems to override mouse control of camera
		MouseInput.get().setCursorVisible(true);
		
		float[] ad = jmeARParameters.getCameraFrustum();
		cam.setFrustum(ad[0], ad[1], ad[2], ad[3], ad[4], ad[5]);
	}
	
	/**
	 * SimpleUpdate is a method from the jME class SimpleGame, which ARMonkeyKitApp extends, dealing with code which must be 
	 * executed every time there is a frame update. In the case of ARMonkeyKitApp the camera texture must be updated, along with
	 * all marker detection and processing.
	 */
	protected void simpleUpdate(){
		synchronized(SyncObject.getSyncObject()){
			if (showCamera == true){
				cameraBG.update();
			}
			
			markerProcessor.update(cameraBG.getRaster());
		}
		callUpdates();

	
	}
	
	/**
	 * Utility method allowing fast creation of a default teapot attached to it's own scene Node. This allows rapid prototyping
	 * for example programs with content without having to replicate the code many times.
	 * @return Node a scene node with teapot model attached
	 */
	protected Node createTestTeapot() {
		Node teapotAffectedNode = new Node("Affected Teapot Node");
		Teapot tp = new Teapot("ShinyTeapot");
		tp.setLocalScale(10f);
		// rotate our teapot so its base sits on the marker
		Quaternion q = new Quaternion();
		q = q.fromAngleAxis(-FastMath.PI/2,new Vector3f(1f,0f,0f));
		tp.setLocalRotation(q);
		
		teapotAffectedNode.attachChild(tp);
		
		return teapotAffectedNode;
	}
	/**
	 * Allows the user to specify further code which must take place on a frame by frame basis. This will happen after the code which
	 * is in simpleUpdate.
	 */
	protected abstract void callUpdates();
	
	/**
	 * This marker must be overridden by all applications as it handles the code for registering the markers which the user
	 * wants the application to detect. 
	 */
	protected abstract void addMarkers();
	
	/**
	 * Abstract method which should contain all of the code for creating the marker processors and event listeners which are needed by the
	 * system.
	 */
	protected abstract void simpleInitARSystem();
	
	/**
	 * Handles the order in which everything is created from the lights and cameras to user specified code for adding markers. 
	 * Performs the first camera update to start the system.
	 */
	protected  void simpleInitGame(){
		
		lightSetup();
		
		cameraSetup();
		simpleInitARSystem();
		cam.setLocation(new Vector3f(0, 0, 0));
		cam.update();
		
		addMarkers();
		SceneMonitor.getMonitor().showViewer(showSceneViewer);
		SceneMonitor.getMonitor().registerNode(rootNode);
	}
	

	
}
