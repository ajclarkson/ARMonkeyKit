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
package armonkeykit.core.app.utils.eventlisteners;

import java.util.ArrayList;
import java.util.List;

import jp.nyatla.nyartoolkit.core.transmat.NyARTransMatResult;

import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Line;
import com.jme.scene.Node;

import armonkeykit.core.app.utils.MarkerDistanceRelationship;
import armonkeykit.core.events.MarkerChangedEvent;
import armonkeykit.core.markers.Marker;

/**
 * Marker Relationship Listener. This class is used to just provide feedback
 * about the distance between two related markers. The user specifies a
 * relationship which they wish to track and each time a marker changed event is
 * fired, the distance between them is recalculated.
 * 
 * @author Adam Clarkson
 * 
 */
//TODO Work out why only some of the lines are being drawn?

public class MarkerRelationshipListener implements IEventListener {

	List<MarkerDistanceRelationship> distanceRelationships = new ArrayList<MarkerDistanceRelationship>();

	public void createDistanceRelationship(Marker source, Marker target,
			Node node) {
		distanceRelationships.add(new MarkerDistanceRelationship(source,
				target, node));
	}

	@Override
	public void markerChanged(MarkerChangedEvent event) {
		Marker marker = event.getMarker();
		boolean track = false;
		Marker source = null;
		Marker target = null;
		for (MarkerDistanceRelationship rel : distanceRelationships) {
			/*
			 * Allows the system to track relationships regardless of which way
			 * around they are declared, so can switch source to target and
			 * target to source. This helps with occlusion tracking.
			 */
			if (rel.getSource().equals(marker)) {
				source = marker;
				target = rel.getTarget();
				track = true;
//			} else if (rel.getTarget().equals(marker)) {
//				source = rel.getSource();
//				target = marker;
//				track = true;
			} else {
				track = false;
			}
			Node n = rel.getNode();
			if (track == true) {
				NyARTransMatResult transMatResult = source.getTransMatResult();
				NyARTransMatResult targetTransMat = new NyARTransMatResult();

				targetTransMat = target.getTransMatResult();
				
				try {
					//n.detachAllChildren();

					// store the positions of the markers as vectors for use in
					// calculations.
					Vector3f sourcePos = new Vector3f(
							(float) -transMatResult.m03,
							(float) -transMatResult.m13,
							(float) -transMatResult.m23);
					Vector3f targetPos = new Vector3f(
							(float) -targetTransMat.m03,
							(float) -targetTransMat.m13,
							(float) -targetTransMat.m23);
					// In order to draw a line we have to provide an array of
					// Vector3f objects
					Vector3f[] v = new Vector3f[2];
					v[0] = sourcePos;
					v[1] = targetPos;
					/**
					 * attempt line drawing
					 */

					Line line = new Line(source.getUniqueID() + ">"
							+ target.getUniqueID(), v, null, null, null);
					line.setSolidColor(ColorRGBA.green);

					line.setLineWidth(10f);

					n.attachChild(line);

				} catch (NullPointerException e) {
					// In the case that one of the markers in the relationship
					// cannot be found, all lines are removed from the node.
					n.detachAllChildren();
				}

			}else {
				n.detachAllChildren();
			}

		}

	}

	@Override
	public void markerRemoved(Marker m) {
		// TODO Yet to implement a markerRemoved event across the whole
		// framework

	}

	@Override
	public void associate(Marker m, Node n) {
		// TODO unneeded - may remove this from the interface

	}

	@Override
	public void markerAdded(Marker m) {
		// TODO Auto-generated method stub
		
	}

}
