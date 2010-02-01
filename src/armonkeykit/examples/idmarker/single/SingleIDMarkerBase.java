package armonkeykit.examples.idmarker.single;

import jp.nyatla.nyartoolkit.NyARException;
import jp.nyatla.nyartoolkit.core.param.NyARPerspectiveProjectionMatrix;
import jp.nyatla.nyartoolkit.core.transmat.NyARTransMatResult;
import jp.nyatla.nyartoolkit.qt.sample.JmeNyARParam;
import armonkeykit.core.CaptureQuad;
import armonkeykit.core.CustomMarkerProcessor;

import com.jme.app.SimpleGame;
import com.jme.input.NodeHandler;
import com.jme.light.DirectionalLight;
import com.jme.light.PointLight;
import com.jme.math.Matrix3f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Torus;

public abstract class SingleIDMarkerBase extends SimpleGame{

	private static final int CAMERA_WIDTH = 640;
	private static final int CAMERA_HEIGHT = 480;
	private final String PARAM_FILE = "ardata/camera_para.dat";
	
	private CustomMarkerProcessor markerProcessor;
	private JmeNyARParam jmeARParameters;
	private CaptureQuad cameraBG;
	private Node arAffectedNode;
	
	public SingleIDMarkerBase() throws NyARException {
		jmeARParameters = new JmeNyARParam();
		jmeARParameters.loadARParamFromFile(PARAM_FILE);
		jmeARParameters.changeScreenSize(CAMERA_WIDTH, CAMERA_HEIGHT);
	}
	@Override
	protected void simpleInitGame() {
		
		cam.setLocation(new Vector3f(0, 0, 0));
		cam.update();
		
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
		
		input = new NodeHandler(new Torus(), 10, 2);
		
		NyARPerspectiveProjectionMatrix m2 = jmeARParameters.getPerspectiveProjectionMatrix();

		cameraBG = new CaptureQuad("Background", CAMERA_WIDTH, CAMERA_HEIGHT, 60f);
		cameraBG.updateGeometry(CAMERA_WIDTH *4 , CAMERA_HEIGHT *4);
		cameraBG.setCastsShadows(false);
		Matrix3f m = new Matrix3f();
		m.fromAngleAxis((float) Math.toRadians(180), new Vector3f(0, 0, 1));
		cameraBG.setLocalRotation(m);
		cameraBG.setLocalTranslation(new Vector3f(0, 0, (float) -m2.m00 * 4));
		
		rootNode.attachChild(cameraBG);
		arAffectedNode = new Node();
		addARContent(arAffectedNode, rootNode);
		
		rootNode.attachChild(arAffectedNode);
		
		try {
			markerProcessor = new CustomMarkerProcessor(jmeARParameters, 46, cameraBG.getRaster().getBufferType());
		} catch (NyARException e) {
			
			e.printStackTrace();
		}
		
		float[] ad = jmeARParameters.getCameraFrustum();
		cam.setFrustum(ad[0], ad[1], ad[2], ad[3], ad[4], ad[5]);	
		
		
	}
	
	public abstract void addARContent(Node arAffectedNode2, Node rootNode);
	
	@Override
	protected void simpleUpdate(){
		cameraBG.update();
		try {
			markerProcessor.detectMarker(cameraBG.getRaster());
			NyARTransMatResult src = markerProcessor.getTransMatResult();
			
			if (!(markerProcessor.getCurrentID() < 0) && (src !=null)){
				
				
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
		}catch (NyARException e){
			e.printStackTrace();
		}
		callUpdates();
		
	}
	
	public abstract void callUpdates();

	@Override
	protected void cleanup() {}

}
