package armonkeykit.core.app;

import jp.nyatla.nyartoolkit.NyARException;
import jp.nyatla.nyartoolkit.core.NyARCode;
import jp.nyatla.nyartoolkit.detector.NyARDetectMarker;

import com.jme.math.Vector3f;

import armonkeykit.core.markerprocessor.pattern.PatternMarkerProcessor;
import armonkeykit.videocapture.SyncObject;

public abstract class ARMonkeyPatternApp extends ARMonkeyKitApp {

	protected PatternMarkerProcessor markerProcessor = new PatternMarkerProcessor();
	protected NyARDetectMarker arDetector;
	
	public ARMonkeyPatternApp() {
		super();
	}
	
	
	protected abstract void addMarkers();

	protected abstract void callUpdates();
	
	@Override
	protected void simpleInitGame() {
		lightSetup();
		cameraSetup();
		
		cam.setLocation(new Vector3f(0, 0, 0));
		cam.update();
		try {
			addMarkers();
			//TODO: possibly move this to the responsibility of the processor
			NyARCode[] codes =  markerProcessor.createARCodesList();
			arDetector = new NyARDetectMarker(jmeARParameters, codes, new double[] {80.0, 80.0}, codes.length, cameraBG.getRaster().getBufferType());
		} catch (NyARException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	protected void simpleUpdate() {


		synchronized(SyncObject.getSyncObject()){
			cameraBG.update();
			markerProcessor.update(arDetector, cameraBG.getRaster());
		}
		callUpdates();

	}
	

}
