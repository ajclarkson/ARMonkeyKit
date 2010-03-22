package armonkeykit.core.markerprocessor.pattern;

import java.util.ArrayList;
import java.util.List;

import armonkeykit.core.events.AREventListener;
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

public class PatternMarkerProcessor implements IMarkerProcessor {

	// A list which stores all of the PatternMarker objects currently in use
	private ArrayList<PatternMarker> markerList = new ArrayList<PatternMarker>();
	private List<AREventListener> listeners = new ArrayList<AREventListener>();
	private JmeNyARParam jmeARParameters;
	private CaptureQuad cameraBG;
	private NyARDetectMarker arDetector;

	public PatternMarkerProcessor(JmeNyARParam jmeARParameters,
			CaptureQuad cameraBG) {
		this.jmeARParameters = jmeARParameters;
		this.cameraBG = cameraBG;
	}

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
		try {
			foundMarkers = arDetector.detectMarkerLite(raster, 200);
			if (foundMarkers > 0) {

				for (int i = 0; i < foundMarkers; i++) {

					for (PatternMarker m : markerList) {
						if (m.getCodeArrayPosition() == arDetector
								.getARCodeIndex(i)) {
							// TODO: move confidence rating
							if (arDetector.getConfidence(i) > 0.5) {
								NyARTransMatResult src = new NyARTransMatResult();
								arDetector.getTransmationMatrix(i, src);

								for (AREventListener l : listeners) {
									l.markerChanged(new MarkerChangedEvent(m,
											src));
								}
							} else {
								for (AREventListener l : listeners) {
									// TODO: consider making event for this
									l.markerRemoved(m);
								}
							}
						}
					}
				}
			} else {
				for (PatternMarker m : markerList) {
					for (AREventListener l : listeners) {
						l.markerRemoved(m);
					}
				}
			}
		} catch (NyARException e) {
			e.printStackTrace();
		}
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
			String path) {

		NyARCode code = null;
		try {
			code = new NyARCode(segments, segments);
			code.loadARPattFromFile(path);
		} catch (NyARException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (code == null)
			return null;
		else
			return new PatternMarker(uid, code);

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

	public void registerEventListener(AREventListener listener) {
		listeners.add(listener);
	}

	public void registerMarker(Marker m) {
		markerList.add((PatternMarker) m);
	}

	public void deregisterMarker(Marker m) {
		// TODO Not needed as yet.

	}

	public void finaliseMarkers() {

		// TODO Need to abstract the width from here
		NyARCode[] codes = this.createARCodesList();

		try {
			arDetector = new NyARDetectMarker(jmeARParameters, codes,
					new double[] { 80.0, 80.0 }, codes.length, cameraBG
							.getRaster().getBufferType());
		} catch (NyARException e) {
			e.printStackTrace();
		}

	}

}
