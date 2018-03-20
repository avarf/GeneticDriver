package Model.Game.Track;

import Utils.Math.NURBS.NURBSCurve;
import org.dyn4j.geometry.Vector2;

/**
 * @author Matthieu Le Boucher
 */
public class Track {
    private Vector2[] controlPoints;

    private float[] nodes;

    private float[] weights;

    private int n;

    private Vector2[] points;

    public Track(Vector2[] controlPoints, float[] nodes, float[] weights, int n) {
        if(nodes.length != controlPoints.length + n + 1)
            throw new IllegalArgumentException("Invalid track parameters: the amount of nodes (" + nodes.length + ") " +
                    " must be equal to the amount of control points (" + controlPoints.length + ") plus the degree " +
                    " of the curve (" + n + ") plus one.");

        this.controlPoints = controlPoints;
        this.nodes = nodes;
        this.weights = weights;
    }

    private void evaluateCurve() {
        // Create linear space between first and last node.
        float step = 0.25f;
        points = new Vector2[(int) (Math.ceil(nodes[nodes.length - 1] - nodes[0]) / step)];

        int i = 0;
        for(float t = nodes[0]; t < nodes[nodes.length - 1]; t += step) {
            points[i] = NURBSCurve.evaluate(nodes, weights, controlPoints, n, t);
            i++;
        }
    }
}
