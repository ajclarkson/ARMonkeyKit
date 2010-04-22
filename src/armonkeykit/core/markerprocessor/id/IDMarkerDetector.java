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

package armonkeykit.core.markerprocessor.id;

import jp.nyatla.nyartoolkit.NyARException;
import jp.nyatla.nyartoolkit.core.param.NyARParam;
import jp.nyatla.nyartoolkit.core.squaredetect.NyARSquare;
import jp.nyatla.nyartoolkit.core.transmat.NyARTransMatResult;
import jp.nyatla.nyartoolkit.nyidmarker.data.INyIdMarkerData;
import jp.nyatla.nyartoolkit.nyidmarker.data.NyIdMarkerDataEncoder_RawBit;
import jp.nyatla.nyartoolkit.nyidmarker.data.NyIdMarkerData_RawBit;
import jp.nyatla.nyartoolkit.processor.SingleNyIdMarkerProcesser;

public class IDMarkerDetector extends SingleNyIdMarkerProcesser{


	private int currentID;
	private NyARTransMatResult transMatResult;

	public IDMarkerDetector(NyARParam jmeARParameters, int markerWidth, int rasterFormat) {
		super();
		try {
			initInstance(jmeARParameters, new NyIdMarkerDataEncoder_RawBit(), markerWidth, rasterFormat);
		} catch (NyARException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onEnterHandler(INyIdMarkerData arCode) {
		NyIdMarkerData_RawBit rawCode = (NyIdMarkerData_RawBit)arCode;
		//if the length of identified code is more than 4, it is outside of scope for our markers. 
		if (rawCode.length > 4) {
			this.currentID=-1; //signifies undefined ID.

		}else {
			this.currentID = 0; //replace null value to perform bit shifting
			for (int i=0; i<rawCode.length;i++){
				this.currentID = (this.currentID << 8 | rawCode.packet[i]);
			}
		}
		this.transMatResult = null;
	}

	@Override
	protected void onLeaveHandler() {
		this.currentID=-1;
		this.transMatResult=null;
	}

	@Override
	protected void onUpdateHandler(NyARSquare square, NyARTransMatResult result) {
		this.transMatResult = result;
	}

	public NyARTransMatResult getTransMatResult(){
		return this.transMatResult;
	}

	public int getCurrentID(){
		return this.currentID;
	}

}
