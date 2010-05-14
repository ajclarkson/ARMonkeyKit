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
//

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
