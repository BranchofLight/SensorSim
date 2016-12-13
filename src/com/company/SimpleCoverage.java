package com.company;

import java.util.ArrayList;
import java.util.List;

public class SimpleCoverage {
    private List<Integer> vertices;
    private int range;
    private int length;

    private List<Integer> newVertices;
    private List<Integer> distances;
    private int sumDistance;

    /*public SimpleCoverage(List<Integer> v, int l, int r){
        distances = new ArrayList<Integer>();
        setVertices(v);
        setLength(l);
        setRange(r);
    }*/
    public SimpleCoverage(List<Double> v, double r){
        distances = new ArrayList<Integer>();
        List<Integer> vs = new ArrayList<Integer>();
        for(int i = 0; i < v.size(); i++){
            vs.add((int)(v.get(i)*10));
        }
        setVertices(vs);
        length = 10;
        setRange((int)(r*10));
    }
    public SimpleCoverage(List<Double> v, double r, int digit){
        distances = new ArrayList<Integer>();
        List<Integer> vs = new ArrayList<Integer>();
        for(int i = 0; i < v.size(); i++){
            vs.add((int)(v.get(i)*digit));
        }
        setVertices(vs);
        length = digit;
        setRange((int)(r*digit));
    }
    public List<Integer> getVertices() {
        return vertices;
    }
    public List<Double> getDoubleVertices() {
        List<Double> myVertices = new ArrayList<Double>();
        getNewVertices();
        for(int i = 0; i < newVertices.size(); i++){
            double partA = newVertices.get(i);
            double result = partA/10;
            myVertices.add(i, result); // what about zeros?!
        }
        return myVertices;
    }
    private void setVertices(List<Integer> vertices) {
        this.vertices = vertices;
    }

    public int getRange() {
        return range;
    }

    private void setRange(int range) {
        this.range = range;
    }

    public int getLength() {
        return length;
    }

    private void setLength(int length) {
        this.length = length;
    }

    public List<Integer> getNewVertices() {
        if(newVertices == null){
            setNewVertices(distances);
        }
        return newVertices;
    }

    private void setNewVertices(List<Integer> distances) {
        newVertices = new ArrayList<Integer>();
        for(int i = 0; i < vertices.size(); i++){
            //System.out.println("Vertix " + vs.get(i) + " moved by " + newDistance.get(vs.get(i)));
            int newVertix = (vertices.get(i)+distances.get(i)); //vertices.get(i) because it is identified by index now
            newVertices.add(newVertix);
        }
    }

    public List<Integer> getDistances() {
        return distances;
    }

    public int getSumDistance() {
        if(sumDistance == 0){
            setSumDistance();
        }
        return sumDistance;
    }

    public double getSumDoubleDistance() {
        if(sumDistance == 0){
            setSumDistance();
        }
        double sum = sumDistance;
        sum = sum/length;
        return sum;
    }

    private void setSumDistance() {
        sumDistance = 0;
        for(int i = 0; i < distances.size(); i++){
            sumDistance += distances.get(i);
        }
    }

    public List<List<Integer>> getGaps() {
        List<List<Integer>> gaps = new ArrayList<List<Integer>>();

        List<Integer> pregap = new ArrayList<Integer>();
        pregap.add(0, 0);
        //pregap.add(1, vertices.get(0));
        pregap.add(1, 0);
        if (vertices.get(0) < range) { // overlap
            pregap.add(2, -(range - vertices.get(0)));
        }
        else if (vertices.get(0) > range) { // gap
            pregap.add(2, vertices.get(0) - range);
        }
        else if (vertices.get(0) == range) { // gap
            pregap.add(2, 0);
        }
        gaps.add(pregap);
        for (int i = 1; i < vertices.size(); i++) {
            int difference = vertices.get(i) - vertices.get(i - 1);
            List<Integer> gap = new ArrayList<Integer>();
            //gap.add(0, vertices.get(i - 1));
            //gap.add(1, vertices.get(i));
            gap.add(0, i-1);
            gap.add(1, i);
            if(difference == 0){
                gap.add(2, -(2*range));
            }
            else if (difference > (2 * range)) {
                // gap
                int d = difference - (2 * range);
                gap.add(2, d);
            } else if (difference < (2 * range)) {
                // overlap
                int ov = (2 * range) - difference;
                gap.add(2, -ov);
            } else if (difference == (2 * range)) {
                // exactly where they should be
                gap.add(2, 0); // watch out for this edge case.
            }
            gaps.add(gap);
        } // for gaps add last gap.

        List<Integer> postgap = new ArrayList<Integer>();
        //postgap.add(0, vertices.get(vertices.size() - 1));
        //postgap.add(1, length);
        postgap.add(0, vertices.size()-1);
        postgap.add(1, vertices.size());
        int diff = (length - vertices.get(vertices.size() - 1));

        if (diff > range) { // gap
            int g = length - (vertices.get(vertices.size() - 1) + range);
            postgap.add(2, g);
        }
        if (diff < range) { // overlap
            int o = range - (length - vertices.get(vertices.size() - 1));
            postgap.add(2, -o);
        }
        if (diff == range) {
            postgap.add(2, 0);
        }
        gaps.add(postgap);
        return gaps;
    }

    /* Does NOT return a list of new vertices. Returns a list of each vertix' movements
     * Call getNewVertices() to get a list of new vertices */
    public List<Integer> rGreaterL() { //List<Integer> vertices, int length, int range
        //List<Integer> distances = new ArrayList<Integer>();
        for (int i = 0; i < vertices.size(); i++) {
            distances.add(0);
        }
        // get all the gaps & all the overlaps
        // order them in such a way that they are ordered. ie, include start and
        // end of each
        List<List<Integer>> gaps = getGaps();

        for (int i = 0; i < gaps.size(); i++) { // removed -1
            int costLeft = 0;
            int costRight = 0;
            int c = 0;
            if (gaps.get(i).get(2) > 0) {
                if (i > 0) {
                    // check left for overlaps
                    if (gaps.get(i - 1).get(2) < 0) {
                        // calculate cost of left - for now set cost to 1
                        costLeft = 1;
                    } else {
                        // if its another gap, ignore it. or set it to infinite
                        costLeft = 1000;
                    }
                } // make sure i-1 doesn't cause a NPE

                if (i + 1 < gaps.size()) {
                    if (gaps.get(i + 1).get(2) < 0) {
                        // calculate cost of right. - for now set cost to 1
                        costRight = 1;
                    } else {
                        // if its another gap, ignore it. or set it to infinite
                        costRight = 1000;
                    }
                }
                if(i == 0){
                    int vertexShifted = gaps.get(0).get(1);
                    // shift it left by gap distance.
                    distances.set(vertexShifted, -gaps.get(0).get(2));
                    int newVertixLoc = vertices.get(0)-gaps.get(0).get(2);
                    //update the next gap.
                    int diff = vertices.get(1)-newVertixLoc;
                    if(diff == 0){
                        gaps.get(1).set(2, -(2*range));
                    }
                    else if(diff > (2*range)){
                        //gap
                        int d = diff - (2*range);
                        gaps.get(1).set(2,d);
                    }
                    else if(diff < (2*range)){
                        //overlap
                        int ov = (2*range) - diff;
                        gaps.get(1).set(2,ov);
                    }else if(diff == (2*range)){
                        //exactly where they should be
                        gaps.get(1).set(2,0);
                    }
                }
                else if (costLeft <= costRight) { // right shift is done - ONLY if its not the first vertex
                    if (Math.abs(gaps.get(i - 1).get(2)) < gaps.get(i).get(2)) {
                        c = Math.abs(gaps.get(i - 1).get(2)); // size of left overlap
                    } else {
                        c = gaps.get(i).get(2); // size of current gap
                    }
                    gaps.get(i).set(2, gaps.get(i).get(2) - c);
                    gaps.get(i - 1).set(2, Math.abs(gaps.get(i - 1).get(2)) - c);
                    int vertixShifted = gaps.get(i - 1).get(1); /*at this point, vertix shifted points to the index, not the actual vertex */
                    distances.set(vertixShifted, distances.get(vertixShifted)+c);
                    // add c to the values in array d of sensors between
                }
                else if(i == gaps.size()-1){
                    if(gaps.get(i-1).get(2) < 0){
                        int movementRoom = Math.abs(gaps.get(i-1).get(2));
                        // to avoid the nasty edge case of movementRoom putting vertex above length
                        if(movementRoom >= gaps.get(i).get(2)){
                            movementRoom = gaps.get(i).get(2);
                        }
                        // what if movement room puts it above length? then you start crying.
                        gaps.get(i-1).set(2, gaps.get(i-1).get(2)+movementRoom);
                        gaps.get(i).set(2,  movementRoom);
                        System.out.println("Vertex: " + gaps.get(i).get(0) + " Length is: " + length + " Distances size: " + distances.size() + " What is i? " + i);
                        distances.set(gaps.get(i).get(0), distances.get(gaps.get(i).get(0))+movementRoom);
                    }
                }
                else { // left shift is done
                    if (Math.abs(gaps.get(i + 1).get(2)) < Math.abs(gaps.get(i).get(2))) {
                        c = Math.abs(gaps.get(i + 1).get(2));
                    } else {
                        c = gaps.get(i).get(2);
                    }
                    gaps.get(i).set(2, gaps.get(i).get(2) - c);
                    gaps.get(i + 1).set(2, Math.abs(gaps.get(i + 1).get(2)) - c);
                    int vertixShifted = gaps.get(i + 1).get(0); /*add this to array of d. Negative */
                    // vertixShifted is now an index, not the vertex itself.
					/* This tells where a vertix should move */
                    distances.set(vertixShifted, distances.get(vertixShifted)-c);
                }
            } // if not first vertix

        }// for

        return distances;
    }// end of method
}
