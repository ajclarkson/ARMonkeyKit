package armonkeykit.core.app;

import armonkeykit.core.markerprocessor.id.IDMarkerDetector;
import armonkeykit.core.markerprocessor.id.IDMarkerProcessor;
import armonkeykit.videocapture.SyncObject;

import com.jme.input.MouseInput;
import com.jme.math.Vector3f;

public abstract class ARMonkeyIDApp extends ARMonkeyKitApp{

	protected IDMarkerProcessor markerProcessor;
	private IDMarkerDetector markerDetector;
	
	public ARMonkeyIDApp() {
		super();
		
	}
	

	@Override
	protected void simpleInitGame() {
		lightSetup();
		cameraSetup();
		MouseInput.get().setCursorVisible(true);
		
		cam.setLocation(new Vector3f(0, 0, 0));
		cam.update();
		
		
		markerDetector = new IDMarkerDetector(jmeARParameters, 46, cameraBG.getRaster().getBufferType());
		markerProcessor = new IDMarkerProcessor(markerDetector);
		addMarkers();
	}
	
	@Override
	protected void simpleUpdate() {


		synchronized(SyncObject.getSyncObject()){
			cameraBG.update();
//			markerProcessor.update(arDetector, cameraBG.getRaster());
			markerProcessor.update(cameraBG.getRaster());
		}
		callUpdates();

	}
	
	protected abstract void addMarkers();
	
	protected abstract void callUpdates();
	
}
