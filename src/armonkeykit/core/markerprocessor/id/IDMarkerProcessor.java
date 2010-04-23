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
