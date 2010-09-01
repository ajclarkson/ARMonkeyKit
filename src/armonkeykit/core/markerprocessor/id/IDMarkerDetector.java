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
