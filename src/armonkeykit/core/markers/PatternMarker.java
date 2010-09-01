/*******************************************************************************
*    This file is part of ARMonkeyKit.
*
*    ARMonkeyKit is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    ARMonkeyKit is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with ARMonkeyKit.  If not, see <http://www.gnu.org/licenses/>.
******************************************************************************/
package armonkeykit.core.markers;

import armonkeykit.core.app.utils.MatrixSmoother;
import jp.nyatla.nyartoolkit.core.NyARCode;
import jp.nyatla.nyartoolkit.core.transmat.NyARTransMatResult;

/**
 * PatternMarker Object. This is an object representation of a marker with a hardcoded representation.
 * This is stored as part of the marker object and is one type of marker which can be used with the system.
 * @author Adam Clarkson
 *
 */
public class PatternMarker implements Marker {
//TODO update documentation to reflect marker width changes
	
	private String uid;
	private NyARCode code;
	private int codeArrayPosition;
	private NyARTransMatResult transMatResult;
	private double width;
	private double confidence;
	private MatrixSmoother matrixSmooth;
	private int lossCounter;
	private boolean markerInScene;

	/**
	 * Create a new PatternMarker Object. 
	 * @param uid Unique Identifier
	 * @param code NyARCode specifying the segments and filepath
	 */
	public PatternMarker(String uid, NyARCode code, double markerWidth) {
		this.uid = uid;
		this.code = code;
		this.width = markerWidth;
		matrixSmooth = new MatrixSmoother();
		this.lossCounter = 0;
		this.markerInScene = false;
	}
	
	public String getUniqueID() {
		return uid;
	}

	/**
	 * Returns the marker's NyARCode.
	 * @return NyARCode
	 */
	public NyARCode getCode() {
		return code;
	}

	/**
	 * Store the position of this marker in the NyARCode[] array, used by NyARToolkit to determine
	 * which marker has been detected.
	 * @param codeArrayPosition
	 */
	public void setCodeArrayPosition(int codeArrayPosition) {
		this.codeArrayPosition = codeArrayPosition;
	}

	/**
	 * Obtain the NyARCode[] array position of this marker in order to access its properties.
	 * @return int position in the array.
	 */
	public int getCodeArrayPosition() {
		return codeArrayPosition;
	}
	public void setTransMatResult(NyARTransMatResult transMatResult){
		this.transMatResult = transMatResult;
	}
	@Override
	public NyARTransMatResult getTransMatResult() {
		
		return transMatResult;
	}
	
	public void setWidth(double width) {
		this.width = width;
	}
	
	public double getWidth(){
		return this.width;
	}
	
	public void setConfidence(double confidence){
		this.confidence = confidence;
		
	}
	
	public double getConfidence() {
		return this.confidence;
	}

	public MatrixSmoother getMatrixSmoother(){
		return this.matrixSmooth;
	}

	@Override
	public int getLossCounterValue() {
		return this.lossCounter;
	}

	@Override
	public void incrementLossCounter() {
		this.lossCounter++;
		
	}

	@Override
	public void resetLossCounter() {
		this.lossCounter = 0;
		
	}

	@Override
	public boolean getMarkerInScene() {
		// TODO Auto-generated method stub
		return this.markerInScene;
	}

	@Override
	public void setMarkerInScene(boolean markerInScene) {
		this.markerInScene = markerInScene;
	}
}
