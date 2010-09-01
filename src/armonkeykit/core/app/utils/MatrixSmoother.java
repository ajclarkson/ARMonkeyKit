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

/**
 * Adapted from code by Andrew Davison - ad@fivedots.coe.psu.ac.th
 */

package armonkeykit.core.app.utils;

import java.util.ArrayList;
import java.util.List;

import jp.nyatla.nyartoolkit.core.transmat.NyARTransMatResult;

import com.jme.math.Matrix4f;

public class MatrixSmoother {

	// TODO maybe write an automatic function based on standard deviation of the
	// averages
	private int historySize = 3;
	private List<Matrix4f> matrixHistory = new ArrayList<Matrix4f>();

	public void add(NyARTransMatResult transMatResult) {

		Matrix4f matrix = new Matrix4f((float) -transMatResult.m00,
				(float) -transMatResult.m01, (float) -transMatResult.m02,
				(float) -transMatResult.m03, (float) -transMatResult.m10,
				(float) -transMatResult.m11, (float) -transMatResult.m12,
				(float) -transMatResult.m13, (float) transMatResult.m20,
				(float) transMatResult.m21, (float) transMatResult.m22,
				(float) -transMatResult.m23, 0, 0, 0, 1);
		
		if((matrixHistory.size() == historySize)&& matrixHistory.size() > 0){
			matrixHistory.remove(0);
			
		}
		
		
		matrixHistory.add(matrix);

	}
	
	public Matrix4f getAverageMatrix(){
		if (matrixHistory.size() == 0){
			return null;
		}
		Matrix4f averageMatrix = new Matrix4f();
		for (Matrix4f matrix : matrixHistory){
			averageMatrix.addLocal(matrix);
		}
		
		averageMatrix.mult((float) (1.0/matrixHistory.size()));
		return averageMatrix;
	}

}
