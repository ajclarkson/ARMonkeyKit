/*
 * Copyright (c) 2010, ARMonkeyKit
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'ARMonkeyKit' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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
