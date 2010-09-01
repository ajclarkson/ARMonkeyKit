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
import jp.nyatla.nyartoolkit.core.transmat.NyARTransMatResult;


public class NyIDMarker implements Marker {

	private String uid;
	private NyARTransMatResult transMatResult;
	private int lossCounter;

	public NyIDMarker(int id) {
		this.uid = "" + id;
	}

	public String getUniqueID() {
		return uid;
	}

	public void setTransMatResult(NyARTransMatResult transMatResult){
		this.transMatResult = transMatResult;
	}
	@Override
	public NyARTransMatResult getTransMatResult() {
		
		return transMatResult;
	}

	@Override
	public MatrixSmoother getMatrixSmoother() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getLossCounterValue() {
		return lossCounter;
		// TODO Auto-generated method stub
		
	}

	@Override
	public void incrementLossCounter() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resetLossCounter() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getMarkerInScene() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setMarkerInScene(boolean markerInScene) {
		// TODO Auto-generated method stub
		
	}
}
