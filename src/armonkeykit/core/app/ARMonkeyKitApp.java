/*******************************************************************************
 * Copyright (c) 2010, ARMonkeyKit
 *  All rights reserved.
 *  
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are
 *  met:
 *  
 *  Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *  
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  
 *  Neither the name of 'ARMonkeyKit' nor the names of its contributors
 *  may be used to endorse or promote products derived from this software
 *  without specific prior written permission.
 *  
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 *  TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 *  PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 *  EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 *  PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 *  PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 *  LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/
package armonkeykit.core.app;

import jp.nyatla.nyartoolkit.NyARException;
import jp.nyatla.nyartoolkit.core.param.NyARPerspectiveProjectionMatrix;
import jp.nyatla.nyartoolkit.qt.sample.JmeNyARParam;

import armonkeykit.core.markerprocessor.IMarkerProcessor;
import armonkeykit.core.markerprocessor.id.IDMarkerDetector;
import armonkeykit.core.markerprocessor.id.IDMarkerProcessor;
import armonkeykit.core.markerprocessor.pattern.PatternMarkerProcessor;
import armonkeykit.videocapture.CaptureQuad;

import com.acarter.scenemonitor.SceneMonitor;
import com.jme.app.AbstractGame;
import com.jme.app.BaseSimpleGame;
import com.jme.image.Texture;
import com.jme.input.InputHandler;

import com.jme.input.MouseInput;
import com.jme.light.DirectionalLight;
import com.jme.light.PointLight;
import com.jme.math.FastMath;
import com.jme.math.Matrix3f;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.Spatial.CullHint;
import com.jme.scene.shape.Teapot;
import com.jme.util.geom.Debugger;

/**
 * ARMonkeyKitApp is the abstract base class for all applications using the
 * ARMonkeyKit framework. This class stores information regarding:
 * <ul>
 * <li>The camera which is used to detect markers
 * <li>Configuration variables as to whether the camera feed is visible or scene
 * debugging mode is on
 * <li>General Camera Configuration data
 * <li>Default Scene and Lighting Construction
 * </ul>
 * 
 * ARMonkeyKit app provides access to a number of underlying functions from the
 * NyARToolkit.
 * 
 * @author Adam Clarkson
 * @author Andrew Hatch
 * 
 * 
 */
public abstract class ARMonkeyKitApp extends BaseSimpleGame {

	/**
	 * The Quad which provides the background texture for showing live camera
	 * feed in application.
	 */
	protected CaptureQuad cameraBG;
	/**
	 * Tells the application whether or not to render the camera stream to
	 * background texture. Defaults to true.
	 */
	protected boolean showCamera = true;

	/**
	 * Tells the application to render the camera feed as a small Heads Up Display, as a visual reference. Defaults to false.
	 */
	protected boolean showCameraFeedAsHUD = false;
	/**
	 * Sets the status of SceneMonitor (a useful tool for debugging scene graph
	 * problems visually). Defaults to false as it is primarily a debugging
	 * tool.
	 */
	protected boolean showSceneViewer = false; 

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
			// TODO: think about this a bit
		}
		jmeARParameters.changeScreenSize(CAMERA_WIDTH, CAMERA_HEIGHT);

	}

	/**
	 * Initialises a new PatternMarkerProcessor using the camera configuration
	 * established in the class constructor. The processor will be initialised
	 * with all default settings. This method must be called if the application
	 * is to detect Pattern Markers.
	 * 
	 * @see PatternMarkerProcessor
	 * @return markerProcessor instance of PatternMarkerProcessor
	 */
	protected PatternMarkerProcessor initPatternProcessor() {
		PatternMarkerProcessor processor = new PatternMarkerProcessor(
				jmeARParameters, cameraBG);
		markerProcessor = processor;
		return processor;
	}

	/**
	 * Initialises a new PatternMarkerProcessor using the default system
	 * settings, but allowing the user to specify a confidence rating for the
	 * marker detection process.
	 * 
	 * @see PatternMarkerProcessor
	 * @param confidenceRating
	 *            confidence rating which will be used. Must be a value between
	 *            0 and 1
	 * @return markerProcessor instance of PatternMarkerProcessor
	 */
	protected PatternMarkerProcessor initPatternProcessor(
			double confidenceRating) {
		PatternMarkerProcessor processor = new PatternMarkerProcessor(
				jmeARParameters, cameraBG, confidenceRating);
		markerProcessor = processor;
		return processor;
	}

	/**
	 * Initialises a new IDMarkerProcessor using default system settings. This
	 * method must be called if the application is to make use of NyIDModel2
	 * marker
	 * 
	 * @param markerWidth
	 *            the width of the ID Marker which will be detected.
	 * @return markerProcessor instance of IDMarkerProcessor
	 * @see IDMarkerProcessor
	 */
	protected IDMarkerProcessor initIDProcessor(int markerWidth) {
		IDMarkerProcessor processor = new IDMarkerProcessor(
				new IDMarkerDetector(jmeARParameters, markerWidth, cameraBG
						.getRaster().getBufferType()));
		markerProcessor = processor;
		return processor;
	}
	
	/**
	 * Augments the default scene graph with a basic lighting setup.
	 */
	protected void lightSetup() {

		lightState.detachAll();

		PointLight pl = new PointLight();
		pl.setAmbient(new ColorRGBA(0.5f, 0.5f, 1.0f, 1));
		pl.setDiffuse(new ColorRGBA(0.5f, 0.5f, 1, 1));
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
	 * Creates the default camera configuration. This method specifies the
	 * texture which will accept the camera feed and attaches it to the scene
	 * graph. It also configures a basic position and orientation for the
	 * camera.
	 */
	protected void cameraSetup() {
		NyARPerspectiveProjectionMatrix m2 = jmeARParameters
				.getPerspectiveProjectionMatrix();

		cameraBG = new CaptureQuad("Background", CAMERA_WIDTH, CAMERA_HEIGHT,
				60f);
		cameraBG.updateGeometry(CAMERA_WIDTH * 4, CAMERA_HEIGHT * 4);
		cameraBG.setCastsShadows(false);
		cameraBG.setCullHint(CullHint.Never);
		Matrix3f m = new Matrix3f();
        m.fromAngleAxis((float) Math.toRadians(180), new Vector3f(0, 0, 1));
        cameraBG.setLocalRotation(m);
		cameraBG.setLocalTranslation(new Vector3f(0, 0, (float) -m2.m00 * 4));

		if (showCamera == true) {
			rootNode.attachChild(cameraBG);
		}
		
		if (showCameraFeedAsHUD){
			cameraBG.updateGeometry(CAMERA_WIDTH / 4, CAMERA_HEIGHT / 4);
			cameraBG.setRenderQueueMode(Renderer.QUEUE_ORTHO);
			cameraBG.setLocalTranslation(new Vector3f(cameraBG.getWidth(), cameraBG.getHeight(), 0));
			cameraBG.setLightCombineMode(Spatial.LightCombineMode.Off);
			cameraBG.updateRenderState();
			
			
			//TODO convert HUD to a moveable window?
			
		}

		input = new InputHandler();
		MouseInput.get().setCursorVisible(true);

		float[] ad = jmeARParameters.getCameraFrustum();
		cam.setFrustum(ad[0], ad[1], ad[2], ad[3], ad[4], ad[5]);

		cam.update();

	}

	/**
	 * SimpleUpdate is a method from the jME class SimpleGame, which
	 * ARMonkeyKitApp extends, dealing with code which must be executed every
	 * time there is a frame update. In the case of ARMonkeyKitApp the camera
	 * texture must be updated, along with all marker detection and processing.
	 */
	protected void simpleUpdate() {
		if (showCamera == true) {
			cameraBG.update();
		}
		markerProcessor.update(cameraBG.getRaster());
		callUpdates();
	}

	/**
	 * Utility method allowing fast creation of a default teapot attached to
	 * it's own scene Node. This allows rapid prototyping for example programs
	 * with content without having to replicate the code many times.
	 * 
	 * @return Node a scene node with teapot model attached
	 */
	protected Node createTestTeapot() {
		Node teapotAffectedNode = new Node("Affected Teapot Node");
		Teapot tp = new Teapot("ShinyTeapot");
		tp.setLocalScale(10f);
		// rotate our teapot so its base sits on the marker
		Quaternion q = new Quaternion();
		q = q.fromAngleAxis(-FastMath.PI / 2, new Vector3f(1f, 0f, 0f));
		tp.setLocalRotation(q);

		teapotAffectedNode.attachChild(tp);

		return teapotAffectedNode;
	}

	/**
	 * Handles the order in which everything is created from the lights and
	 * cameras to user specified code for adding markers. Performs the first
	 * camera update to start the system.
	 */
	protected void simpleInitGame() {
		rootNode.setCullHint(CullHint.Never);
		configOptions();
		lightSetup();

		cameraSetup();
		simpleInitARSystem();
		cam.setLocation(new Vector3f(0, 0, 0));
		cam.update();

		addMarkers();
		SceneMonitor.getMonitor().showViewer(showSceneViewer);
		SceneMonitor.getMonitor().registerNode(rootNode);

	}

	/**
	 * Called every frame to update scene information.
	 * 
	 * @param interpolation
	 *            unused in this implementation
	 * @see BaseSimpleGame#update(float interpolation)
	 */
	protected final void update(float interpolation) {
		super.update(interpolation);

		if (!pause) {
			/** Call simpleUpdate in any derived classes of SimpleGame. */
			simpleUpdate();

			/** Update controllers/render states/transforms/bounds for rootNode. */
			rootNode.updateGeometricState(tpf, true);
			statNode.updateGeometricState(tpf, true);
		}

		if (showSceneViewer) {
			SceneMonitor.getMonitor().updateViewer(interpolation);
		}
	}

	/**
	 * This is called every frame in BaseGame.start(), after update()
	 * 
	 * @param interpolation
	 *            unused in this implementation
	 * @see AbstractGame#render(float interpolation)
	 */
	protected final void render(float interpolation) {
		super.render(interpolation);

		Renderer r = display.getRenderer();

		/** Draw the rootNode and all its children. */
		r.draw(rootNode);

		/** Call simpleRender() in any derived classes. */
		simpleRender();

		/** Draw the stats node to show our stat charts. */
		r.draw(statNode);
		if (showSceneViewer) {
			SceneMonitor.getMonitor().renderViewer(r);
		}
		doDebug(r);
	}

	@Override
	protected void doDebug(Renderer r) {
		super.doDebug(r);

		if (showDepth) {
			r.renderQueue();
			Debugger.drawBuffer(Texture.RenderToTextureType.Depth,
					Debugger.NORTHEAST, r);
		}
	}

	/**
	 * Allows the user to specify further code which must take place on a frame
	 * by frame basis. This will happen after the code which is in simpleUpdate.
	 */
	protected abstract void callUpdates();

	/**
	 * This marker must be overridden by all applications as it handles the code
	 * for registering the markers which the user wants the application to
	 * detect.
	 */
	protected abstract void addMarkers();

	/**
	 * Abstract method which should contain all of the code for creating the
	 * marker processors and event listeners which are needed by the system.
	 */
	protected abstract void simpleInitARSystem();
	
	/**
	 * Abstract method which, when implemented, allows the developer to control configuration options such as whether the camera
	 * feed should be shown, whether the HUD is visible etc. This is required because these methods must be called before all other 
	 * setup takes place.
	 */
	protected abstract void configOptions();

}
