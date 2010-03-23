package armonkeykit.core.markers;

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

	/**
	 * Create a new PatternMarker Object. 
	 * @param uid Unique Identifier
	 * @param code NyARCode specifying the segments and filepath
	 */
	public PatternMarker(String uid, NyARCode code, double markerWidth) {
		this.uid = uid;
		this.code = code;
		this.width = markerWidth;
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

}
