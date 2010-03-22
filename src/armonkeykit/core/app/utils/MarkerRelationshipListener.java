package armonkeykit.core.app.utils;

import java.util.ArrayList;
import java.util.List;

import jp.nyatla.nyartoolkit.core.transmat.NyARTransMatResult;

import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Line;
import com.jme.scene.Node;

import armonkeykit.core.events.AREventListener;
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
public class MarkerRelationshipListener implements AREventListener {

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
			} else if (rel.getTarget().equals(marker)) {
				source = rel.getSource();
				target = marker;
				track = true;
			} else {
				track = false;
			}

			if (track == true) {
				NyARTransMatResult transMatResult = source.getTransMatResult();
				NyARTransMatResult targetTransMat = new NyARTransMatResult();

				targetTransMat = target.getTransMatResult();
				Node n = rel.getNode();
				try {
					n.detachAllChildren();

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

}
