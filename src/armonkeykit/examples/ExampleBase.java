/**
 * Modified from an original by nyatla (see license below)
 * for ARMonkeyKit project
 */

/* 
 * PROJECT: NyARToolkit QuickTime sample program.
 * --------------------------------------------------------------------------------
 * The MIT License
 * Copyright (c) 2008 nyatla
 * airmail(at)ebony.plala.or.jp
 * http://nyatla.jp/nyartoolkit/
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * 
 */

package armonkeykit.examples;

import jp.nyatla.nyartoolkit.NyARException;
import jp.nyatla.nyartoolkit.qt.sample.JmeNyARParam;

import armonkeykit.core.CaptureQuad;
import armonkeykit.core.SyncObject;

import com.acarter.scenemonitor.SceneMonitor;
import com.jme.app.SimpleGame;
import com.jme.input.NodeHandler;
import com.jme.light.DirectionalLight;
import com.jme.light.PointLight;
import com.jme.math.Matrix3f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Torus;

import jp.nyatla.nyartoolkit.core.*;
import jp.nyatla.nyartoolkit.core.param.NyARPerspectiveProjectionMatrix;
import jp.nyatla.nyartoolkit.core.transmat.NyARTransMatResult;
import jp.nyatla.nyartoolkit.detector.NyARSingleDetectMarker;

public abstract class ExampleBase extends SimpleGame
{
	private static final long serialVersionUID = 6154831884117789648L;
	private static final int CAMERA_WIDTH = 640;
	private static final int CAMERA_HEIGHT = 480;

	private final String CARCODE_FILE = "ardata/patt.hiro";
	private final String PARAM_FILE = "ardata/camera_para.dat";

	private NyARSingleDetectMarker arDetector;
	private JmeNyARParam jmeARParameters;
	private Node arAffectedNode;

	private final NyARTransMatResult displayTransMatResult = new NyARTransMatResult();

	private NyARCode ar_code;
	private CaptureQuad cameraBG;

	public ExampleBase() throws NyARException, NyARException
	{
		jmeARParameters = new JmeNyARParam();
		ar_code = new NyARCode(16, 16);
		jmeARParameters.loadARParamFromFile(PARAM_FILE);
		jmeARParameters.changeScreenSize(CAMERA_WIDTH, CAMERA_HEIGHT);
	}

	// needs to be cleaned up to use Quaternions, etc.
	@Override
	protected void simpleInitGame() {
		display.setTitle("Example");
		//display.setVSyncEnabled(true); // use this to limit frame rate with vertical sync

		cam.setLocation(new Vector3f(0, 0, 0));
		cam.update();

		arAffectedNode = new Node("AR Affected Node");

		addARContent(arAffectedNode, rootNode);

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
		rootNode.attachChild(arAffectedNode);

		input = new NodeHandler(new Torus(), 10, 2);
		
		NyARPerspectiveProjectionMatrix m2 = jmeARParameters.getPerspectiveProjectionMatrix();

		cameraBG = new CaptureQuad("Background", CAMERA_WIDTH, CAMERA_HEIGHT, 60f);
		cameraBG.updateGeometry(CAMERA_WIDTH *4 , CAMERA_HEIGHT *4);
		cameraBG.setCastsShadows(false);
		Matrix3f m = new Matrix3f();
		m.fromAngleAxis((float) Math.toRadians(180), new Vector3f(0, 0, 1));
		cameraBG.setLocalRotation(m);
		cameraBG.setLocalTranslation(new Vector3f(0, 0, (float) -m2.m00 * 4));

		try {
			arDetector = new NyARSingleDetectMarker(jmeARParameters, ar_code, 80.0,cameraBG.getRaster().getBufferType());
			ar_code.loadARPattFromFile(CARCODE_FILE);
		} catch (NyARException e) {
			e.printStackTrace();
		}

		rootNode.attachChild(cameraBG);
		float[] ad = jmeARParameters.getCameraFrustum();
		cam.setFrustum(ad[0], ad[1], ad[2], ad[3], ad[4], ad[5]);

		SceneMonitor.getMonitor().showViewer(true);
		SceneMonitor.getMonitor().registerNode(rootNode);
	}

	public abstract void addARContent(Node arAffectedNode2, Node rootNode);


	/**
	 * Update called on a capture frame. Calls AR toolkit to manipulate
	 * the node affected by AR transforms.
	 */
	@Override
	protected void simpleUpdate() {
		synchronized(SyncObject.getSyncObject()) {
			cameraBG.update();
			if (cameraBG.getRaster() != null) {

				try {
					boolean detect = arDetector.detectMarkerLite(cameraBG.getRaster());

					if (detect && arDetector.getConfidence() > 0.5) {
						NyARTransMatResult src = displayTransMatResult;
						arDetector.getTransmationMatrix(src);

						arAffectedNode.setLocalRotation(new Matrix3f((float) -src.m00,
								(float) -src.m01, (float) src.m02,
								(float) -src.m10, (float) -src.m11,
								(float) src.m12, (float) -src.m20,
								(float) -src.m21, (float) src.m22));
						arAffectedNode.setLocalTranslation((float) -src.m03,
								(float) -src.m13, (float) -src.m23);
						arAffectedNode.setLocalScale(1.0f);

					} else {
						arAffectedNode.setLocalTranslation(0f, 0f, -100000f);
					}

				} catch (NyARException e) {
					e.printStackTrace();
				}
			}
		}
		callUpdates();
	}

	public abstract void callUpdates();

	@Override
	protected void cleanup() {}



}







