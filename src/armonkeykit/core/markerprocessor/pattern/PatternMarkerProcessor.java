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
package armonkeykit.core.markerprocessor.pattern;

import java.util.ArrayList;
import java.util.List;

import com.sun.media.Log;

import armonkeykit.core.events.IEventListener;
import armonkeykit.core.events.MarkerChangedEvent;
import armonkeykit.core.markerprocessor.IMarkerProcessor;
import armonkeykit.core.markers.Marker;
import armonkeykit.core.markers.PatternMarker;
import armonkeykit.videocapture.CaptureQuad;

import jp.nyatla.nyartoolkit.NyARException;
import jp.nyatla.nyartoolkit.core.NyARCode;
import jp.nyatla.nyartoolkit.core.raster.rgb.INyARRgbRaster;
import jp.nyatla.nyartoolkit.core.transmat.NyARTransMatResult;
import jp.nyatla.nyartoolkit.detector.NyARDetectMarker;
import jp.nyatla.nyartoolkit.qt.sample.JmeNyARParam;

/**
 * An implementation of the Marker Processor Interface. This implementation deals with the processing of markers which use pattern files 
 * as a means of detection. 
 * 
 * @author Adam Clarkson
 *
 */
public class PatternMarkerProcessor implements IMarkerProcessor {

	/**
	 * markerList - a list of all 
	 */
	private ArrayList<PatternMarker> markerList = new ArrayList<PatternMarker>();
	// 
	private List<IEventListener> listeners = new ArrayList<IEventListener>();
	private JmeNyARParam jmeARParameters;
	private CaptureQuad cameraBG;
	private NyARDetectMarker arDetector;
	private double defaultConfidenceRating = 0.5;
	private static final int MAX_NO_DETECTIONS = 5;

	public PatternMarkerProcessor(JmeNyARParam jmeARParameters,
			CaptureQuad cameraBG) {
		this.jmeARParameters = jmeARParameters;
		this.cameraBG = cameraBG;
	}

	public PatternMarkerProcessor(JmeNyARParam jmeARParameters,
			CaptureQuad cameraBG, double defaultConfidenceRating) {
		this.jmeARParameters = jmeARParameters;
		this.cameraBG = cameraBG;
		this.defaultConfidenceRating = defaultConfidenceRating;

	}
	//TODO update documentation of this class

	/**
	 * Update. This is called by SimpleUpdate to calculate what markers are
	 * currently in the scene and how the nodes are affected.
	 * 
	 * @param detector
	 *            The marker detector which is in use
	 * @param raster
	 *            The raster to use for detection
	 */
	public void update(INyARRgbRaster raster) {
		int foundMarkers = 0;
		if(arDetector == null) { 
			Log.warning("arDetector is null!");
			return;
		}
		try {
			foundMarkers = arDetector.detectMarkerLite(raster, 100);
			if (foundMarkers > 0) {
				NyARTransMatResult src = new NyARTransMatResult();
				for (int id = 0; id < markerList.size(); id++){
									PatternMarker m = markerList.get(id);
									int[] detectionInformation = findBestFitForMarker(foundMarkers, id);
									int bestDetectedFit = detectionInformation[0]; 
									double confidence = ((double)detectionInformation[1])/1000;
									if (bestDetectedFit == -1){
											m.incrementLossCounter();
									}else{
										if (confidence >= defaultConfidenceRating){
											
											if (m.getLossCounterValue() > MAX_NO_DETECTIONS) {
												m.setMarkerInScene(true);
												for (IEventListener l : listeners){
													l.markerAdded(m);
												}
											}
											m.resetLossCounter();
											arDetector.getTransmationMatrix(bestDetectedFit, src);
											for(IEventListener l : listeners){
												l.markerChanged(new MarkerChangedEvent(m,src));
											}
										}
									}
									if (m.getLossCounterValue() > MAX_NO_DETECTIONS){
										if (m.getMarkerInScene()){
											m.setMarkerInScene(false);
										
											for (IEventListener l : listeners){
												l.markerRemoved(m);
											}
											
										}
									}
					}
				
			}
		} catch (NyARException e) {
			e.printStackTrace();
		}
	}

	private int[] findBestFitForMarker(int numberOfMarkers, int id){
		
		int bestFit = -1;
		double bestConf = -1;
		
		for (int i =0; i<numberOfMarkers; i++){
			int codeForMarker = arDetector.getARCodeIndex(i);
			double conf = arDetector.getConfidence(i);
			
			if ((codeForMarker == id) && (conf > bestConf)){
				bestFit = i;
				bestConf = conf;
				
			}
		}
		
		int[] detectorInformation = {bestFit, (int)(bestConf * 1000)};
		return detectorInformation;
		
	}
	
	/**
	 * Creates a PatternMarker object, using given specifications from the
	 * application.
	 * 
	 * @param uid
	 *            A unique identifier string, which can be used to represent a
	 *            marker.
	 * @param segments
	 *            The number of segments dictating the resolution of the
	 *            pattern. This number is set when the pattern is created.
	 * @param path
	 *            The path to the file containing pattern data.
	 * @return PatternMarker Returns a PatternMarker object to the application,
	 *         returns null if a problem exists
	 */
	public PatternMarker createMarkerObject(String uid, int segments,
			String path, double markerWidth) {

		NyARCode code = null;
		try {
			code = new NyARCode(segments, segments);
			code.loadARPattFromFile(path);
		} catch (NyARException e) {
			e.printStackTrace();
		}
		if (code == null){
			return null;
		}else{
			return new PatternMarker(uid, code, markerWidth);
		}
	}

	/**
	 * Gets the marker at a specified Array Position. Due to the fact that
	 * NyARToolKit references detected markers based on their position in a
	 * primitive Array, we need to be able to relate a marker object to this
	 * array position.
	 * 
	 * @param positionInArray
	 *            The primitive array position for which we want the marker.
	 * @return PatternMarker
	 */
	public PatternMarker getMarkerFromArrayPosition(int positionInArray) {

		PatternMarker marker = null;

		for (PatternMarker m : markerList) {

			if (m.getCodeArrayPosition() == positionInArray) {
				marker = m;
			}
		}

		return marker;
	}

	/**
	 * Creates the List of NyARCodes which are used in the NyARToolKit detection
	 * program. This Code list forms the basis for all marker detection, through
	 * the ordering of the array.
	 * 
	 * @return NyARCode[] a list of NyARCodes.
	 */
	public NyARCode[] createARCodesList() {

		NyARCode[] codes = new NyARCode[markerList.size()];

		for (int i = 0; i < markerList.size(); i++) {

			codes[i] = markerList.get(i).getCode();
			// set the position of this marker in the codes array (to allow us
			// to detect what marker we are using later)
			markerList.get(i).setCodeArrayPosition(i);

		}
		return codes;
	}

	public void registerEventListener(IEventListener listener) {
		listeners.add(listener);
	}

	public void registerMarker(Marker m) {
		markerList.add((PatternMarker) m);
	}

	public void deregisterMarker(Marker m) {
		// TODO Not needed as yet.

	}

	@Override
	public void finaliseMarkers() {

		// TODO Need to document properly
		NyARCode[] codes = this.createARCodesList();
		double[] markerWidths = new double[markerList.size()];

		for (int i=0; i<markerList.size(); i++){
			markerWidths[i] = markerList.get(i).getWidth();
		}

		//remove everything from the scene to begin with, hides all content
		for (PatternMarker m : markerList){
			for (IEventListener l : listeners){
				l.markerRemoved(m);
			}
		}

		try {
			arDetector = new NyARDetectMarker(jmeARParameters, codes,
					markerWidths, codes.length, cameraBG
					.getRaster().getBufferType());
			
		} catch (NyARException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

	}

}
