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
