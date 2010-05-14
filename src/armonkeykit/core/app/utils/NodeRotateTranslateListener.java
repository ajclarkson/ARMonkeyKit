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
package armonkeykit.core.app.utils;

import java.util.HashMap;
import java.util.Map;

import jp.nyatla.nyartoolkit.core.transmat.NyARTransMatResult;

import com.jme.math.Matrix3f;
import com.jme.math.Matrix4f;
import com.jme.scene.Node;

import armonkeykit.core.events.IEventListener;
import armonkeykit.core.events.MarkerChangedEvent;
import armonkeykit.core.markers.Marker;

/**
 * NodeRotateTranslateListener. An AREventListener implementation which allows
 * association of markers to nodes, and then allows for the translation of those
 * nodes based on the marker translation.
 * 
 * @author Adam Clarkson
 * 
 */
public class NodeRotateTranslateListener implements IEventListener {

	Map<String, Node> markerToNode = new HashMap<String, Node>();
	boolean matrixSmoothing = false;
	
	public NodeRotateTranslateListener() {
		
	}
	
	public NodeRotateTranslateListener(boolean matrixSmoothing){
		this.matrixSmoothing = matrixSmoothing;
	}
	
	

	
	public void associate(Marker m, Node n) {
		markerToNode.put(m.getUniqueID(), n);
	}

	private Node getNodeForMarker(Marker marker) {
		return markerToNode.get(marker.getUniqueID());
	}

	public void markerChanged(MarkerChangedEvent event) {
		Node model = getNodeForMarker(event.getMarker());
		if(model == null) return;
		
		NyARTransMatResult transMatResult = event.getTransMatResult();
		
		
		if (matrixSmoothing){
			MatrixSmoother matSmooth = event.getMarker().getMatrixSmoother();
			
			matSmooth.add(transMatResult);
			Matrix4f averageMatrix = matSmooth.getAverageMatrix();
			
			model.setLocalRotation(new Matrix3f((float) averageMatrix.m00, (float) averageMatrix.m01,(float) averageMatrix.m02,
					(float) averageMatrix.m10, (float) averageMatrix.m11, (float) averageMatrix.m12, (float) averageMatrix.m20,
					(float) averageMatrix.m21, (float) averageMatrix.m22));
			//TODO sort out the translation problem here using average matrix
			
			model.setLocalTranslation((float) averageMatrix.m03/10, (float) averageMatrix.m13/10, (float) averageMatrix.m23/10);
			
			System.out.println("TRANS: [" + -transMatResult.m03 + ", " + -transMatResult.m13 + ", " + -transMatResult.m23 + "], AVG: [" +
						averageMatrix.get(0,3)/10 + ", " + averageMatrix.get(1,3)/10 + ", " + averageMatrix.get(2,3)/10 + "]");
		}else{
		
			model.setLocalRotation(new Matrix3f((float) -transMatResult.m00,
				(float) -transMatResult.m01, (float) transMatResult.m02,
				(float) -transMatResult.m10, (float) -transMatResult.m11,
				(float) transMatResult.m12, (float) -transMatResult.m20,
				(float) -transMatResult.m21, (float) transMatResult.m22));
		
			model.setLocalTranslation((float) -transMatResult.m03,
				(float) -transMatResult.m13, (float) -transMatResult.m23);
		
		}
	}

	public void markerRemoved(Marker m) {
		Node n = getNodeForMarker(m);
		if(n != null) n.setLocalTranslation(0f, 0f, -100000);
	}


	@Override
	public void markerAdded(Marker m) {
		
	}

}
