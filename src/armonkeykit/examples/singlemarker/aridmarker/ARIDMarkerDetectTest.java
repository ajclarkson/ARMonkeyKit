package armonkeykit.examples.singlemarker.aridmarker;

import jp.nyatla.nyartoolkit.NyARException;
import jp.nyatla.nyartoolkit.core.NyARCode;
import jp.nyatla.nyartoolkit.core.param.NyARParam;
import jp.nyatla.nyartoolkit.core.param.NyARPerspectiveProjectionMatrix;
import jp.nyatla.nyartoolkit.core.squaredetect.NyARSquare;
import jp.nyatla.nyartoolkit.core.transmat.NyARTransMatResult;
import jp.nyatla.nyartoolkit.nyidmarker.data.INyIdMarkerData;
import jp.nyatla.nyartoolkit.nyidmarker.data.NyIdMarkerDataEncoder_RawBit;
import jp.nyatla.nyartoolkit.nyidmarker.data.NyIdMarkerData_RawBit;
import jp.nyatla.nyartoolkit.processor.SingleNyIdMarkerProcesser;
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
import com.jme.scene.shape.Torus;
import com.jme.system.DisplaySystem;



/*
 * Taken directly from SingleNyIDMarker to find purpose
 */
class MarkerProcessor extends SingleNyIdMarkerProcesser
{	
	private Object _sync_object=new Object();
	public NyARTransMatResult transmat=null;
	public int current_id=-1;

	public MarkerProcessor(NyARParam i_cparam,int i_width,int i_raster_format) throws NyARException
	{
		//アプリケーションフレームワークの初期化
		super();
		initInstance(i_cparam,new NyIdMarkerDataEncoder_RawBit(),100,i_raster_format);
		return;
	}
	/**
	 * アプリケーションフレームワークのハンドラ（マーカ出現）
	 */
	protected void onEnterHandler(INyIdMarkerData i_code)
	{
		synchronized(this._sync_object){
			NyIdMarkerData_RawBit code=(NyIdMarkerData_RawBit)i_code;
			if(code.length>4){
				//4バイト以上の時はint変換しない。
				this.current_id=-1;//undefined_id
			}else{
				this.current_id=0;
				//最大4バイト繋げて１個のint値に変換
				for(int i=0;i<code.length;i++){
					//Pipe is binary OR operator
					this.current_id=(this.current_id<<8)|code.packet[i];
//					System.out.println("MARKER: " + ((current_id<<8)|code.packet[i]));
				}
				
			}
			this.transmat=null;
		}
	}
	/**
	 * アプリケーションフレームワークのハンドラ（マーカ消滅）
	 */
	protected void onLeaveHandler()
	{
		synchronized(this._sync_object){
			this.current_id=-1;
			this.transmat=null;
		}
		return;
	}
	/**
	 * アプリケーションフレームワークのハンドラ（マーカ更新）
	 */
	protected void onUpdateHandler(NyARSquare i_square, NyARTransMatResult result)
	{
		synchronized(this._sync_object){
			this.transmat=result;
		}
	}
}



public class ARIDMarkerDetectTest extends SimpleGame {

	private static final int CAMERA_WIDTH = 640;
	private static final int CAMERA_HEIGHT = 480;
	private final String PARAM_FILE = "ardata/camera_para.dat";
	
	private JmeNyARParam jmeARParameters;
	private CaptureQuad cameraBG;

	private MarkerProcessor markerProcessor;
	
	private final NyARTransMatResult displayTransMatResult = new NyARTransMatResult();
	
	public ARIDMarkerDetectTest() throws NyARException {
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
		float[] ad = jmeARParameters.getCameraFrustum();
		cam.setFrustum(ad[0], ad[1], ad[2], ad[3], ad[4], ad[5]);

		
		try {
			markerProcessor = new MarkerProcessor(jmeARParameters, 100, cameraBG.getRaster().getBufferType());
		} catch (NyARException e) {
			
			e.printStackTrace();
		}

		
	}
	
	@Override
	protected void simpleUpdate() {
		synchronized(SyncObject.getSyncObject()){
			cameraBG.update();
			try {
				markerProcessor.detectMarker(cameraBG.getRaster());
			} catch (NyARException e) {
			
				e.printStackTrace();
			}
			if(!(markerProcessor.current_id<0) || !(displayTransMatResult == null)){
			
				System.out.println("Marker" + markerProcessor.current_id);
			}
		}
//		callUpdates();
	}
	
	public static void main (String[] args) throws NyARException{
		ARIDMarkerDetectTest app = new ARIDMarkerDetectTest();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();
	}

}
