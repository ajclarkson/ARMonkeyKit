package armonkeykit.core.markerprocessor.id;

import java.util.ArrayList;
import java.util.List;

import armonkeykit.core.events.IEventListener;
import armonkeykit.core.events.MarkerChangedEvent;
import armonkeykit.core.markerprocessor.IMarkerProcessor;
import armonkeykit.core.markers.Marker;
import armonkeykit.core.markers.NyIDMarker;

import jp.nyatla.nyartoolkit.NyARException;
import jp.nyatla.nyartoolkit.core.raster.rgb.INyARRgbRaster;
import jp.nyatla.nyartoolkit.core.transmat.NyARTransMatResult;

public class IDMarkerProcessor  implements IMarkerProcessor{

	private ArrayList<NyIDMarker> markerList = new ArrayList<NyIDMarker>();
	private List<IEventListener> listeners = new ArrayList<IEventListener>();
	private IDMarkerDetector idDetector;
	
	
	public IDMarkerProcessor(IDMarkerDetector idDetector) {
		this.idDetector = idDetector;
		
	}
	public void deregisterMarker(Marker m) {
		// TODO Auto-generated method stub
		
	}

	public void registerEventListener(IEventListener listener) {
		listeners.add(listener);
		
	}

	public void registerMarker(Marker m) {
		markerList.add((NyIDMarker)m);		
	}

	public void update(INyARRgbRaster raster){
		try {
			idDetector.detectMarker(raster);
			NyARTransMatResult src = idDetector.getTransMatResult();
			if (idDetector.getCurrentID()> 0 && src != null){
				
				String id = "" + idDetector.getCurrentID();
				for (Marker m : markerList){
					if (m.getUniqueID().equals(id)){
						for (IEventListener l : listeners){
							l.markerChanged(new MarkerChangedEvent(m, src));
						}
					}
				}
			}else {
				for (Marker m : markerList){
					for(IEventListener l : listeners) {
						l.markerRemoved(m);
					}
				}
			}
		} catch (NyARException e) {
			
			e.printStackTrace();
		}
	}
	@Override
	public void finaliseMarkers() {
		// TODO Auto-generated method stub
		
	}
	
	
	

}
