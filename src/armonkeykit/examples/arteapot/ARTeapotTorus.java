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


package armonkeykit.examples.arteapot;

import jp.nyatla.nyartoolkit.NyARException;
import jp.nyatla.nyartoolkit.qt.sample.JmeNyARParam;
import jp.nyatla.nyartoolkit.qt.utils.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.WritableRaster;

import com.acarter.scenemonitor.SceneMonitor;
import com.jme.app.SimpleGame;
import com.jme.image.Texture;
import com.jme.input.NodeHandler;
import com.jme.light.DirectionalLight;
import com.jme.light.PointLight;
import com.jme.math.FastMath;
import com.jme.math.Matrix3f;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jme.scene.shape.Teapot;
import com.jme.scene.shape.Torus;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager;

import jp.nyatla.nyartoolkit.core.*;
import jp.nyatla.nyartoolkit.core.param.NyARPerspectiveProjectionMatrix;
import jp.nyatla.nyartoolkit.core.transmat.NyARTransMatResult;
import jp.nyatla.nyartoolkit.core.types.NyARIntSize;
import jp.nyatla.nyartoolkit.detector.NyARSingleDetectMarker;

public class ARTeapotTorus extends SimpleGame implements QtCaptureListener
{
	private static final long serialVersionUID = 6154831884117789648L;
	private static final int CAMERA_WIDTH = 640;
	private static final int CAMERA_HEIGHT = 480;
	
	private final String CARCODE_FILE = "Data/patt.hiro";
	private final String PARAM_FILE = "Data/camera_para.dat";
	private QtCameraCapture qtCameraCapture;
	private NyARSingleDetectMarker arDetector;
	private QtNyARRaster_RGB raster;
	private JmeNyARParam jmeARParameters;
	private Node arAffectedNode;
	private TextureState backgroundTextureState;
	private Texture backgroundTexture;
	private BufferedImage bufferedVideoImage;
	private final NyARTransMatResult displayTransMatResult = new NyARTransMatResult();

	public ARTeapotTorus() throws NyARException, NyARException
	{
		qtCameraCapture = new QtCameraCapture(CAMERA_WIDTH, CAMERA_HEIGHT, 30f);
		qtCameraCapture.setCaptureListener(this);

		jmeARParameters = new JmeNyARParam();
		NyARCode ar_code = new NyARCode(16, 16);
		jmeARParameters.loadARParamFromFile(PARAM_FILE);

		jmeARParameters.changeScreenSize(CAMERA_WIDTH, CAMERA_HEIGHT);
		raster = new QtNyARRaster_RGB(CAMERA_WIDTH, CAMERA_HEIGHT);
		arDetector = new NyARSingleDetectMarker(jmeARParameters, ar_code, 80.0,raster.getBufferType());
		ar_code.loadARPattFromFile(CARCODE_FILE);
	}

	/**
	 * We know this to be slow. Still need to apply comments from
	 * http://www.jmonkeyengine.com/forum/index.php?topic=12220.0
	 * 
	 */
	public void onUpdateBuffer(byte[] pixels)
	{
		try {
			raster.wrapBuffer(pixels);
		} catch (NyARException e) {
			e.printStackTrace();
		}

		NyARIntSize s=raster.getSize();
		WritableRaster wr = WritableRaster.createInterleavedRaster(DataBuffer.TYPE_BYTE, s.w,s.h, s.w * 3, 3, new int[] { 0, 1, 2 }, null);
		bufferedVideoImage = new BufferedImage(s.w, s.h, BufferedImage.TYPE_3BYTE_BGR);

		wr.setDataElements(0, 0, s.w, s.h,raster.getBuffer());
		bufferedVideoImage.setData(wr);

		if (backgroundTexture != null) {
			TextureManager.releaseTexture(backgroundTexture);
		}
		synchronized (bufferedVideoImage) {
			backgroundTexture = TextureManager.loadTexture(bufferedVideoImage,
					Texture.MinificationFilter.BilinearNearestMipMap,
					Texture.MagnificationFilter.Bilinear, true);
			backgroundTextureState.setTexture(backgroundTexture);
		}
	}

	private void startCapture()
	{
		try {
			qtCameraCapture.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void simpleInitGame() {
		display.setTitle("AR Teapot");
		display.setVSyncEnabled(true);

		cam.setLocation(new Vector3f(0, 0, 0));
		cam.update();

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
		
		arAffectedNode = new Node("AR Affected Node");
		arAffectedNode.attachChild(tp);
		arAffectedNode.attachChild(torus);


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

		startCapture();

		NyARPerspectiveProjectionMatrix m2 = jmeARParameters.getPerspectiveProjectionMatrix();
		Quad videoBG = getVideoBackground();
		videoBG.setLocalTranslation(new Vector3f(0, 0, (float) -m2.m00 * 4));
		rootNode.attachChild(videoBG);
		
		float[] ad = jmeARParameters.getCameraFrustum();
		cam.setFrustum(ad[0], ad[1], ad[2], ad[3], ad[4], ad[5]);

		SceneMonitor.getMonitor().showViewer(true);
		SceneMonitor.getMonitor().registerNode(rootNode);
	}

	/**
	 * Gets uad with texture state applied ready for texture
	 * updates from camera capture.
	 * @return
	 */
	private Quad getVideoBackground() {
		Quad videoBackgroundQuad = new Quad("Background");
		videoBackgroundQuad.updateGeometry(CAMERA_WIDTH *4 , CAMERA_HEIGHT *4);
		videoBackgroundQuad.setCastsShadows(false);
		Matrix3f m = new Matrix3f();
		m.fromAngleAxis((float) Math.toRadians(180), new Vector3f(0, 1, 0));
		videoBackgroundQuad.setLocalRotation(m);
		backgroundTextureState = display.getRenderer().createTextureState();
		backgroundTextureState.setEnabled(true);
		videoBackgroundQuad.setRenderState(backgroundTextureState);
		return videoBackgroundQuad;
	}
	
	/**
	 * Update called on a capture frame. Calls AR toolkit to manipulate
	 * the node affected by AR transforms.
	 */
	@Override
	protected void simpleUpdate() {
		if (bufferedVideoImage != null) {
			try {
				boolean detect = arDetector.detectMarkerLite(raster);

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
	
	@Override
	protected void cleanup() {}

	public static void main(String[] args) throws NyARException
	{
		ARTeapotTorus app = new ARTeapotTorus();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();
	}
	
}







