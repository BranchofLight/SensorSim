package com.company;

import java.util.ArrayList;
import java.util.List;

public class RigidRouting {
    private double totalDistanceMoved;
    private List<Double> newVertices;

    private List<Double> vertices;
    private double range;
    private int length = 1;

    RigidRouting(List<Double> v, double r){
        totalDistanceMoved = 0;
        setVertices(v);
        //setLength(l);
        setRange(r);
    }

    public List<Double> getVertices() {
        return vertices;
    }

    private void setVertices(List<Double> vertices) {
        this.vertices = vertices;
    }

    public double getRange() {
        return range;
    }

    private void setRange(double range) {
        this.range = range;
    }

    public int getLength() {
        return length;
    }

    /* Be careful, total distance moved gets calculated as following:
     * if moving left, subtract distance moved, if moving right, add.
     * This is to avoid calculating the same vertix moving multiple times twice.
     * If the total number of movements are negative, the number returned is the absolute value.
     * */
    public double getTotalDistanceMoved() {
        return Math.abs(totalDistanceMoved);
    }

    public void setTotalDistanceMoved(double totalDistanceMoved) {
        this.totalDistanceMoved = totalDistanceMoved;
    }

    public List<Double> getNewVertices() {
        return newVertices;
    }

    public void setNewVertices(List<Double> newVertices) {
        this.newVertices = newVertices;
    }

    public List<Double> rigidRouting() {
        // To avoid the dilemma of the first vertex, I just added a ghost vertex
        // with vertex -range
        List<Double> vs = new ArrayList<Double>();
        vs.add(-range);
        vs.addAll(vertices);

        double dist;
        for (int i = 1; i < vs.size(); i++) {
            if (vs.get(i - 1) > (vs.get(i) - (2 * range))) {
                dist = vs.get(i - 1) - (vs.get(i) - (2 * range));
                totalDistanceMoved+=dist;
				/* If the vertex is within range of length, don't move it. */
                if (vs.get(i) + dist <= (length - range)) {
                    vs.set(i, vs.get(i) + dist);
                }
            }
            if (vs.get(i - 1) < (vs.get(i) - (2 * range))) {
                dist = vs.get(i) - (2 * range) - vs.get(i - 1);
                totalDistanceMoved-=dist;
                vs.set(i, vs.get(i) - dist);
            }
        } // for
        vs.remove(0);
        return vs;
    }
}
