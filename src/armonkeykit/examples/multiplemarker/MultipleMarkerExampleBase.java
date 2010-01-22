package armonkeykit.examples.multiplemarker;

import jp.nyatla.nyartoolkit.NyARException;
import jp.nyatla.nyartoolkit.core.NyARCode;
import jp.nyatla.nyartoolkit.core.param.NyARPerspectiveProjectionMatrix;
import jp.nyatla.nyartoolkit.core.transmat.NyARTransMatResult;
import jp.nyatla.nyartoolkit.detector.NyARDetectMarker;
import jp.nyatla.nyartoolkit.qt.sample.JmeNyARParam;

import armonkeykit.core.CaptureQuad;
import armonkeykit.core.SyncObject;

import com.jme.app.SimpleGame;
import com.jme.input.NodeHandler;
import com.jme.light.DirectionalLight;
import com.jme.light.PointLight;
import com.jme.math.Matrix3f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Torus;

public abstract class MultipleMarkerExampleBase extends SimpleGame {

	private static final int CAMERA_WIDTH = 640;
	private static final int CAMERA_HEIGHT = 480;

	private final String CARCODE_FILE = "ardata/patt.kanji";
	private final String CARCODE_FILE2 = "ardata/patt.hiro";
	
	private final String PARAM_FILE = "ardata/camera_para.dat";
	
	private NyARDetectMarker arDetector;
	private JmeNyARParam jmeARParameters;
	private Node[] arAffectedNodes;
	
	private final NyARTransMatResult displayTransMatResult = new NyARTransMatResult();
	

	private CaptureQuad cameraBG;
	
	private NyARCode[] arCodes = new NyARCode[2];
	
	public MultipleMarkerExampleBase() throws NyARException {
		jmeARParameters = new JmeNyARParam();
		jmeARParameters.loadARParamFromFile(PARAM_FILE);
		jmeARParameters.changeScreenSize(CAMERA_WIDTH, CAMERA_HEIGHT);
	}
	
	@Override
	protected void simpleInitGame() {
		display.setTitle("Example");
		//display.setVSyncEnabled(true); // use this to limit frame rate with vertical sync

		cam.setLocation(new Vector3f(0, 0, 0));
		cam.update();

		arAffectedNodes = new Node[2];
		
		addARContent(arAffectedNodes);

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

		for (int i=0; i < arAffectedNodes.length; i++){
			rootNode.attachChild(arAffectedNodes[i]);
		}

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
			double[] width = new double[] {80.0, 80.0};
			
			arCodes[0] = new NyARCode(16,16);
			arCodes[0].loadARPattFromFile(CARCODE_FILE);
			arCodes[1] = new NyARCode(16,16);
			arCodes[1].loadARPattFromFile(CARCODE_FILE2);
			
			arDetector = new NyARDetectMarker(jmeARParameters, arCodes, width, 2,cameraBG.getRaster().getBufferType());
		} catch (NyARException e) {
			
			e.printStackTrace();
		}

		rootNode.attachChild(cameraBG);
		float[] ad = jmeARParameters.getCameraFrustum();
		cam.setFrustum(ad[0], ad[1], ad[2], ad[3], ad[4], ad[5]);

	}
	
	public abstract void addARContent(Node[] arAffectedNodes);
	
	@Override
	protected void simpleUpdate() {
		
		int foundMarkers =0 ;
		synchronized(SyncObject.getSyncObject()){
			cameraBG.update();
			
			 // Need to check to make sure that this is the most efficient way of tracking two hard coded markers
				try {
					foundMarkers = arDetector.detectMarkerLite(cameraBG.getRaster(), 200);
					
					if (foundMarkers > 0){
						
						for (int i =0; i< foundMarkers; i++){
							
							
							if (arDetector.getConfidence(i) > 0.5){
								
								NyARTransMatResult src = displayTransMatResult;
								arDetector.getTransmationMatrix(i,src);
								
								arAffectedNodes[arDetector.getARCodeIndex(i)].setLocalRotation(new Matrix3f((float) -src.m00,
									(float) -src.m01, (float) src.m02,
									(float) -src.m10, (float) -src.m11,
									(float) src.m12, (float) -src.m20,
									(float) -src.m21, (float) src.m22));
								arAffectedNodes[arDetector.getARCodeIndex(i)].setLocalTranslation((float) -src.m03,
									(float) -src.m13, (float) -src.m23);
								arAffectedNodes[arDetector.getARCodeIndex(i)].setLocalScale(1.0f);
							}else{
								
								arAffectedNodes[arDetector.getARCodeIndex(i)].setLocalTranslation(0f,0f,-100000f);
								
							}
						}
						
					}else {
						for (int i = 0 ; i < arCodes.length; i++){
							arAffectedNodes[i].setLocalTranslation(0f,0f,-100000);
						}
					}
					
					
				} catch (NyARException e) {
				
					e.printStackTrace();
				}
			
			
		}
		
		
	}
	
	public abstract void callUpdates();

	@Override
	protected void cleanup() {}
	
	

}
